package com.x2bee.common.base.token;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenRequest {
	// 사용자이름
	@NotEmpty
	private String userName;
	// 비밀번호
	private String password;
	// 토큰유효기간
	private Long validMillis;
}
