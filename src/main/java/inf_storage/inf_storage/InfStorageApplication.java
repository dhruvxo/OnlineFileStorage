package inf_storage.inf_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import inf_storage.inf_storage.Model.Params;

@SpringBootApplication
public class InfStorageApplication {

	public static void main(String[] args) {
		Params config =  Params.getInstance();
		config.setUploadPath("Upload/");
		config.setDownloadPath("Downloads/");
		config.setTempPath("Temp/");
		config.setFramesPath("Frames/");

		SpringApplication.run(InfStorageApplication.class,args);
	}

}
