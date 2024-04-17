package inf_storage.inf_storage.Youtube.LIB;
import java.io.*;

public class Download {

    
    private static String path = "temp/";
    
    // public Download(String dl_path){
    //     pass;
    // }

    public void __download(String id){
        try {
            // Command to be executed
            String command = "python download.py "+id+" "+path ;
            
            // Execute command
            Process process = Runtime.getRuntime().exec(command);
            
            // Read output from terminal
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            // Wait for the process to finish
            process.waitFor();
            
            // Close streams
            reader.close();
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
