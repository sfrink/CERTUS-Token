/**
 * @author Ahmad Kouraiem
 */
package application;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAKeys {

	public void generateKeys(String pubKeyPath, String pvkKeypath){
		
		try {			
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(3072);
			KeyPair keyPair = gen.generateKeyPair();
			PublicKey pubKey = keyPair.getPublic();
			PrivateKey pvkKey = keyPair.getPrivate();
			
			writeFile(pubKeyPath, pubKey.getEncoded());
			writeFile(pvkKeypath, pvkKey.getEncoded());
			
			System.out.println("RSA key pair are generated.");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("RSA key pair generation failed");
		}

		
		
		return;
	}
	
	private static void writeFile (String path, byte [] content) throws IOException{
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
		bos.write(content);
		bos.flush();
		bos.close();
	}	
	
	
}
