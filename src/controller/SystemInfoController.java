package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.PhysicalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;


public class SystemInfoController implements Initializable{

	SystemInfo systemInfo = new SystemInfo();
	Timer timer = new Timer();

	@FXML
	AnchorPane SystemInfoAP;

	@FXML
	Text ManufacturerTX;

	@FXML
	Text FamilyTX;

	@FXML
	Text MemoryTX;

	@FXML
	Text FileStoreTX;

	@FXML
	Text VersionTX;

	@FXML
	Text PCManufacturerTX;

	@FXML
	Text PCModelTX;

	@FXML
	Text PCSerialNumberTX;

	@FXML
	Text GraphicCardTX;

	@FXML
	Text ProcessorTX;

	@FXML
	TableView<OSProcess> processTBL;

	@FXML
	TableColumn<OSProcess, String> ProcessNameCLN = new TableColumn<>("Name");
	@FXML
	TableColumn<OSProcess, Integer> PriorityCLN = new TableColumn<>("Priority");
	@FXML
	TableColumn<OSProcess, String> ProcessUserCLN = new TableColumn<>("User");
	@FXML
	TableColumn<OSProcess, String> ProcessStateCLN = new TableColumn<>("State");
	@FXML
	TableColumn<OSProcess, Integer> ProcessPidCLN = new TableColumn<>("PID");

	@FXML
	Button KillProcessBTN;
	@FXML
	Button UpdateProcessBTN;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getSystemInfo();
		getProcesses();

		KillProcessBTN.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				try {
					killProcess();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		UpdateProcessBTN.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				updateProcess();
			}
		});
	}

	public void getProcesses(){

		List<OSProcess> serviceList = systemInfo.getOperatingSystem().getProcesses();
		ObservableList<OSProcess> observableList = FXCollections.observableArrayList();

		for(OSProcess osService : serviceList) {
			observableList.add(osService);
		}

		formatTableProcesses();

		processTBL.setItems(observableList);
	}

	public void formatTableProcesses(){
		ProcessNameCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getName()));

		PriorityCLN.setCellValueFactory(cellData -> 
		new  SimpleIntegerProperty(cellData.getValue().getPriority()).asObject());

		ProcessUserCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getUser()));

		ProcessStateCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(String.valueOf(cellData.getValue().getState())));

		ProcessPidCLN.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getProcessID()).asObject());
	}

	private void getSystemInfo() {
		ManufacturerTX.setText(systemInfo.getOperatingSystem().getManufacturer());
		FamilyTX.setText(systemInfo.getOperatingSystem().getFamily());
		VersionTX.setText(systemInfo.getOperatingSystem().getVersionInfo().toString());
		FileStoreTX.setText(formatOsFileStore());
		MemoryTX.setText(formatMemory());
		PCManufacturerTX.setText(systemInfo.getHardware().getComputerSystem().getManufacturer());
		PCModelTX.setText(systemInfo.getHardware().getComputerSystem().getModel());
		PCSerialNumberTX.setText(systemInfo.getHardware().getComputerSystem().getSerialNumber());
		GraphicCardTX.setText(formatGraphicCard()); 
		ProcessorTX.setText(systemInfo.getHardware().getProcessor().toString());
	}

	public String formatOsFileStore() {
		List<OSFileStore> osFileStores = systemInfo.getOperatingSystem().getFileSystem().getFileStores();
		String osFileStr = "";

		for(OSFileStore osFileStore : osFileStores) {
			osFileStr += osFileStore.getName() +
					"  Type: " + osFileStore.getType() +
					"  Total Space: " + osFileStore.getTotalSpace() +
					"  Usable Space: " + osFileStore.getUsableSpace() +
					"\n";
		}
		return osFileStr;
	}

	public String formatMemory() {
		List<PhysicalMemory> memory = systemInfo.getHardware().getMemory().getPhysicalMemory();
		String memoryStr = "";

		for(PhysicalMemory pshMemory : memory) {
			memoryStr += pshMemory.toString() + "\n";
		}
		return memoryStr;
	}

	public String formatGraphicCard() {
		List<GraphicsCard> graphicsCards = systemInfo.getHardware().getGraphicsCards();
		String graphicCardStr= "";
		for( GraphicsCard graphicCard: graphicsCards) {
			graphicCardStr += graphicCard.getName() + 
					"  Vendor: " + graphicCard.getVendor() +
					"  VRam: " + graphicCard.getVRam() +
					"   " + graphicCard.getVersionInfo() + 
					"\n";
		}

		return graphicCardStr;
	}

	void killProcess() throws IOException {
		if (processTBL.getSelectionModel().getSelectedItem().getProcessID() != -1) {
			int PID = processTBL.getSelectionModel().getSelectedItem().getProcessID();
			String cmd = "taskkill /F /PID " + PID;
			Runtime.getRuntime().exec(cmd);
		};
		
		getProcesses();
	}
	
	void updateProcess() {
		getProcesses();
	}
}
