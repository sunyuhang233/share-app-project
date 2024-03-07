package top.ssy.share.app.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2023-03-07 18:04
 **/
public class AESUtil {

    private static final String SECRET = "session_key";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String AES_NAME = "AES";

    /**
     * 解密
     * AES解密
     *
     * @param encryptedData 加密数据
     * @param sessionKey    会话密钥
     * @param vi            6
     * @return {@link String}
     */
    public static String decrypt(String encryptedData, String sessionKey, String vi) {
        byte[] encData = Base64.decodeBase64(encryptedData);
        byte[] iv = Base64.decodeBase64(vi);
        byte[] key = Base64.decodeBase64(sessionKey);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key, AES_NAME);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return new String(cipher.doFinal(encData), CHARSET_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}


