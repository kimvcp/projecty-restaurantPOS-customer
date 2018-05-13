package controller;

import application.CSTable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import util.ScreenController;

/**
 * StartUpController contains method for handling all event receive from the
 * UserInterface.
 * 
 * @author Piyawat & Vichaphol
 *
 */
public class StartUpController {
	@FXML
	private Button login;
	@FXML
	private Button signUp;
	@FXML
	private Button csMode;

	/**
	 * Method for handling Customer mode button. When event received, CustomerTable scene is
	 * shown.
	 * 
	 * @param event
	 */
	public void csModeButtonHandler(MouseEvent event) {
		ScreenController.switchWindow((Stage) csMode.getScene().getWindow(), new CSTable());
	}
}
