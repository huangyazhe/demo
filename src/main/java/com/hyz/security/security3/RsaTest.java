package com.hyz.security.security3;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * RAS 加解密
 * @author huangyazhe
 */
public class RsaTest {

    /** RSA最大加密明文大小 */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        long st = System.currentTimeMillis();
        /*//生成密钥对
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        RSAKeyGenerationParameters rsaKeyGenerationParameters = new RSAKeyGenerationParameters(BigInteger.valueOf(3),
                new SecureRandom(), CACHE_SIZE, 25);
        //初始化参数
        rsaKeyPairGenerator.init(rsaKeyGenerationParameters);

        long t = System.currentTimeMillis();
        System.out.println("初始化参数 "+(t - st));

        AsymmetricCipherKeyPair keyPair = rsaKeyPairGenerator.generateKeyPair();

        long t1 = System.currentTimeMillis();
        System.out.println("generateKeyPair "+(t1 - st));

        //公钥
        AsymmetricKeyParameter publicKey = keyPair.getPublic();
        //私钥
        AsymmetricKeyParameter privateKey = keyPair.getPrivate();



        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey);
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo(privateKey);

        long t2 = System.currentTimeMillis();
        System.out.println("创建KEY "+(t2 - t1));

        //变字符串
        ASN1Object asn1ObjectPublic = subjectPublicKeyInfo.toASN1Primitive();
        byte[] publicInfoByte = asn1ObjectPublic.getEncoded();
        ASN1Object asn1ObjectPrivate = privateKeyInfo.toASN1Primitive();
        byte[] privateInfoByte = asn1ObjectPrivate.getEncoded();


        System.out.println("PublicKey:\n" + encoder64.encodeToString(publicInfoByte));
        System.out.println("PrivateKey:\n" + encoder64.encodeToString(privateInfoByte));

        */

        long t3 = System.currentTimeMillis();
        //System.out.println("这里可以将密钥对保存到本地 "+(t3 - t2));
        //这里可以将密钥对保存到本地
        final Base64.Encoder encoder64 = Base64.getEncoder();
        final Base64.Decoder decoder64 = Base64.getDecoder();


        String publicInfoStr = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCqyvJfXtQLoip9olA5Gy3i3wJ73QXzVcHD6levgQlB5IG5D5ya2kxvDUpnhoP7zk4KtFvbKO3Xb0jBQwD09I8KXfeNosZAUkjOvBqXpzsyKQmJhyCQQ0oD/S9qlWV4YTC7yakh8Pcv5tSt6D8ZyP/j2Ei6goILtEH85A/GlLC8ewIBAw==";
        byte[] publicInfoBytes = decoder64.decode(publicInfoStr);

        ASN1Object pubKeyObj = ASN1Primitive.fromByteArray(publicInfoBytes); //这里也可以从流中读取，从本地导入

