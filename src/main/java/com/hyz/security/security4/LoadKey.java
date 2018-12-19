package com.hyz.security.security4;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.PEMUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * HI 雅哲，别忘记写注释哦，认真是一种态度
 * @author HuangYazhe
 * Date: 2018/12/18
 */
public class LoadKey {

    public static PublicKey loadPublicKey() throws Exception {
        //String publicKeyPEM = FileUtils.readFileToString(new File("C:/Users/Administrator/Desktop/RSA/my_pub.pem"), "utf-8");
        String publicKeyPEM = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC18UP+qNFQ177MfA0GYgdEu3+n\n"
                + "sVKJLjq9odqyZtPLFZl6Qoih29XKaeY92G1UMSD4/6zJG1+/Uzh2bmDJYDRinhNc\n"
                + "XFaIdcOZ4Krlz4PDluTyP4IPDmtYSfBx8BgROEnZar441qkg0xK01oU9xLBaYSVJ\n" + "CEZNfGZVEjig7vzehQIDAQAB";
        // strip of header, footer, newlines, whitespaces
        /*publicKeyPEM = publicKeyPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");*/

        // decode to get the binary DER representation
        byte[] publicKeyDER = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDER));
        return publicKey;
    }



    public static void main(String[] args) throws Exception {

    }
}
