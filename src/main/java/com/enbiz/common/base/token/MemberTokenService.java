package com.enbiz.common.base.token;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class MemberTokenService implements TokenServiceForFilter {
	@Value("${jwt.member.access.key:defaultJwtMemberAccessKeyWithEnoughLength}")
	private String accessKey;
	@Value("${jwt.member.refresh.key:defaultJwtMemberRefreshKeyWithEnoughLength}")
	private String refreshKey;
	private final String algorithm = "HmacSHA256";

	private SecretKey accessSecretKey;
	private SecretKey refreshSecretKey;

    @PostConstruct
    protected void init() {
    	accessSecretKey = new SecretKeySpec(Base64.getEncoder().encode(accessKey.getBytes()), algorithm);
    	refreshSecretKey = new SecretKeySpec(Base64.getEncoder().encode(refreshKey.getBytes()), algorithm);
    }

	// access token 유효시간: 30분
	private static final Integer accessExpMin = 30;
	// refresh token 유효시간: 7일
	private static final Integer refreshExpMin = 60*24*7;

	private static final String ISSUER = "enbiz";
	private static final String SUBJECT = "enbiz member token";

	public MemberTokenDto create(UserDetail userInfo) {
		// Create Access Token
		String accessToken = createJws(accessSecretKey, accessExpMin, userInfo);

		// Create Refresh Token
		String refreshToken = createJws(refreshSecretKey, refreshExpMin, UserDetail.builder().username(userInfo.getUsername()).build());

		MemberTokenDto tokens = new MemberTokenDto();
		tokens.setAccessToken(accessToken);
		tokens.setRefreshToken(refreshToken);

		return tokens;

	}

	private String createJws(SecretKey secretKey, Integer expMin, UserDetail userDetail) {
		//JWT Builder create
		JwtBuilder builder = Jwts.builder();

		// header configuration
		builder.setHeaderParam("typ", "JWT");

		// claim configuration
		builder.setIssuer(ISSUER);
		builder.setSubject(SUBJECT);
		builder.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expMin));
		builder.setIssuedAt(new Date());
		if(userDetail != null) {
			builder.claim("id", userDetail.getId());
			builder.claim("username", userDetail.getUsername());
			builder.claim("roles", Arrays.asList("ROLE_MEMBER"));
		}

		// signature configuration
		builder.signWith(secretKey);
		String jws = builder.compact();

		return jws;

	}

	/**
	 * 리프레시 토큰 유효성 검증
	 * @param token
	 * @return
	 */
	public boolean verifyRefreshToken(String token) {
		try {
			Jwts.parserBuilder()
    			.setSigningKey(refreshSecretKey)
    			.build()
    			.parseClaimsJws(token);
		} catch (Exception e) {
			log.error("[COMMON][MEMBER_TOKEN_SERVICE] invalidate token: " + token, e);
			return false;
		}
		return true;
	}

	@Override
	public boolean verifyToken(String token) {
    	try {
			final Jws<Claims> jws = parseToken(token);

			final String tokenSubject = jws.getBody().getSubject();
			final String tokenIssuer = jws.getBody().getIssuer();

			if ( StringUtils.hasText(tokenSubject) && StringUtils.hasText(tokenIssuer) ) {
				return tokenSubject.equals(SUBJECT) && tokenIssuer.equals(ISSUER);
			}
		} catch (Exception e) {
			log.info("[COMMON][MEMBER_TOKEN_SERVICE] invalidate token: " + token);
		}

        return false;
	}

	@Override
	public Jws<Claims> parseToken(String token) {
    	return Jwts.parserBuilder()
    			.setSigningKey(accessSecretKey)
    			.build()
    			.parseClaimsJws(token);
	}
	
	@Override
	public Jws<Claims> parseRefreshToken(String token) {
		return Jwts.parserBuilder()
    			.setSigningKey(refreshSecretKey)
    			.build()
    			.parseClaimsJws(token);
	}
}
