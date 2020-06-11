package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import oshi.SystemInfo;

public class TaskManagerFXMLController implements Initializable{

	@FXML
	Text osManufacturerTX;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SystemInfo systemInfo = new SystemInfo();
		osManufacturerTX.setText(systemInfo.getOperatingSystem().getManufacturer());
	}
}
