package com.enbiz.common.base.token;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ServiceTokenDto {
	private String token;
}
