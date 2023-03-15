package com.enbiz.common.base.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends UserDefinedException {
	private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMessage;

	public CommonException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public CommonException(String message) {
		super(message);
		
	}

	public CommonException(Throwable cause) {
		super(cause);
		
	}
	
	public CommonException(String errorCode, String errorMessage) {
		super(errorMessage);
		
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
}
