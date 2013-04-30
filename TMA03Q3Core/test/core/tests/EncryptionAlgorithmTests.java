package core.tests;

import core.security.*;
import core.protocol.*;
import core.protocol.exceptions.*;
import org.junit.runners.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Edward Wilde
 */
public class EncryptionAlgorithmTests
{
    private static final String Key = "password";
    private static final String HelloWorldTextToEncrypt = "Hello World!";
    private static final String HelloWorldEncryptedByBlowfish = "RCHbyRtaDfIrTiGy3LGUdQ=="; // Taken from http://www.tools4noobs.com/online_tools/encrypt/
    
    @Test
    public void CanEncryptAndDecryptTextUsingDESPBE() throws Exception
    {
        EncryptionEngine engine = new EncryptionEngine(EncryptionAlgorithm.PBE);

        String encryptedtext = engine.Encrypt(Key, HelloWorldTextToEncrypt);
        String clearText = engine.Decrypt(Key, encryptedtext);

        assertThat(clearText, is(HelloWorldTextToEncrypt));
    }
}
