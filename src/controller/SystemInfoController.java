package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.PhysicalMemory;
import oshi.software.os.OSFileStore;


public class SystemInfoController implements Initializable{

	SystemInfo systemInfo = new SystemInfo();
	
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ManufacturerTX.setText(systemInfo.getOperatingSystem().getManufacturer());
		FamilyTX.setText(systemInfo.getOperatingSystem().getFamily());
		VersionTX.setText(systemInfo.getOperatingSystem().getVersionInfo().toString());
		
		FileStoreTX.setText(formatOsFileStore());
		MemoryTX.setText(formatMemory());
		MemoryTX.setWrappingWidth(SystemInfoAP.getWidth());
		
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
}
