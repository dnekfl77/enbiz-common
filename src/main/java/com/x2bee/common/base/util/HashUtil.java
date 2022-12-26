package com.x2bee.common.base.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x2bee.common.base.constant.X2Constants;

/**
 * md5 hash CJPG와 통신에 필요한 Hash데이터 처리 Utility.
 * 
 * @author User
 *
 */
public class HashUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashUtil.class);

    private HashUtil() {

    }

    

    /**
     * md5로 변환
     * @param value
     * @return
     */
    public static String convertMD5(String value){
        String converted = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            md.update(value.getBytes()); 
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder(); 
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            converted = sb.toString();
            
        }catch(NoSuchAlgorithmException e){
            LOGGER.error("MD5 converting failed.", e);
        }
        return converted;
    }
    
    
    /**
     * 
     * CJPG 주문 요청 시 HashData 생성한다.
     * 
     * @param plainText
     * 
     * @return
     * 
     * @throws UnsupportedEncodingException
     */
    public static String getEncryptMD5ForCjpg(String plainText) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        String encryptedMD5 = X2Constants.EMPTY;
        MessageDigest md = null;
            
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());            
            byte[] byteData = md.digest();            
            for(int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            } 
            encryptedMD5 = sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        return encryptedMD5;
    }

}
