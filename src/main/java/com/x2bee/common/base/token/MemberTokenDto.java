package com.x2bee.common.base.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberTokenDto {
	private String accessToken;
	private String refreshToken;
}
