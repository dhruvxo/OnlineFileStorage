package inf_storage.inf_storage.Model;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class UploadForm {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public File getFileObject()
    {
        File _file = new File(file.getOriginalFilename());
        try {
            file.transferTo(_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _file;
    }
}
