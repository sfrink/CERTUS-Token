/**
 * @author Ahmad Kouraiem
 */
package application;
	
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	private Stage stage;
	@Override
	public void start(Stage primaryStage) throws IOException {

		stage = primaryStage;

		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

		Scene scene = new Scene(root);
		stage.setTitle("Certus Token");
                stage.setResizable(false);
                stage.centerOnScreen();
                
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	
	public static String toHex (byte [] hashValue){
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashValue.length; i++) {
          sb.append(Integer.toString((hashValue[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	
	
	public static void main(String[] args) {		
		launch(args);
	}
}
