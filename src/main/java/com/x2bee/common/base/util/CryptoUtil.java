package com.x2bee.common.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.x2bee.common.base.exception.FrameworkException;

@Component
@Scope(value = "prototype")
public class CryptoUtil{
    private static final String ALGORITHM = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";
    private static final String UTF8 = "UTF-8";
    
    @Autowired
    private Environment environment;

    private String encryptKey;
    private SecretKey key;
    private IvParameterSpec iv;
    
    private CryptoUtil() {
    	super();
    }

    public CryptoUtil(String encryptKey) {
    	this();
        this.encryptKey = encryptKey;
    }

    public String encrypt(String plainText){
    	initialize();
        Cipher ecipher;
        
        try {
            ecipher = Cipher.getInstance(TRANSFORMATION);
            ecipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] utf8 = plainText.getBytes(UTF8);
            byte[] enc = ecipher.doFinal(utf8);

            return Base64.encodeBase64String(enc);
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }

    public String decrypt(String encryptedText){
    	initialize();
        Cipher dcipher = null;

        try {
            dcipher = Cipher.getInstance(TRANSFORMATION);
            dcipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] dec = Base64.decodeBase64(encryptedText.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, UTF8);
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }

    /**
     * 복호화 불가능한 암호문을 만들기 위해 SHA256으로 암호화하는 메소드
     * 
     * @param plainText
     *            암호화 대상 평문 문자열
     * @return 암호화된 문자열
     */
    public static final String encryptSHA256(String plainText){
        if (plainText == null){
            throw new IllegalArgumentException("input text cannot be null.");
        }
        
	    MessageDigest digester = null;
		try {
			digester = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new FrameworkException(e);
		}
        
	    digester.update(plainText.getBytes());
	    byte[] encryptedBytes = digester.digest();
        
        StringBuilder buffer = new StringBuilder();

        for (byte b : encryptedBytes) {
            String str = Integer.toString((b & 0xff) + 0x100, 16).substring(1);
            buffer.append(str);
        }

        return buffer.toString();
    }

	private void initialize(){
		if(encryptKey == null) {
    		encryptKey = environment.getProperty("app.crypto.encryptKey");
    	}
		try {
            DESedeKeySpec keySpec = new DESedeKeySpec(this.encryptKey.getBytes());
            key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
            iv = new IvParameterSpec(new byte[8]);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
	}
}
