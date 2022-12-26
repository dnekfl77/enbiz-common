package com.x2bee.common.base.encrypt;

/**
 * Encryption
 * @author N.J.Kim
 *
 */
public enum EncryptType {
	/**
	 * DEFAULT
	 * - Select: 복호화
	 * - insert, Update: 암호화
	 */
	DEFAULT,
	/**
	 * 단방향 암호화
	 * @deprecated spring security passwordEncoder 사용한다.
	 */
	CRYPTO
}