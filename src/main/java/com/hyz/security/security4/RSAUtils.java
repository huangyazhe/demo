package com.hyz.security.security4;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * java JDK 自带工具包调用 ,和 IOS已经联调成功
 */
public class RSAUtils {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String _ALGORITHM = "RSA";



    public static Map<String, String> createKeys(int keySize){
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try{
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        //keyPairMap.put("publicKey", publicKeyStr);
        //keyPairMap.put("privateKey", privateKeyStr);
        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBxDSlHWNyQR3PAeJhzcgWgS9i\n"
                + "VMtZEzL82lwb7XT2SeaUgN2Sxn9sqi9GxuPua847w7vgUj0xa1zIP3VPlhTPiaox\n"
                + "Z8UTxXKAqnrvTB2aFrlE4PW/yxGF1FuidM2zymzUkQkCBE9qlDjjea3wK5hh5Y9g\n" + "qDL3rEr6fgYTm2mC0QIDAQAB";

        String pri = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMHENKUdY3JBHc8B\n"
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
        keyPairMap.put("publicKey", pub);
        keyPairMap.put("privateKey", pri);

        return keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey){
        try{
            final java.util.Base64.Encoder encoder64 = java.util.Base64.getEncoder();
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding",getInstance());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] splitdata = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), 799);//.bitLength()
            return encoder64.encodeToString(splitdata);
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding",getInstance());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            System.out.println("bitLength: "+privateKey.getModulus().bitLength());
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = 86;//keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }


    public static void main (String[] args) throws Exception {
        /*Map<String, String> keyMap = RSAUtils.createKeys(1024);
        String  publicKey = keyMap.get("publicKey");
        String  privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

        System.out.println("公钥加密——私钥解密");
        String str = "12345678901234567890123456789012345678901234adfadfasfadfasfafadsf12345678901234567890123456789012345678901234adfadfasfadfasfafadsf12345678901234567890123456789012345678901234adfadfasfadfasfafadsf12345678901234567890123456789012345678901234adfadfasfadfasfafadsf";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(publicKey));
        System.out.println("密文：\r\n" + encodedData);
        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
        System.out.println("解密后文字: \r\n" + decodedData);*/

        String encodedData = "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";
        Map<String, String> keyMap = RSAUtils.createKeys(1024);
        String  privateKey = keyMap.get("privateKey");

        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
        System.out.println("解密后文字: \r\n" + decodedData);

    }


    private static BouncyCastleProvider bouncyCastleProvider = null;
    public static synchronized BouncyCastleProvider getInstance() {
        if (bouncyCastleProvider == null) {
            bouncyCastleProvider = new BouncyCastleProvider();
        }
        return bouncyCastleProvider;
    }




}
