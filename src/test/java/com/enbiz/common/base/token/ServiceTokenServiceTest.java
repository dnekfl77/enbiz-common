package com.enbiz.common.base.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "jwt.service.key=secret-encryption-needed" })
class ServiceTokenServiceTest {
	@Autowired
	private static ServiceTokenService TokenService = null;

	@Test
	void createToken() {
		ServiceTokenDto tokenDto = TokenService.createToken(new TokenRequest().setUsername("service"));
		
		Assertions.assertNotNull(tokenDto);
	}

}
