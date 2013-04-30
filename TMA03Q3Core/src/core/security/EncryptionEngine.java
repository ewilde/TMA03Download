/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.security;

import org.apache.commons.codec.binary.Base64;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 *
 * @author Edward Wilde
 */
public class EncryptionEngine
{
    private EncryptionAlgorithm algorithm;

    private static final String salt = "5>R/s[vG";
    public EncryptionEngine(EncryptionAlgorithm algorithm)
    {
        this.algorithm = algorithm;

    }

    public String Encrypt(String key, String value) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException
    {
        this.EnsureSupportAlgorithm();

        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Iteration count
        int count = 20;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt.getBytes(), count);

        pbeKeySpec = new PBEKeySpec(key.toCharArray());

        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        String ciphertext = Base64.encodeBase64String(pbeCipher.doFinal(value.getBytes()));

        return ciphertext;
    }

    public String Decrypt(String key, String value)
             throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException
    {
        this.EnsureSupportAlgorithm();
        
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Iteration count
        int count = 20;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt.getBytes(), count);

         pbeKeySpec = new PBEKeySpec(key.toCharArray());

         keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        String ciphertext = new String(pbeCipher.doFinal(Base64.decodeBase64(value)));
        return ciphertext;
    }
    /**
     * @return the algorithm
     */
    public EncryptionAlgorithm getAlgorithm()
    {
        return algorithm;
    }

    /**
     * @param algorithm the algorithm to set
     */
    public void setAlgorithm(EncryptionAlgorithm algorithm)
    {
        this.algorithm = algorithm;
    }

    private void EnsureSupportAlgorithm()
    {
        if (this.getAlgorithm() != EncryptionAlgorithm.PBE)
        {
            throw new IllegalArgumentException("PBE is currently the only support algoritm.");
        }
    }

}
