package com.hyz.security.security4;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import static java.lang.String.format;
public class Encryptor {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey privateKey = readPrivateKey();
        String message = "YtSepKneBHUOhokr4YDnFxQq7jwt54THiyuzCnN0jc9KOwOUAa8eZNHzXxxs9ga9lyj4Ag6vRrg3Aa8oFMFz38G4k5vyCetSZ5ISXnYmi4goZ8BOHKbgj6Am6d2IRDZuNQ/5BJ3M3uV+FkE6bxnzFKWf5qanyKeL14Id/c=";
        System.out.println(format("- decrypt rsa encrypted base64 message: %s", message));
        // hello ~,  encrypted and encoded with Base64:
        byte[] data = message.getBytes("utf-8");
        String text = decrypt(privateKey, data);
        System.out.println(text);
    }
    private static String decrypt(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(data);
        return new String(decryptedData);
    }
    private static byte[] encryptedData(String base64Text) {
        return Base64.getDecoder().decode(base64Text.getBytes(Charset.forName("UTF-8")));
    }
    private static PrivateKey readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyData = Files.readAllBytes(
                Paths.get("C:\\Users\\Administrator\\Documents\\Tencent Files\\270741749\\FileRecv\\pkcs8_private_key.pem"));
        byte[] decodedKeyData = Base64.getDecoder()
                .decode(new String(privateKeyData)
                        .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                        .replace("\n", "")
                        .getBytes());
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedKeyData));
    }
}

