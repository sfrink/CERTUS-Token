/**
 * @author Ahmad Kouraiem
 */
package application;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;

public class DataEncryptor {
	public static String IV = "ABAFACAFAA5ABBAA";
	
	public static byte[] AESEncrypt(byte[] plainText, String encryptionKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes()));
		return cipher.doFinal(plainText);
	}

	public static byte[] AESDecrypt(byte[] cipherText, String encryptionKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(), "AES");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes()));
		return cipher.doFinal(cipherText);
	}

	
}
