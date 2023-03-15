package jasypt;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;

public class JwtSample {

	public static void main(String[] args) {
		var key = "jwtMemberAccessKeyWithEnoughLength";
		var secretKey = new SecretKeySpec(Base64.getEncoder().encode(key.getBytes()), "HmacSHA256");
		var builder = Jwts.builder();
		
		// header configuration
		builder.setHeaderParam("typ", "JWT");

		// claim configuration
		builder.setIssuer("enbiz");
		builder.setSubject("enbiz member token");
		builder.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30));
		builder.setIssuedAt(new Date());
		
			builder.claim("id", 1_000_000_000_000L);
			builder.claim("username", "dnekfl77@");
			builder.claim("roles", Arrays.asList("ROLE_MEMBER"));
		

		// signature configuration
		builder.signWith(secretKey);

		var token = "";
		System.out.println((token = builder.compact()));
		
		
		token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJlbmJpeiIsInN1YiI6ImVuYml6IG1lbWJlciB0b2tlbiIsImV4cCI6MTY4MDc1ODkwMywiaWF0IjoxNjgwNzU3MTAzLCJpZCI6MSwidXNlcm5hbWUiOiJkbmVrZmw3N0AiLCJyb2xlcyI6WyJST0xFX01FTUJFUiJdfQ.SVKpNrBE7Fx0t_mApJf3XZhA4wWEbcU4bFfVsiPb9dVnvun_EnLZFr6cEQnN2quY";
		var jws = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
		
		System.out.println(jws.getBody());
	}
}
