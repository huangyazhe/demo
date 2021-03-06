package com.hyz.security.security3;


import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.generators.*;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.crypto.engines.*;
import org.bouncycastle.math.*;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.x509.*;
import org.bouncycastle.util.*;
import org.bouncycastle.asn1.pkcs.*;
import org.bouncycastle.asn1.*;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * 第一版本
 */
public class b {

    /** RSA最大加密明文大小 */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static void main(String[] args) throws Exception {
        long st = System.currentTimeMillis();
        //生成密钥对
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        RSAKeyGenerationParameters rsaKeyGenerationParameters = new RSAKeyGenerationParameters(BigInteger.valueOf(3),
                new SecureRandom(), 1024, 25);
        rsaKeyPairGenerator.init(rsaKeyGenerationParameters);//初始化参数

        long t = System.currentTimeMillis();
        System.out.println("初始化参数 "+(t - st));

        AsymmetricCipherKeyPair keyPair = rsaKeyPairGenerator.generateKeyPair();

        long t1 = System.currentTimeMillis();
        System.out.println("generateKeyPair "+(t1 - st));

        AsymmetricKeyParameter publicKey = keyPair.getPublic();//公钥
        AsymmetricKeyParameter privateKey = keyPair.getPrivate();//私钥



        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey);
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo(privateKey);

        long t2 = System.currentTimeMillis();
        System.out.println("创建KEY "+(t2 - t1));

        //变字符串
        ASN1Object asn1ObjectPublic = subjectPublicKeyInfo.toASN1Primitive();
        byte[] publicInfoByte = asn1ObjectPublic.getEncoded();
        ASN1Object asn1ObjectPrivate = privateKeyInfo.toASN1Primitive();
        byte[] privateInfoByte = asn1ObjectPrivate.getEncoded();

        //这里可以将密钥对保存到本地
        final Base64.Encoder encoder64 = Base64.getEncoder();
        System.out.println("PublicKey:\n" + encoder64.encodeToString(publicInfoByte));
        System.out.println("PrivateKey:\n" + encoder64.encodeToString(privateInfoByte));

        long t3 = System.currentTimeMillis();
        System.out.println("这里可以将密钥对保存到本地 "+(t3 - t2));


        //加密、解密
        ASN1Object pubKeyObj = subjectPublicKeyInfo.toASN1Primitive();//这里也可以从流中读取，从本地导入
        AsymmetricKeyParameter pubKey = PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(pubKeyObj));
        AsymmetricBlockCipher cipher = new RSAEngine();
        cipher.init(true, pubKey);//true表示加密

        long t4 = System.currentTimeMillis();
        System.out.println("加密、解密 初始化 "+(t4 - t3));

        final Base64.Decoder decoder64 = Base64.getDecoder();
        //加密
        String data = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        System.out.println("\n明文：" + data);
        byte[] getBytes = data.getBytes("utf-8");
        //byte[] encryptData = cipher.processBlock(getBytes, 0, data.getBytes("utf-8").length);

        //System.out.println("密文:" + encoder64.encodeToString(encryptData));

        byte[] resEncryptData = doEncryptDataStream(data,cipher);
        System.out.println("分段加密："+new String(resEncryptData));

        long t5 = System.currentTimeMillis();
        System.out.println("加密后数据耗时 "+(t5 - t4));

        //解密
        /*AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoByte);
        cipher.init(false, priKey);//false表示解密
        byte[] decriyptData = cipher.processBlock(encryptData, 0, encryptData.length);
        String decryptData = new String(decriyptData, "utf-8");
        System.out.println("解密后数据：" + decryptData);

        long t6 = System.currentTimeMillis();
        System.out.println("解密后数据耗时 "+(t6 - t5));*/


        AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoByte);
        cipher.init(false, priKey);//false表示解密
        byte[] decriyptData = doDecriyptDataStream(resEncryptData,cipher);
        String decryptData = new String(decriyptData, "utf-8");
        System.out.println("解密后数据：" + decryptData);

        long t6 = System.currentTimeMillis();
        System.out.println("解密后数据耗时 "+(t6 - t5));

    }

    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * 截取byte数组   不改变原数组
     * @param b 原数组
     * @param off 偏差值（索引）
     * @param length 长度
     * @return 截取后的数组
     */
    public static byte[] subByte(byte[] b,int off,int length){
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
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



    public static byte[] doEncryptData(String data,AsymmetricBlockCipher cipher)
            throws UnsupportedEncodingException, InvalidCipherTextException {

        int tag = 0;
        byte[] res = null;
        for (int i = 0; i < data.length()/117+1 ; i++) {
            String temp = "";
            String tempdate = data.substring(tag,data.length());
            if(tempdate.length() >0 && tempdate.length() < 117){
                temp = data.substring(tag,data.length());
                res = new byte[temp.length()];
                
            }else{
                temp = data.substring(tag,tag+117);
                res = new byte[117];
            }

            byte[] b = temp.getBytes("UTF-8");

            byte[] encryptDatas = cipher.processBlock(b, 0, temp.getBytes("utf-8").length);
            res = byteMerger(encryptDatas,res);
            tag += 117;
        }

        return res;
    }




    public static byte[] doDecriyptData(byte[] data,AsymmetricBlockCipher cipher)
            throws UnsupportedEncodingException, InvalidCipherTextException {
        int tag = 0;
        byte[] res = new byte[200];
        byte[] decriyptData = null;
        for (int i = 0; i < data.length/117+1 ; i++) {
            byte[] temp = null;
            byte[] tempdate =subByte(data,0,117);
            if(tempdate.length < 117){
                temp = subByte(data,0,data.length);
                decriyptData = cipher.processBlock(temp, 0, data.length);
            }else{
                temp = subByte(data,0,117);
                decriyptData = cipher.processBlock(temp, 0, 117);
            }



            res = byteMerger(decriyptData,res);
            tag += 117;
        }

        return res;
    }





    public static final InputStream byte2Input(byte[] buf){
        return new ByteArrayInputStream(buf);
    }
    
    public static final byte[] input2byte(InputStream inputStream) throws IOException {
          ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
          byte[] buff = new byte[100];
          int rc = 0;
          while((rc = inputStream.read(buff, 0, 100)) > 0){
              swapStream.write(buff, 0, rc);
          }
          byte[] in2b = swapStream.toByteArray();
          return in2b;
    }


}

