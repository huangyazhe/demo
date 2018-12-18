package com.hyz.security.security3;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * RSA 加解密工具类 （JDK自带RSA加解密性能差些，这些选用 bouncycastle 来处理加解密，初次加载性能上优化了 5倍到 1个数量级左右）
 * @author HuangYazhe
 * Date: 2018/12/18
 */
public class RSAUtils {

    /** RSA最大加密明文大小 */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /** 文件读取缓冲区大小 */
    private static final int CACHE_SIZE = 1024;


    /**
     * RSA 加密
     * @param data  加密数据
     * @return
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    public static String encryptData(String data) throws IOException, InvalidCipherTextException {
        //这里可以将密钥对保存到本地
        final Base64.Encoder encoder64 = Base64.getEncoder();
        final Base64.Decoder decoder64 = Base64.getDecoder();

        //这里也可以从流中读取，从本地导入
        String publicInfoStr = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCqyvJfXtQLoip9olA5Gy3i3wJ73QXzVcHD6levgQlB5IG5D5ya2kxvDUpnhoP7zk4KtFvbKO3Xb0jBQwD09I8KXfeNosZAUkjOvBqXpzsyKQmJhyCQQ0oD/S9qlWV4YTC7yakh8Pcv5tSt6D8ZyP/j2Ei6goILtEH85A/GlLC8ewIBAw==";
        byte[] publicInfoBytes = decoder64.decode(publicInfoStr);
        ASN1Object pubKeyObj = ASN1Primitive.fromByteArray(publicInfoBytes);

        AsymmetricKeyParameter pubKey = PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(pubKeyObj));
        AsymmetricBlockCipher cipher = new RSAEngine();
        cipher.init(true, pubKey);

        byte[] resEncryptData = doEncryptDataStream(data,cipher);
        System.out.println("密文："+ encoder64.encodeToString(resEncryptData));

        return encoder64.encodeToString(resEncryptData);
    }


    /**
     * RSA 解密
     * @param data 解密数据
     * @return
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    public static String decryptData(String data) throws IOException, InvalidCipherTextException {
        final Base64.Decoder decoder64 = Base64.getDecoder();
        AsymmetricBlockCipher cipher = new RSAEngine();
        byte[] encryptDataBytes = decoder64.decode(data);

        //解密
        String priavteKeyInfoStr = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAKrK8l9e1AuiKn2iUDkbLeLfAnvdBfNVwcPqV6+BCUHkgbkPnJraTG8NSmeGg/vOTgq0W9so7ddvSMFDAPT0jwpd942ixkBSSM68GpenOzIpCYmHIJBDSgP9L2qVZXhhMLvJqSHw9y/m1K3oPxnI/+PYSLqCggu0QfzkD8aUsLx7AgEDAoGAHHcoZTp4rJsHFPBitC8yUHqAaforqI5K9fxj8pWBivtq9C1ExHm3Z9eMZpZrVKJiVx4PTzF8+T02yuCAKNNtLB6t3aIwYZQjjBxkWoodQEux4AdgBvBC7oZuLh00Npq/okbIpfYQUAiEPamRBSd4OT6TgzM4UQ58bxLH/0sihucCQQDuyi0Rf3hi/qWl3f6R9Wda6evNFjLo+AxUzTeuLbjMRsUDUB52PXrbhegqaMH0VDViKRc8m9HkoDVYxlCMUnq3AkEAtxouxCR+dnTga+J52JZJB/RdjcozuMBggc0eN/539GwpHaUftlfU2DVTxHA459pW/qqQEpRTeLctGpl6RY8YXQJBAJ8xc2D/pZdUbm6T/wv475HxR94OzJtQCDiIz8lz0IgvLgI1aaQo/JJZRXGbK/g4I5bGD329Nphqzjsu4F2MUc8CQHoRdILC/vmjQEfsUTsO21qi6Qkxd9CAQFaIvs/++qLyxhPDanmP4zrON9hK0JqRj1RxtWG4N6XPc2cQ/C5fZZMCQBfOyK1GMXMIDgxeVH8/FtCIQNek+zGbPUS0sZqKwzKs8g5aDw8IqO1tMwqy8LQWBtKfqhP8o057A6c0+4NgUn8=";
        byte[] privateInfoBytes = decoder64.decode(priavteKeyInfoStr);

        AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoBytes);
        cipher.init(false, priKey);
        byte[] decriyptData = doDecriyptDataStream(encryptDataBytes,cipher);

        System.out.println("解密后数据：" + new String(decriyptData, "utf-8"));

        return new String(decriyptData, "utf-8");
    }


    /**
     * 分段加密
     * @param encryptedData  加密数据
     * @param cipher         对接密码
     * @return
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    public static byte[] doEncryptDataStream(String encryptedData, AsymmetricBlockCipher cipher)
            throws IOException, InvalidCipherTextException {

        int i = 0;
        int offSet = 0;
        byte[] cache = new byte[0];
        int inputLen = encryptedData.length();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.processBlock(encryptedData.getBytes("utf-8"), offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.processBlock(encryptedData.getBytes("utf-8"), offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 分段解密
     * @param decryptData  解密数据
     * @param cipher       对接密码
     * @return
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    public static byte[] doDecriyptDataStream(byte[] decryptData, AsymmetricBlockCipher cipher)
            throws IOException, InvalidCipherTextException {

        int i = 0;
        int offSet = 0;
        byte[] cache = new byte[0];
        int inputLen = decryptData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.processBlock(decryptData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.processBlock(decryptData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }


    public static void main(String[] args) throws IOException, InvalidCipherTextException {

        //第一次耗时会长一些，数据加载的原因
        for (int i = 0; i < 1 ; i++) {
            //GenerateKeyPair();
            String data = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890adfasfa";
            long st = System.currentTimeMillis();
            String encryptData = encryptData(data);

            long t = System.currentTimeMillis();
            System.out.println("加密时间 "+(t - st));

            System.out.println("encryptData: "+encryptData);

            String decryptData = decryptData(encryptData);

            long t1 = System.currentTimeMillis();
            System.out.println("解密时间: "+(t1 - st));

            System.out.println("decryptData: "+decryptData);
        }

    }

}
