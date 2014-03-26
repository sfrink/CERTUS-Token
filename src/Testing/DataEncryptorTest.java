package Testing;

import static org.junit.Assert.*;

import org.junit.Test;

import application.DataEncryptor;

public class DataEncryptorTest {

	@Test
	public void test() {
		
		DataEncryptor tester = new DataEncryptor();
		
		String Key1 = "0123456789abcdef";
		String Key2 = "1234567890abcdef";
		String plainText = "Encrypt me if you can.";
		byte [] cipherBytes = null;
		String decryptedCipher1 = "";
		String decryptedCipher2 = "";
		
		try {
			cipherBytes = tester.AESEncrypt(plainText.getBytes(), Key1);
			decryptedCipher1 = new String(tester.AESDecrypt(cipherBytes, Key1));
			decryptedCipher2 = new String(tester.AESDecrypt(cipherBytes, Key2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		assertEquals(plainText, decryptedCipher1);
		
		boolean diff = false;
		if (!decryptedCipher1.equals(decryptedCipher2)){
			diff = true;
		}
		
		assertTrue(diff);
	}

}
