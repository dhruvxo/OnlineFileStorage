package inf_storage.inf_storage.Model;

import java.io.File;
public class Params {
    private static Params single_instance = null;

    private String _uploadPath;
    private String _downloadPath;

    private String tempPath;
    private String framesPath;

    
    private Params()
    {
        
    }

    private void createFolder(String path){
        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();           
        }
    }
    public static synchronized Params getInstance()
    {
        if (single_instance == null)
            single_instance = new Params();
 
        return single_instance;
    }

    public String getUploadPath() {
        
        return _uploadPath;
    }

    public void setUploadPath(String _uploadPath) {
        createFolder(_uploadPath);
        this._uploadPath = _uploadPath;

    }

    public String getDownloadPath() {
        return _downloadPath;
    }


    public void setDownloadPath(String _downloadPath) {
        createFolder(_downloadPath);
        this._downloadPath = _downloadPath;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        createFolder(tempPath);
        this.tempPath = tempPath;
    }

    public String getFramesPath() {
        return framesPath;
    }

    public void setFramesPath(String framesPath) {
        createFolder(framesPath);
        this.framesPath = framesPath;
    }
 
}