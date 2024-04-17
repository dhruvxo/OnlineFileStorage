package inf_storage.inf_storage.Utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MultiPartFileHandler {
    
    public static String convert(MultipartFile file) throws IOException {
        Path newFile = Paths.get(file.getOriginalFilename());
        try{
           InputStream is = file.getInputStream();
           OutputStream os = Files.newOutputStream(newFile);
           byte[] buffer = new byte[4096];
           int read = 0;
           while((read = is.read(buffer)) > 0) {
             os.write(buffer,0,read);
            }   
        } catch (IOException e) {}
        return newFile.getFileName().toString();  
    }
}
