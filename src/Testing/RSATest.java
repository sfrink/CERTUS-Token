package Testing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import application.BinFile;
import application.DataEncryptor;
import application.RSAKeys;
import application.RSASign;

public class RSATest {

	@Test
	public void test() {
		
		RSAKeys rsaKeysTester = new RSAKeys();
		RSASign rsaSignTester = new RSASign();
		RSAVer rsaVerTester = new RSAVer();
		
		String pubKeyPath = "f:\\temp\\pubKey";
		String pvkKeyPath = "f:\\temp\\pvkKey";
		String password = "my password";
		
		BinFile pvkKeyFile = new BinFile(pvkKeyPath);
		
		rsaKeysTester.generateKeys(pubKeyPath, pvkKeyPath, password);
		
		String textToBeSigned = "This tutorial explains unit testing with JUnit 4.x.";
		byte[] bytesToBeSigned = textToBeSigned.getBytes();
		
		//read the private key:
		byte [] encodedPVK = null;
		byte [] encryptedPVK = null;
		try {
			encryptedPVK = pvkKeyFile.readFile();
			try {
				encodedPVK = DataEncryptor.AESDecrypt(encryptedPVK, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//sign the text bytes:
		byte [] signature = null;
		signature = rsaSignTester.Sign(bytesToBeSigned, encodedPVK);
		
		//read the public key:
		BinFile pubKeyFile = new BinFile(pubKeyPath);
		byte[] encodedPublicKey = null;
		try {
			encodedPublicKey = pubKeyFile.readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//verify the signature:		
		boolean verified = rsaVerTester.Ver(bytesToBeSigned, encodedPublicKey, signature);
		if (verified){System.out.println("Signature verified.");}
		
		
		assertTrue(verified);
	}

}