        //这里也可以从流中读取，从本地导入
        //ASN1Object pubKeyObj = subjectPublicKeyInfo.toASN1Primitive();
        AsymmetricKeyParameter pubKey = PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(pubKeyObj));
        AsymmetricBlockCipher cipher = new RSAEngine();
        //true表示加密
        cipher.init(true, pubKey);

        long t4 = System.currentTimeMillis();
        System.out.println("加密、解密 初始化 "+(t4 - t3));


        //加密
        String data = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890adfasfa";
        System.out.println("\n明文：" + data);

        byte[] resEncryptData = doEncryptDataStream(data,cipher);
        System.out.println("密文："+ encoder64.encodeToString(resEncryptData));

        long t5 = System.currentTimeMillis();
        System.out.println("加密后数据耗时 "+(t5 - t4));

        //解密
        String priavteKeyInfoStr = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAKrK8l9e1AuiKn2iUDkbLeLfAnvdBfNVwcPqV6+BCUHkgbkPnJraTG8NSmeGg/vOTgq0W9so7ddvSMFDAPT0jwpd942ixkBSSM68GpenOzIpCYmHIJBDSgP9L2qVZXhhMLvJqSHw9y/m1K3oPxnI/+PYSLqCggu0QfzkD8aUsLx7AgEDAoGAHHcoZTp4rJsHFPBitC8yUHqAaforqI5K9fxj8pWBivtq9C1ExHm3Z9eMZpZrVKJiVx4PTzF8+T02yuCAKNNtLB6t3aIwYZQjjBxkWoodQEux4AdgBvBC7oZuLh00Npq/okbIpfYQUAiEPamRBSd4OT6TgzM4UQ58bxLH/0sihucCQQDuyi0Rf3hi/qWl3f6R9Wda6evNFjLo+AxUzTeuLbjMRsUDUB52PXrbhegqaMH0VDViKRc8m9HkoDVYxlCMUnq3AkEAtxouxCR+dnTga+J52JZJB/RdjcozuMBggc0eN/539GwpHaUftlfU2DVTxHA459pW/qqQEpRTeLctGpl6RY8YXQJBAJ8xc2D/pZdUbm6T/wv475HxR94OzJtQCDiIz8lz0IgvLgI1aaQo/JJZRXGbK/g4I5bGD329Nphqzjsu4F2MUc8CQHoRdILC/vmjQEfsUTsO21qi6Qkxd9CAQFaIvs/++qLyxhPDanmP4zrON9hK0JqRj1RxtWG4N6XPc2cQ/C5fZZMCQBfOyK1GMXMIDgxeVH8/FtCIQNek+zGbPUS0sZqKwzKs8g5aDw8IqO1tMwqy8LQWBtKfqhP8o057A6c0+4NgUn8=";
        byte[] privateInfoBytes = decoder64.decode(priavteKeyInfoStr);

        AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoBytes);
        //false表示解密
        cipher.init(false, priKey);
        byte[] decriyptData = doDecriyptDataStream(resEncryptData,cipher);
        String decryptData = new String(decriyptData, "utf-8");
        System.out.println("解密后数据：" + decryptData);

        long t6 = System.currentTimeMillis();
        System.out.println("解密后数据耗时 "+(t6 - t5));

    }

    public static String encryptData(String data) throws IOException, InvalidCipherTextException {
        //这里可以将密钥对保存到本地
        final Base64.Encoder encoder64 = Base64.getEncoder();
        final Base64.Decoder decoder64 = Base64.getDecoder();

        String publicInfoStr = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCqyvJfXtQLoip9olA5Gy3i3wJ73QXzVcHD6levgQlB5IG5D5ya2kxvDUpnhoP7zk4KtFvbKO3Xb0jBQwD09I8KXfeNosZAUkjOvBqXpzsyKQmJhyCQQ0oD/S9qlWV4YTC7yakh8Pcv5tSt6D8ZyP/j2Ei6goILtEH85A/GlLC8ewIBAw==";
        byte[] publicInfoBytes = decoder64.decode(publicInfoStr);
        ASN1Object pubKeyObj = ASN1Primitive.fromByteArray(publicInfoBytes); //这里也可以从流中读取，从本地导入

        //这里也可以从流中读取，从本地导入
        AsymmetricKeyParameter pubKey = PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(pubKeyObj));
        AsymmetricBlockCipher cipher = new RSAEngine();
        //true表示加密
        cipher.init(true, pubKey);

        //加密
        //String data = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890adfasfa";
        System.out.println("\n明文：" + data);

        byte[] resEncryptData = doEncryptDataStream(data,cipher);
        System.out.println("密文："+ encoder64.encodeToString(resEncryptData));

        String encryptData = encoder64.encodeToString(resEncryptData);
        return encryptData;
    }

    public static String decryptData(String data) throws IOException, InvalidCipherTextException {
        final Base64.Decoder decoder64 = Base64.getDecoder();
        AsymmetricBlockCipher cipher = new RSAEngine();
        byte[] encryptDataBytes = decoder64.decode(data);
        //解密
        String priavteKeyInfoStr = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAKrK8l9e1AuiKn2iUDkbLeLfAnvdBfNVwcPqV6+BCUHkgbkPnJraTG8NSmeGg/vOTgq0W9so7ddvSMFDAPT0jwpd942ixkBSSM68GpenOzIpCYmHIJBDSgP9L2qVZXhhMLvJqSHw9y/m1K3oPxnI/+PYSLqCggu0QfzkD8aUsLx7AgEDAoGAHHcoZTp4rJsHFPBitC8yUHqAaforqI5K9fxj8pWBivtq9C1ExHm3Z9eMZpZrVKJiVx4PTzF8+T02yuCAKNNtLB6t3aIwYZQjjBxkWoodQEux4AdgBvBC7oZuLh00Npq/okbIpfYQUAiEPamRBSd4OT6TgzM4UQ58bxLH/0sihucCQQDuyi0Rf3hi/qWl3f6R9Wda6evNFjLo+AxUzTeuLbjMRsUDUB52PXrbhegqaMH0VDViKRc8m9HkoDVYxlCMUnq3AkEAtxouxCR+dnTga+J52JZJB/RdjcozuMBggc0eN/539GwpHaUftlfU2DVTxHA459pW/qqQEpRTeLctGpl6RY8YXQJBAJ8xc2D/pZdUbm6T/wv475HxR94OzJtQCDiIz8lz0IgvLgI1aaQo/JJZRXGbK/g4I5bGD329Nphqzjsu4F2MUc8CQHoRdILC/vmjQEfsUTsO21qi6Qkxd9CAQFaIvs/++qLyxhPDanmP4zrON9hK0JqRj1RxtWG4N6XPc2cQ/C5fZZMCQBfOyK1GMXMIDgxeVH8/FtCIQNek+zGbPUS0sZqKwzKs8g5aDw8IqO1tMwqy8LQWBtKfqhP8o057A6c0+4NgUn8=";
        byte[] privateInfoBytes = decoder64.decode(priavteKeyInfoStr);

        AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoBytes);
        //false表示解密
        cipher.init(false, priKey);
        byte[] decriyptData = doDecriyptDataStream(encryptDataBytes,cipher);
        String decryptData = new String(decriyptData, "utf-8");
        System.out.println("解密后数据：" + decryptData);


        return decryptData;
    }



    public static byte[] doEncryptDataStream(String encryptedData, AsymmetricBlockCipher cipher)
            throws IOException, InvalidCipherTextException {

        int inputLen = encryptedData.length();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache = new byte[0];
        int i = 0;
        // 对数据分段解密
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


    public static byte[] doDecriyptDataStream(byte[] decryptData, AsymmetricBlockCipher cipher)
            throws IOException, InvalidCipherTextException {

        int inputLen = decryptData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache = new byte[0];
        int i = 0;
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

}

