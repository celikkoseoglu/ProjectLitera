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
    private byte[] encryptionKey;

    /**
     * @param s String to be encoded
     * @return encoded String
     * @description encrypts a String using AES
     */
    private EncryptionManager( String keyStr)
    {
        encryptionKey = keyStr.getBytes();
    }
     
    public void setKeyFromString( String keyStr)
    {
        encryptionKey = keyStr.getBytes();
    }
    public String encryptString(String s)
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
    public String decryptString(String s)
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
