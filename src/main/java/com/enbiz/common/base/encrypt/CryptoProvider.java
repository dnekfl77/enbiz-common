package com.enbiz.common.base.encrypt;

public interface CryptoProvider {
    public String encrypt(String data);
    public String decrypt(String data);
}
