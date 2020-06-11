package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class MainController implements Initializable{

	SystemInfoController systemInfoController;
	ServiceController serviceController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		systemInfoController.initialize(location, resources);
		serviceController.initialize(location, resources);
	}

}
