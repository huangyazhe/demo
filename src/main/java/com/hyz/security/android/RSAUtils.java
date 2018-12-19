package com.hyz.security.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;

public class RSAUtils {

	public static final String PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMHENKUdY3JBHc8B\n" +
			"4mHNyBaBL2JUy1kTMvzaXBvtdPZJ5pSA3ZLGf2yqL0bG4+5rzjvDu+BSPTFrXMg/\n" +
			"dU+WFM+JqjFnxRPFcoCqeu9MHZoWuUTg9b/LEYXUW6J0zbPKbNSRCQIET2qUOON5\n" +
			"rfArmGHlj2CoMvesSvp+BhObaYLRAgMBAAECgYEAji+HHh6Zqe6kjBHq6DAUAoeb\n" +
			"mMF2Uo/nG2q0qn2uFUiXXiPN8/Wa7cdYV8x816jeNjbkd7CBXPFWrU77q4ILFBQS\n" +
			"PIWVc11bq22HFlWlwTCs7/g/UR0ekEgrlolZDyxTcWgmY8S2NALxr0uR/TwLya8f\n" +
			"5YpcCH8P7BEsLtKhv00CQQDxnAfdmRcxn/6WDopVtf/RkzS74L2PSgtVKaHIPnEp\n" +
			"KXMAXg9uyK7/uklNRUTt2YDDNsK81RBySSpWm6HMQv8vAkEAzU6unylYuUghsNgY\n" +
			"Tc54RK33oV4ZqKWDmzkOt5S0A08HoDO6jRVzR20f8JnJmXWqPcop1Cue43hh3dYt\n" +
			"JUsd/wJBALL6RsldMtVMFCfMtaUwoUT6q0HSBhozW5nGsVXJC8LWNZ68DuqeNySx\n" +
			"NsPK4HjheoUh97gyjXBbysVFnOHXb3kCQQClqo8HyaJZaBYfkFAUQL4VlVeTs836\n" +
			"owxObb0tb+XOIbBimjs3aw6pnSm/ySi/Fw53a7FTDpvYq6Q1EIU/aZzbAkEAq1mY\n" +
			"VJ6cv/ek8ZjUL/SxZeJUkQNdksHJlAEbn4/WvY2+qEHI8vO0hZDPkbtZeVRsQG2u\n" +
			"nICCnuQ4cOg0WE+etg==";
	private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBxDSlHWNyQR3PAeJhzcgWgS9i\n" +
			"VMtZEzL82lwb7XT2SeaUgN2Sxn9sqi9GxuPua847w7vgUj0xa1zIP3VPlhTPiaox\n" +
			"Z8UTxXKAqnrvTB2aFrlE4PW/yxGF1FuidM2zymzUkQkCBE9qlDjjea3wK5hh5Y9g\n" +
			"qDL3rEr6fgYTm2mC0QIDAQAB";

	/** RSA最大加密明文大小 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** RSA最大解密密文大小 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/** 加密算法RSA */
	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * 生成公钥和私钥
	 * 
	 * @throws Exception
	 * 
	 */
	public static void getKeys() throws Exception {
//		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//		keyPairGen.initialize(1024);
//		KeyPair keyPair = keyPairGen.generateKeyPair();
//		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//		String publicKeyStr = getPublicKeyStr(publicKey);
//		String privateKeyStr = getPrivateKeyStr(privateKey);
//
//		System.out.println("公钥\r\n" + publicKeyStr);
//		System.out.println("私钥\r\n" + privateKeyStr);

        for (int i=0;i<1;i++){
            System.out.println("----------");
            String hello = "123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha123456abcdefghihahaha";
		    System.out.println("原始数据："+hello);
            long t1= System.currentTimeMillis();
            String encryptResult = encryptByPublicKey(hello);
            long t2= System.currentTimeMillis();
            System.out.println("加密时间："+(t2-t1));
            System.out.println("加密结果："+encryptResult);

		//encryptResult = "CJs99UR/L/scBdx1l3e+5sm2NydD1zijfhGY/qB+30rNT1I6L52KPvrcyhmFT2v4TQNvAzPVYFP5UAqDmBmYvz8ieKb4ny7yzI8bAiEP7fjFoDp2zO5behP9MKUtdqu241v8Vf5NpOYnzE+5VsbYEVSCtxy/BW5GHzNRfoTGu2Q=";
            String decryptResult = decryptByPrivateKey(encryptResult);
            long t3= System.currentTimeMillis();
            System.out.println("解密时间："+(t3-t2));
            System.out.println("解密结果："+decryptResult);
        }



	}

	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            公钥指数
	 * @return
	 */
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
		*/
		public static String encryptByPublicKey(String data) throws Exception {
			byte[] dataByte = data.getBytes();
			byte[] keyBytes = Base64Utils.decode(PUBLIC_KEY);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = dataByte.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return Base64Utils.encode(encryptedData);
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data) throws Exception {
		byte[] encryptedData = Base64Utils.decode(data);
		byte[] keyBytes = Base64Utils.decode(PRIVATE_KEY);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");

		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return new String(decryptedData);
	}

	/**
	 * 获取模数和密钥
	 * 
	 * @return
	 */
	public static Map<String, String> getModulusAndKeys() {

		Map<String, String> map = new HashMap<String, String>();

		try {
			InputStream in = RSAUtils.class
					.getResourceAsStream("/rsa.properties");
			Properties prop = new Properties();
			prop.load(in);

			String modulus = prop.getProperty("modulus");
			String publicKey = prop.getProperty("publicKey");
			String privateKey = prop.getProperty("privateKey");

			in.close();

			map.put("modulus", modulus);
			map.put("publicKey", publicKey);
			map.put("privateKey", privateKey);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64Utils.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从字符串中加载私钥<br>
	 * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
	 * 
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(String privateKeyStr)
			throws Exception {
		try {
			byte[] buffer = Base64Utils.decode(privateKeyStr);
			// X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	public static String getPrivateKeyStr(PrivateKey privateKey)
			throws Exception {
		return new String(Base64Utils.encode(privateKey.getEncoded()));
	}

	public static String getPublicKeyStr(PublicKey publicKey) throws Exception {
		return new String(Base64Utils.encode(publicKey.getEncoded()));
	}

	public static void main(String[] args) throws Exception {
		getKeys();
	}


}