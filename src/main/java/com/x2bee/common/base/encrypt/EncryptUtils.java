package com.x2bee.common.base.encrypt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class EncryptUtils{
    public static final char DEFAULT_REPLACE = '*';
    
    private static AwsCryptoUtil AWS_CRYPTO_UTIL = null;
    
    @Autowired
    private void setAwsCryptoUtil(AwsCryptoUtil awsCryptoUtil) {
    	AWS_CRYPTO_UTIL = awsCryptoUtil;
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
		        value = AWS_CRYPTO_UTIL.encrypt(value);
			break;
			
			/**
			 * 단방향 암호화. 
			 */
			case CRYPTO:
				// 사용안함(spring security passwordEncoder 사용)
				//value = CryptoUtil.encryptSHA256(value);
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
		        value = AWS_CRYPTO_UTIL.decrypt(value);
			break;
			
			/**
			 * 단방향 암호화
			 */
			case CRYPTO:
			break;
		}

		return value;
	}
}