package com.enbiz.common.base.rest;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Response<T> {
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp = LocalDateTime.now();
	private String code = "0000";
	private String message = "";
	private T payload;

	public static <T> Response<T> success(T payload) {
		return success(payload, StringUtils.EMPTY);
	}

	public static <T> Response<T> success(T payload, String message) {
		return new Response<T>().setPayload(payload).setMessage(message);
	}

	public static <T> Response<T> failure(String message) {
		return failure("9999", message, null);
	}

	public static <T> Response<T> failure(String code, String message) {
		return failure(code, message, null);
	}

	public static <T> Response<T> failure(String code, String message, T payload) {
		return new Response<T>().setCode(code).setMessage(message).setPayload(payload);
	}
}
