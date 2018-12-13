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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by Administrator on 2018/8/1 0001.
 */
public class b {

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
        String data = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";

        System.out.println("\n明文：" + data);
        byte[] getBytes = data.getBytes("utf-8");
        byte[] encryptData = cipher.processBlock(getBytes, 0, data.getBytes("utf-8").length);

        System.out.println("密文:" + encoder64.encodeToString(encryptData));

        long t5 = System.currentTimeMillis();
        System.out.println("加密后数据耗时 "+(t5 - t4));

        //解密
        AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoByte);
        cipher.init(false, priKey);//false表示解密
        byte[] decriyptData = cipher.processBlock(encryptData, 0, encryptData.length);
        String decryptData = new String(decriyptData, "utf-8");
        System.out.println("解密后数据：" + decryptData);

        long t6 = System.currentTimeMillis();
        System.out.println("解密后数据耗时 "+(t6 - t5));



        int tag = 0;

        byte[] res = new byte[200];
        for (int i = 1; i < data.length()/5 ; i++) {

            String temp = data.substring(tag,tag+5);

            byte[] b = temp.getBytes("UTF-8");
            byte[] encryptDatas = cipher.processBlock(getBytes, 0, data.getBytes("utf-8").length);
            res = byteMerger(encryptDatas,res);
            tag += 5;
        }

        System.out.println(res);


        String decryptData1 = "";
        int tag1 = 0;

        byte[] res1 = new byte[200];
        for (int i = 1; i < res.length/5 ; i++) {

            //byte[] b = res[tag],res[tag+5];


            AsymmetricKeyParameter priKey1 = PrivateKeyFactory.createKey(privateInfoByte);
            cipher.init(false, priKey1);//false表示解密
            byte[] decriyptDataTemp = cipher.processBlock(encryptData, 0, encryptData.length);


            res1 = byteMerger(decriyptDataTemp,res);
            tag += 5;
        }

        //解密
        decryptData1 = new String(res1, "utf-8");
        System.out.println("解密后数据：" + decryptData1);
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
    public byte[] subByte(byte[] b,int off,int length){
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }
}