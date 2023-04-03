package com.enbiz.common.base.token;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * api 서버간 restapi 호출 시 인증을 위한 토큰 서비스
 * @author choiyh44
 *
 */
@Component
@Slf4j
public class ServiceTokenService implements TokenServiceForFilter {
	@Value("${jwt.service.key:defaultJwtServiceKeyWithEnoughLength}")
	private String jwtServiceKey;
	private final String algorithm = "HmacSHA256";
	private SecretKey secretKey;
	private final String issuer = "enbiz";
	private final String subject = "enbiz service token";

    private final long TOKEN_VALID_MILISECOND = 1000L * 60; // 60초

    @PostConstruct
    protected void init() {
        secretKey = new SecretKeySpec(Base64.getEncoder().encode(jwtServiceKey.getBytes()), algorithm);
    }

    public ServiceTokenDto createToken(TokenRequest tokenRequest) {
    	UserDetail userDetail = UserDetail.builder().username(tokenRequest.getUsername()).build();
		return ServiceTokenDto.builder().token(generateToken(userDetail, Arrays.asList("ROLE_SERVICE"))).build();
    }
    
    public String generateToken(UserDetail userDetail,List<String> roles) {
    	final long now = System.currentTimeMillis();

        Claims claims = Jwts.claims();
        claims.put("userDetail", userDetail);
        claims.put("roles", roles);
        
		return Jwts.builder()
				.setClaims(claims)
			    .setSubject(subject)
			    .setIssuer(issuer)
			    .setIssuedAt(new Date(now))
			    .setNotBefore(new Date(now - TOKEN_VALID_MILISECOND))
			    .setExpiration(new Date(now + TOKEN_VALID_MILISECOND))
			    .signWith(secretKey)
			    .compact();
    }

    @Override
    public boolean verifyToken(String token) {
    	try {
    		final Jws<Claims> jws = parseToken(token);
    		
    		final String tokenSubject = jws.getBody().getSubject();
    		final String tokenIssuer = jws.getBody().getIssuer();
    		
    		if ( StringUtils.hasText(tokenSubject) && StringUtils.hasText(tokenIssuer) ) {
    			return tokenSubject.equals(this.subject) && tokenIssuer.equals(this.issuer);
    		}
		} catch (Exception e) {
			log.info("[COMMON][SERVICE_TOKEN_SERVICE] invalidate token: " + token);
		}
    	
        return false;
    }

    @Override
    public Jws<Claims> parseToken(String token) {
    	return Jwts.parserBuilder()
    			.setSigningKey(secretKey)
    			.build()
    			.parseClaimsJws(token);
    }
    
}
