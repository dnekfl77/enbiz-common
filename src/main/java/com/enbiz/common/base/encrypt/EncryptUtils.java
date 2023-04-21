package com.enbiz.common.base.encrypt;

import org.apache.commons.lang3.StringUtils;

public class EncryptUtils{
    public static final char DEFAULT_REPLACE = '*';
    
    private static CryptoProvider cryptoProvider = null;
    
    public EncryptUtils(CryptoProvider cryptoProvider) {
    	EncryptUtils.cryptoProvider = cryptoProvider;
    }
    
	/**
 	 * 암호화 Value
	 */
    public static String getEncryptValue(String value) throws Exception {
        return getEncryptValue(value, EncryptType.DEFAULT);
    }
    
	/**
	* 암호화 Value
	*/
	public static String getEncryptValue(String value, EncryptType type) throws Exception {
		if(StringUtils.isEmpty(value)) {
			return null;
		}
		switch (type) {
		
		/**
		 * DEFAULT
		 * - insert, Update: 암호화
		 */
		case DEFAULT:
			// 일반문자열 --> 암호화문자열 sctype=enc
	        value = cryptoProvider.encrypt(value);
			break;
		default:
			break;
		}

		return value;
	}
    
	/**
	* 복호화 Value
	*/
    public static String getDecryptValue(String value) throws Exception {
        return getDecryptValue(value, EncryptType.DEFAULT);
    }

    /**
	* 복호화 Value
	*/
	public static String getDecryptValue(String value, EncryptType type) throws Exception {
		if(StringUtils.isEmpty(value)) {
			return null;
		}
		switch (type) {
		
		/**
		 * DEFAULT
		 * - Select: 복호화
		 */
		case DEFAULT:
	        // 암호화문자열 --> 일반문자열 sctype=dec
	        value = cryptoProvider.decrypt(value);
			break;
		default:
			break;
		}

		return value;
	}
}