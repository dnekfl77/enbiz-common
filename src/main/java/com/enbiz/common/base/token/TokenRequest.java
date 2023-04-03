package com.enbiz.common.base.token;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenRequest {
	// 사용자이름
	@NotEmpty
	private String username;
	// 비밀번호
	private String passwd;
	// 토큰유효기간
	private Long validMillis;
}
