package litera.Data;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @description the Encryption Manager class for Litera. Manages local data & encryption
 * @info all functions are made as efficient as possible. prove otherwise, and your code will replace mine :) (please do it)
 * @devloper Çelik Köseoğlu
 * @revision 1
 */

public class EncryptionManager
{
    private static byte[] encryptionKey = {0x76, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79};

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
            final SecretKeySpec secretKey = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(s.getBytes()));
            return encryptedString;
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
            final SecretKeySpec secretKey = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(s)));
            return decryptedString;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }
}
