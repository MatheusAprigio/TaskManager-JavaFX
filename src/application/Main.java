package application;
	
import java.io.IOException;
import java.util.List;

import controller.TaskManagerFXMLController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.common.AbstractGraphicsCard;
import oshi.hardware.platform.windows.WindowsHWDiskStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OSSession;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
			Pane root = FXMLLoader.load(getClass().getResource("TaskManagerFXML.fxml")); 
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
		
			primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
