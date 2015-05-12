package litera.Data;

import litera.Defaults.Defaults;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * @author Çelik Köseoğlu
 * @version 2
 * @the Encryption Manager class for Litera. Manages local data & encryption
 */

public class EncryptionManager
{
    private static byte[] userEncryptionKey = Defaults.privateEncryptionKey;

    /**
     * @param s String to be encoded
     * @return encoded String
     * @description encrypts a String using AES
     */
    public static String encryptString(String s)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(userEncryptionKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeBase64String(cipher.doFinal(s.getBytes()));
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param s String to be decoded
     * @return decoded string
     * @description decrypts a String using AES
     */
    public static String decryptString(String s)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(userEncryptionKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(s)));
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getUserKey()
    {
        return userEncryptionKey;
    }

    /**
     * IMPLEMENTING AS SOON AS POSSIBLE...
     * PASSWORD PROTECTION IS COMING!
     *
     * @param s
     */
    public static void setUserKey(String s)
    {
        System.out.println("OldKey: " + Arrays.toString(userEncryptionKey));
        userEncryptionKey = org.apache.commons.codec.digest.DigestUtils.md5(s);
        System.out.println("NewKey: " + Arrays.toString(userEncryptionKey));
    }

    public static void resetKey()
    {
        userEncryptionKey = Defaults.privateEncryptionKey;
    }
}