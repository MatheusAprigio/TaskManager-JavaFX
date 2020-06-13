package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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
import javafx.scene.text.Text;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.PhysicalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OSService;


public class SystemInfoController implements Initializable{

	SystemInfo systemInfo = new SystemInfo();

	@FXML
	Text manufacturerTX;

	@FXML
	Text familyTX;

	@FXML
	Text memoryTX;

	@FXML
	Text fileStoreTX;

	@FXML
	Text versionTX;

	@FXML
	Text pcManufacturerTX;

	@FXML
	Text pcModelTX;

	@FXML
	Text pcSerialNumberTX;

	@FXML
	Text graphicCardTX;

	@FXML
	Text processorTX;

	@FXML
	TableView<OSProcess> processTBL;
	@FXML
	TableView<OSService> serviceTBL;
	
	
	@FXML
	TableColumn<OSProcess, String> processNameCLN = new TableColumn<>("Name");
	@FXML
	TableColumn<OSProcess, Integer> priorityCLN = new TableColumn<>("Priority");
	@FXML
	TableColumn<OSProcess, String> processUserCLN = new TableColumn<>("User");
	@FXML
	TableColumn<OSProcess, String> processStateCLN = new TableColumn<>("State");
	@FXML
	TableColumn<OSProcess, Integer> processPidCLN = new TableColumn<>("PID");
	
	@FXML
	TableColumn<OSService, String> serviceNameCLN = new TableColumn<>("Name");
	@FXML
	TableColumn<OSService, String> serviceStateCLN = new TableColumn<>("State");
	@FXML
	TableColumn<OSService, String> serviceIdCLN = new TableColumn<>("PID");
	
	@FXML
	Button killProcessBTN;
	@FXML
	Button updateProcessBTN;
	@FXML
	Button updateServiceBTN;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getSystemInfo();
		getProcesses();
		getServices();
		
		killProcessBTN.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				try {
					killProcess();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		updateProcessBTN.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				updateProcess();
			}
		});
		
		updateServiceBTN.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				updateService();
			}
		});
	}

	public void getProcesses(){

		List<OSProcess> serviceList = systemInfo.getOperatingSystem().getProcesses();
		ObservableList<OSProcess> observableList = FXCollections.observableArrayList();

		for(OSProcess osProcess : serviceList) {
			observableList.add(osProcess);
		}

		formatTableProcesses();

		processTBL.setItems(observableList);
	}
	
	public void getServices(){

		List<OSService> serviceList = Arrays.asList(systemInfo.getOperatingSystem().getServices());
		ObservableList<OSService> observableList = FXCollections.observableArrayList();

		for(OSService osService : serviceList) {
			observableList.add(osService);
		}

		formatTableServices();

		serviceTBL.setItems(observableList);
	}
	
	public void formatTableProcesses(){
		processNameCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getName()));

		priorityCLN.setCellValueFactory(cellData -> 
		new  SimpleIntegerProperty(cellData.getValue().getPriority()).asObject());

		processUserCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getUser()));

		processStateCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(String.valueOf(cellData.getValue().getState())));

		processPidCLN.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getProcessID()).asObject());
	}

	public void formatTableServices(){
		serviceNameCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getName()));
		
		serviceStateCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(String.valueOf(cellData.getValue().getState())));
		
		serviceIdCLN.setCellValueFactory(cellData -> 
		new SimpleStringProperty(String.valueOf(cellData.getValue().getProcessID())));

	}
	private void getSystemInfo() {
		manufacturerTX.setText(systemInfo.getOperatingSystem().getManufacturer());
		familyTX.setText(systemInfo.getOperatingSystem().getFamily());
		versionTX.setText(systemInfo.getOperatingSystem().getVersionInfo().toString());
		fileStoreTX.setText(formatOsFileStore());
		memoryTX.setText(formatMemory());
		pcManufacturerTX.setText(systemInfo.getHardware().getComputerSystem().getManufacturer());
		pcModelTX.setText(systemInfo.getHardware().getComputerSystem().getModel());
		pcSerialNumberTX.setText(systemInfo.getHardware().getComputerSystem().getSerialNumber());
		graphicCardTX.setText(formatGraphicCard()); 
		processorTX.setText(systemInfo.getHardware().getProcessor().toString());
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
	}

	void updateProcess() {
		getProcesses();
	}
	
	void updateService() {
		getServices();
	}
}
