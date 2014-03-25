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
	
	@FXML private RadioButton rdnUseOwnPVK;
	@FXML private Label lblOwnPVKPath;
	@FXML private TextField txtOwnPVKPath;
	
	@FXML private RadioButton rdnUseCertusPVK;
	@FXML private Label lblCertusPVKPath;
	@FXML private TextField txtCertusPVKPath;
	@FXML private Label lblPass;
	@FXML private PasswordField txtPass;
	
	@FXML private TextField txtVote;
	@FXML private TextField txtSignature;
	
	@FXML private Button btnGenerate;

	@FXML private Label lblError;
	
	@FXML private CheckBox ckxGenerate;
	
	@FXML private Label lblNewKeyPairPath;
	@FXML private TextField txtNewKeyPairPath;
	
	@FXML private Button btnGenerateKeys;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		lblCertusPVKPath.setDisable(true);
		txtCertusPVKPath.setDisable(true);
		lblPass.setDisable(true);
		txtPass.setDisable(true);
		lblOwnPVKPath.setDisable(false);
		txtOwnPVKPath.setDisable(false);
		
		lblError.setVisible(false);
		
		txtOwnPVKPath.requestFocus();
		
		showKeyGeneratingComponents(false);
	}
	
	@FXML 
	private void rdnUseOwnPVKOnClick(ActionEvent event){
		lblCertusPVKPath.setDisable(true);
		txtCertusPVKPath.setDisable(true);
		lblPass.setDisable(true);
		txtPass.setDisable(true);
		
		lblOwnPVKPath.setDisable(false);
		txtOwnPVKPath.setDisable(false);
		
		txtOwnPVKPath.requestFocus();
		
	}
	
	@FXML
	private void rdnCertusPVKOnClick(ActionEvent event){
		lblCertusPVKPath.setDisable(false);
		txtCertusPVKPath.setDisable(false);
		lblPass.setDisable(false);
		txtPass.setDisable(false);
		
		lblOwnPVKPath.setDisable(true);
		txtOwnPVKPath.setDisable(true);
		
		txtCertusPVKPath.requestFocus();
	}
	
	private void showErrorMSG(String msg){
		lblError.setVisible(true);
		lblError.setText(msg);
	}
	
	@FXML
	private void btnGenerateOnClick(ActionEvent event) {
		lblError.setVisible(false);
		
		//check the input:
		if (rdnUseOwnPVK.isSelected()){
			if (txtOwnPVKPath.getText().equals("")){
				showErrorMSG("Private key path is empty.");
				txtOwnPVKPath.requestFocus();
				return;
			}
		}else if (rdnUseCertusPVK.isSelected()){
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
			if (txtPass.getText().length() != 16){
				showErrorMSG("Decryption password should be 16 characters.");
				txtPass.requestFocus();
				return;
			}
		}
		
		if (txtVote.getText().equals("")){
			showErrorMSG("Your encrypted vote is empty.");
			txtVote.requestFocus();
			return;
		}
		
		
		//check the needed files path:
		BinFile pvkFile = null;
		BinFile encPvkFile = null;
		if (rdnUseOwnPVK.isSelected()){
			pvkFile = new BinFile(txtOwnPVKPath.getText());
			if (!pvkFile.isFound()){
				showErrorMSG("Private key is not found, please check the path.");
				txtOwnPVKPath.requestFocus();
				return;
			}
		}else if (rdnUseCertusPVK.isSelected()){
			encPvkFile = new BinFile(txtCertusPVKPath.getText());
			if (!encPvkFile.isFound()){
				showErrorMSG("Encrypted private key is not found, please check the path.");
				txtCertusPVKPath.requestFocus();
				return;
			}
		}

		
		//load the private key:
		byte[] encodedPVK = null;
		
		//load the private key directly if not encrypted:
		if (rdnUseOwnPVK.isSelected()){
			
			try {
				encodedPVK = pvkFile.readFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		
		//if encrypted private key is selected, we have to decrypt it:
		if (rdnUseCertusPVK.isSelected()){
			try {
				byte[] encryptedPVK = encPvkFile.readFile();
				try {
					encodedPVK = DataEncryptor.AESDecrypt(encryptedPVK, txtPass.getText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//Sign the encrypted vote (Token Side):
		
		RSASign rsaSign = new RSASign();
		
		String encryptedVote = txtVote.getText(); 
//		byte [] encryptedVoteBytes = DatatypeConverter.parseHexBinary(encryptedVote);
		byte [] encryptedVoteBytes = Converter.HexToByte(encryptedVote);
		byte [] signature = null;
		signature = rsaSign.Sign(encryptedVoteBytes, encodedPVK);
		
		String hexSignature = Converter.ByteToHex(signature);
		txtSignature.setText(hexSignature);
		
		showErrorMSG("Signature is generated successfully.");
	}

	
	@FXML
	private void btnGenerateKeysOnClick(ActionEvent event){
		lblError.setVisible(false);
		
		String newKeyPairPath = txtNewKeyPairPath.getText();
		
		
		
		//input validation:
		if (newKeyPairPath.equals("")){
			showErrorMSG("New key pair directory path is empty.");
			txtNewKeyPairPath.requestFocus();
			return;
		}

		File keyPairFile = new File(newKeyPairPath);
		if(!keyPairFile.exists() && !keyPairFile.isDirectory()) {
			showErrorMSG("New key pair directory path is not valid, please specify a valid path.");
			txtNewKeyPairPath.requestFocus();
			return;
		}
		
		//Key Generation
		RSAKeys rsaKeys = new RSAKeys();
		rsaKeys.generateKeys(newKeyPairPath+"CertusPubKey", newKeyPairPath+"CertusPvkKey");
	
		showErrorMSG("Key pair generated successfully.");
	}
	
	@FXML
	private void ckxGenerateOnClick(ActionEvent event){
		showKeyGeneratingComponents(ckxGenerate.isSelected());
	}
	
	private void showKeyGeneratingComponents(boolean yes){
		lblNewKeyPairPath.setDisable(!yes);
		txtNewKeyPairPath.setDisable(!yes);

		btnGenerateKeys.setDisable(!yes);
		if (yes){
			txtNewKeyPairPath.requestFocus();
		}
	}
	
}
