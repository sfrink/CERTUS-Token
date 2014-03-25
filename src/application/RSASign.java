/**
 * @author Ahmad Kouraiem
 */
package application;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSASign {
		
	private PrivateKey getPrivateKey (byte[] encodedPrivateKey){
		try {
	        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
	        KeyFactory generator = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = generator.generatePrivate(privateKeySpec);

	        return privateKey;
	    } catch (Exception e) {
	        throw new IllegalArgumentException("Failed to create key from provided encoded key", e);
	    }
	}

	public byte[] Sign (byte[] encryptedVote, byte[] encodedPrivateKey){
		try {			
			Signature sign = Signature.getInstance("SHA256WITHRSA");
			PrivateKey prvtKey = getPrivateKey(encodedPrivateKey);
			
			sign.initSign(prvtKey);
			sign.update(encryptedVote);   
			
			System.out.println("Message has been signed.");

			return sign.sign();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("RSA signature test failed");
			return null;
		}
	}
	
	
	
}
