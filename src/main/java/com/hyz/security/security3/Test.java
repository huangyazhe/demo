package com.hyz.security.security3;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * HI 雅哲，别忘记写注释哦，认真是一种态度
 * @author HuangYazhe
 * Date: 2018/12/13
 */
public class Test {

    public static void main(String[] args) {
        try {


            //psw：加密的文本，pswkey：加密的密码，即密钥
            String psw="12345",pswkey="999";

            //对该DES情况下加密，密钥需要为56位难以人工由string为根据设定，因此这里：
            //借助密钥生成器，由new SecureRandom(pswkey.getBytes())，以pswkey为种子值生成密钥
            KeyGenerator keyGenerator=KeyGenerator.getInstance("DES");
            keyGenerator.init(56,new SecureRandom(pswkey.getBytes()));
            Key key=keyGenerator.generateKey();

            //加密，DES/ECB/pkcs5padding为：算法/模式/填充 的参数
            //参数new BouncyCastleProvider()也可修改为"BC"，要求前面要有Security.addProvider(new BouncyCastleProvider());
            Cipher cipher= null;

            cipher = Cipher.getInstance("DES/ECB/pkcs5padding",new BouncyCastleProvider());

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] out=cipher.doFinal(psw.getBytes());
            System.out.println(out);//字节数组，没有编码

            //解密
            Cipher cipher2=Cipher.getInstance("DES/ECB/pkcs5padding",new BouncyCastleProvider());
            cipher2.init(Cipher.DECRYPT_MODE, key);
            byte[] in=cipher2.doFinal(out);
            System.out.println(new String(in));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
