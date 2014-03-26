/**
 * @author Ahmad Kouraiem
 */
package Testing;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAVer {
	
	
	private PublicKey getPublicKey (byte[] encodedPublicKey){
		try {
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
	        KeyFactory generator = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = generator.generatePublic(publicKeySpec);

	        return publicKey;
	    } catch (Exception e) {
	        throw new IllegalArgumentException("Failed to create key from provided encoded key", e);
	    }
	}

	public boolean Ver (byte[] encryptedVote, byte[] encodedPublicKey, byte[] signatureBytes){
		try {			
			  
			Signature ver = Signature.getInstance("SHA256WITHRSA");
			PublicKey pubKey = getPublicKey(encodedPublicKey);
			ver.initVerify(pubKey);
			ver.update(encryptedVote);  
			boolean goodSig = ver.verify(signatureBytes);
			return goodSig;	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("RSA signature test failed");
			return false;
		}
	}


}
