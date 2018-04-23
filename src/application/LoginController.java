package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
@FXML
private Button login;
@FXML
private Button signup;
@FXML
private TextField usernameField;
@FXML
private TextField passwordField;

public void handleLogin(ActionEvent event) {
	String username = usernameField.getText();
}

}
