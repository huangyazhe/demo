package com.hyz.security.security3;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi;

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
        String publicInfoStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBxDSlHWNyQR3PAeJhzcgWgS9iVMtZEzL82lwb7XT2SeaUgN2Sxn9sqi9GxuPua847w7vgUj0xa1zIP3VPlhTPiaoxZ8UTxXKAqnrvTB2aFrlE4PW/yxGF1FuidM2zymzUkQkCBE9qlDjjea3wK5hh5Y9gqDL3rEr6fgYTm2mC0QIDAQAB";
        byte[] publicInfoBytes = decoder64.decode(publicInfoStr);
        ASN1Object pubKeyObj = ASN1Primitive.fromByteArray(publicInfoBytes);

        AsymmetricKeyParameter pubKey = PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(pubKeyObj));
        AsymmetricBlockCipher cipher = new RSAEngine();
        OAEPEncoding oaepEncoding = new OAEPEncoding(cipher);
        cipher.init(true, pubKey);

        byte[] resEncryptData = doEncryptDataStream(data,oaepEncoding);
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
        AsymmetricBlockCipher cipher = new RSABlindedEngine();
        OAEPEncoding oaepEncoding = new OAEPEncoding(cipher);
        byte[] encryptDataBytes = decoder64.decode(data);

        //解密
        String priavteKeyInfoStr = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMHENKUdY3JBHc8B\n"
                + "4mHNyBaBL2JUy1kTMvzaXBvtdPZJ5pSA3ZLGf2yqL0bG4+5rzjvDu+BSPTFrXMg/\n"
                + "dU+WFM+JqjFnxRPFcoCqeu9MHZoWuUTg9b/LEYXUW6J0zbPKbNSRCQIET2qUOON5\n"
                + "rfArmGHlj2CoMvesSvp+BhObaYLRAgMBAAECgYEAji+HHh6Zqe6kjBHq6DAUAoeb\n"
                + "mMF2Uo/nG2q0qn2uFUiXXiPN8/Wa7cdYV8x816jeNjbkd7CBXPFWrU77q4ILFBQS\n"
                + "PIWVc11bq22HFlWlwTCs7/g/UR0ekEgrlolZDyxTcWgmY8S2NALxr0uR/TwLya8f\n"
                + "5YpcCH8P7BEsLtKhv00CQQDxnAfdmRcxn/6WDopVtf/RkzS74L2PSgtVKaHIPnEp\n"
                + "KXMAXg9uyK7/uklNRUTt2YDDNsK81RBySSpWm6HMQv8vAkEAzU6unylYuUghsNgY\n"
                + "Tc54RK33oV4ZqKWDmzkOt5S0A08HoDO6jRVzR20f8JnJmXWqPcop1Cue43hh3dYt\n"
                + "JUsd/wJBALL6RsldMtVMFCfMtaUwoUT6q0HSBhozW5nGsVXJC8LWNZ68DuqeNySx\n"
                + "NsPK4HjheoUh97gyjXBbysVFnOHXb3kCQQClqo8HyaJZaBYfkFAUQL4VlVeTs836\n"
                + "owxObb0tb+XOIbBimjs3aw6pnSm/ySi/Fw53a7FTDpvYq6Q1EIU/aZzbAkEAq1mY\n"
                + "VJ6cv/ek8ZjUL/SxZeJUkQNdksHJlAEbn4/WvY2+qEHI8vO0hZDPkbtZeVRsQG2u\n" + "nICCnuQ4cOg0WE+etg==";

        byte[] privateInfoBytes = org.apache.commons.codec.binary.Base64.decodeBase64(priavteKeyInfoStr);

        AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoBytes);
        cipher.init(false, priKey);
        byte[] decriyptData = doDecriyptDataStream(encryptDataBytes,oaepEncoding);

        System.out.println(new String(decriyptData,"utf-8"));

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
            String data = "1234567890123456789012345678901234567890112345678901234567890123456789012345678901123456789012345678901234567890123456789011234567890123456789012345678901234567890112345678901234567890123456789012345678901";
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

        //String data = "YC0pqPc0QoIvgmv4+Vlz58nBIS2XlAIj3bDdqZnEwPJy3GLUllDXCRRI4lqCeGwSwliYu3ncJuA6EP/qXR1rD7nRtJ0ARUp3jLXEclWYU/pzlGqJEVoy0So5YOtpng57CvCuW+mfJ3VeqC/hdfeGsQKpXL6fqySFhx/LE7en9sg=";
        //String decryptData = decryptData(data);

    }

}
