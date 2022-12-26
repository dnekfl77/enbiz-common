package com.x2bee.common.base.exception;

import lombok.Getter;

@Getter
public class AppException extends CommonException {
	private static final long serialVersionUID = 6044478616579898320L;

	private String errorCode;
	private String errorMessage;
	
	public static AppException exception(AppError appError) {
		throw new AppException(appError.getCode(), MessageResolver.getMessage(appError));
	}

	public static AppException exception(AppError appError, Object[] args) {
		throw new AppException(appError.getCode(), MessageResolver.getMessage(appError, args));
	}

	public AppException(String errorCode, String errorMessage) {
		super(errorMessage);
		
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
