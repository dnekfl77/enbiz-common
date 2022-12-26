/**
 * 
 */
package com.x2bee.common.base.encrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author choiyh44
 * @version 1.0
 * @since 2021. 12. 3.
 *
 */
@Component
@Lazy
@Slf4j
public class AwsCryptoUtil {
    @Value("${aws.kms.keyArn}")
    private String keyArn;
    
    private AwsCrypto crypto;
    private KmsMasterKeyProvider keyProvider;
    private Map<String, String> encryptionContext;
    
    @PostConstruct
    public void init() {
    	// Default 알고리즘: CryptoAlgorithm.ALG_AES_256_GCM_HKDF_SHA512_COMMIT_KEY_ECDSA_P384
        crypto = AwsCrypto.builder()
                .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
//                .withCommitmentPolicy(CommitmentPolicy.ForbidEncryptAllowDecrypt)
//                .withEncryptionAlgorithm(CryptoAlgorithm.ALG_AES_256_GCM_IV12_TAG16_NO_KDF)
                .build();
        keyProvider = KmsMasterKeyProvider.builder().buildStrict(keyArn);
        encryptionContext = Collections.singletonMap("TheHandsomeContextKey", "TheHandsomeContextValue");
        
    }

    /**
     * 평문을 암호화 한다.
     * @param data
     * @return
     */
    public String encrypt(String data) {
        CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(keyProvider, data.getBytes(StandardCharsets.UTF_8), encryptionContext);
        byte[] ciphertext = encryptResult.getResult();
        String result = Base64.encodeBase64URLSafeString(ciphertext);
        return result;
    }
    
    /**
     * 암호문을 평문화 한다.
     * @param data
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String decrypt(String data) throws UnsupportedEncodingException {
        byte[] ciphertext = Base64.decodeBase64(data);
        final CryptoResult<byte[], KmsMasterKey> decryptResult = crypto.decryptData(keyProvider, ciphertext);
        String result = new String(decryptResult.getResult(), "UTF-8");
        return result;
    }
    
    /**
     * 암호문의 길이 예측
     * @param plaintextSize
     * @return
     */
    public long estimateCiphertextSize(final int plaintextSize) {
    	return crypto.estimateCiphertextSize(keyProvider, plaintextSize);
    }
    
}
