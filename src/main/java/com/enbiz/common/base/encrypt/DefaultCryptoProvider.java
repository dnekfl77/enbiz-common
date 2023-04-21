package com.enbiz.common.base.encrypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import lombok.extern.slf4j.Slf4j;

/**
 * F&F DB 데이터 암복화 로직. AES256 보완.
 * @author choiyh44
 *
 */
@Slf4j
public class DefaultCryptoProvider implements CryptoProvider {
	private String key;

	public DefaultCryptoProvider(String key) {
		this.key = key;
	}

	@Override
	public String encrypt(String data) {
		try {
			Key keySpec = getAESKey();
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
			byte[] encrypted = c.doFinal(data.getBytes("UTF-8"));
			return new String(Hex.encodeHex(encrypted));
		} catch (Exception e) {
			log.error("", e);
			return data;
		}
	}

	@Override
	public String decrypt(String data) {
		try {
			Key keySpec = getAESKey();
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
			byte[] byteStr = Hex.decodeHex(data);
			String decStr = new String(c.doFinal(byteStr), "UTF-8");
			return decStr;
		} catch (Exception e) {
			log.error("", e);
			return data;
		}
	}

	public Key getAESKey() throws Exception {
		Key keySpec;

		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");

		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}

		System.arraycopy(b, 0, keyBytes, 0, len);
		keySpec = new SecretKeySpec(keyBytes, "AES");

		return keySpec;
	}

}
