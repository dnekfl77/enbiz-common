package com.x2bee.common.base.encrypt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x2bee.common.base.constant.X2Constants;
import com.x2bee.common.base.util.PNBase64Utils;

/*
 * 현재 기준 JAVA의 기본 정책은 AES128을 권장하고 있지만
 * AES256 암복호화 사용할 수 있도록 별도 라이브러리를 제공하고 있다.
 * Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files을 JAVA 버전에 맞게 다운받아
 * local_policy.jar 파일을 추가해 줘야 사용가능하다.
 *
 * 개발환경 기준 'C:\OYO2016\tools\jdk1.7.0_11\jre\lib\security'에 local_policy.jar 에 추가
 */
public class AES256Cipher {
    
	private static volatile AES256Cipher INSTANCE;
	// TODO ***
	//public static final String secretKey = EnvironmentsConfig.getStatically("aes256cipherEncryptKey");
	public static final String secretKey = "dhfflqmdudqordhvltmtltmxpadkaghk";
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
//    private static final String UTF8 = "UTF-8";

    protected static final Logger LOGGER = LoggerFactory.getLogger("controller.cat");

    private AES256Cipher(){}

    public synchronized static AES256Cipher getInstance(){
        if(INSTANCE==null){
            INSTANCE=new AES256Cipher();
        }
        return INSTANCE;
    }

    private SecretKeySpec getKeySpec(String inkey) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = new byte[32];
        SecretKeySpec spec = null;
        bytes = secretKey.getBytes();
        if(inkey != null && !X2Constants.EMPTY.equals(inkey)){
            bytes = inkey.getBytes();
        }
        spec = new SecretKeySpec(bytes, ALGORITHM);
        return spec;
    }

    // 암호화
    public String encrypt(String encryptedStr, String inkey) throws Exception {

        if (null == encryptedStr
                || (encryptedStr.length() <= 6 && encryptedStr.trim().toUpperCase().indexOf("NULL") >= 0))
            return "";

        Cipher ecipher;

        try {
            SecretKeySpec spec = getKeySpec(inkey);
            ecipher = Cipher.getInstance(TRANSFORMATION);
            ecipher.init(Cipher.ENCRYPT_MODE, spec);

            byte[] utf8 = encryptedStr.getBytes(StandardCharsets.UTF_8);
            byte[] enc = ecipher.doFinal(utf8);

            return PNBase64Utils.base64Encode(enc);
        } catch (Exception e) {
            throw e;
        }
    }

    // 복호화
    public String decrypt(String encryptedStr, String inkey) throws Exception {

        if (null == encryptedStr
                || (encryptedStr.length() <= 6 && encryptedStr.trim().toUpperCase().indexOf("NULL") >= 0))
            return "";

        LOGGER.debug(" DECRYPTSTR=[{}]", encryptedStr);
        LOGGER.debug(" DECRYPTINKEY=[{}]", inkey);

        Cipher dcipher;

        try {
            SecretKeySpec spec = getKeySpec(inkey);
            dcipher = Cipher.getInstance(TRANSFORMATION);
            dcipher.init(Cipher.DECRYPT_MODE, spec);

            byte[] dec = PNBase64Utils.base64Decode(encryptedStr);
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error(" DECRYPTSTR=[{}]", encryptedStr);
            LOGGER.error(" DECRYPTINKEY=[{}]", inkey);
            throw e;
        }
    }

    public String encrypt(String message) throws Exception {
        return encrypt(message, secretKey);
    }

    public String decrypt(String encrypted) throws Exception {
        return decrypt(encrypted, secretKey);
    }
    
    
}
