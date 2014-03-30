/**
 * @author Ahmad Kouraiem
 */
package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ResourceBundle;

import javax.xml.bind.DatatypeConverter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class MainController implements Initializable {
	
	
	
	@FXML private TextField txtCertusPVKPath;
	
	@FXML private PasswordField txtPass;
	
	@FXML private TextField txtVote;
	@FXML private TextField txtSignature;
	
	@FXML private Button btnGenerate;

	@FXML private Label lblError;
	
	@FXML private TextField txtNewKeysPath;
	@FXML private TextField txtNewPass;
	
	@FXML private Label lblNewError;
	
	@FXML private Button btnGenerateKeys;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		lblError.setVisible(false);
		lblNewError.setVisible(false);
		txtCertusPVKPath.requestFocus();
	}
		
	private void showErrorMSG(String msg){
		lblError.setVisible(true);
		lblError.setText(msg);
	}
	
	private void showAdvancedErrorMSG(String msg){
		lblNewError.setVisible(true);
		lblNewError.setText(msg);
	}
	
	@FXML
	private void btnGenerateOnClick(ActionEvent event) {
		showErrorMSG("");
		
		//check empty fields:
		if (txtCertusPVKPath.getText().equals("")){
			showErrorMSG("Certus encrypted private key path is empty.");
			txtCertusPVKPath.requestFocus();
			return;
		}
		if (txtPass.getText().equals("")){
			showErrorMSG("Decryption password is empty.");
			txtPass.requestFocus();
			return;
		}		
		if (txtVote.getText().equals("")){
			showErrorMSG("Your encrypted vote is empty.");
			txtVote.requestFocus();
			return;
		}
		
		
		//check the needed files path:
		BinFile encPvkFile = null;
		
		encPvkFile = new BinFile(txtCertusPVKPath.getText());
		if (!encPvkFile.isFound()){
			showErrorMSG("Encrypted private key is not found, please check the path.");
			txtCertusPVKPath.requestFocus();
			return;
		}
		
		//load the private key:
		byte[] encodedPVK = null;
		
		//Decrypt the private key:
		try {
			byte[] encryptedPVK = encPvkFile.readFile();
			try {
				encodedPVK = DataEncryptor.AESDecrypt(encryptedPVK, txtPass.getText());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showErrorMSG("Decrypting private code faild.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			showErrorMSG("Decrypting private code faild.");
		}
		
		
		//Sign the encrypted vote:
		
		RSASign rsaSign = new RSASign();
		
		String encryptedVote = txtVote.getText(); 
		byte [] encryptedVoteBytes = Converter.HexToByte(encryptedVote);
		byte [] signature = null;
		signature = rsaSign.Sign(encryptedVoteBytes, encodedPVK);
		
		String hexSignature = Converter.ByteToHex(signature);
		txtSignature.setText(hexSignature);
		
		showErrorMSG("Signature is generated successfully.");
	}

	
	@FXML
	private void btnGenerateKeysOnClick(ActionEvent event){
		lblNewError.setVisible(false);
		
		String newKeyPairPath = txtNewKeysPath.getText();
		
		
		//input validation:
		if (newKeyPairPath.equals("")){
			showAdvancedErrorMSG("New key pair directory path is empty.");
			txtNewKeysPath.requestFocus();
			return;
		}

		String pass = txtNewPass.getText();
		if (pass.equals("")){
			showAdvancedErrorMSG("Protecting password is empty.");
			txtNewPass.requestFocus();
			return;
		}
		
		File keyPairFile = new File(newKeyPairPath);
		if(!keyPairFile.exists() && !keyPairFile.isDirectory()) {
			showAdvancedErrorMSG("New key pair directory path is not valid, please specify a valid path.");
			txtNewKeysPath.requestFocus();
			return;
		}
		
		//Key Generation
		
		RSAKeys rsaKeys = new RSAKeys();
		rsaKeys.generateKeys(newKeyPairPath+"CertusPubKey", newKeyPairPath+"CertusEncPvkKey", pass);
	
		showAdvancedErrorMSG("Key pair generated successfully.");
	}
	
		
}
