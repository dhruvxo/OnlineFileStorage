package inf_storage.inf_storage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inf_storage.inf_storage.Model.*;
import inf_storage.inf_storage.Youtube.YoutubeController;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import inf_storage.inf_storage.Utils.*;

import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

@RestController
public class APIController {

    Params config = Params.getInstance();
    // Handler for login
    @PostMapping("/api/login")
    public Response Login(@RequestBody LoginForm loginForm) {
        return Response.create(200);
    }

    // Handler for register
    @PostMapping("/api/register")
    public Response Register(@RequestBody RegisterForm loginForm) {
        return Response.create(200);
    }

    // Handler for upload
    @PostMapping("/api/upload")
    public Response Upload(@RequestParam("file") MultipartFile file) {
        String input_path = "";
        String output_path = "";
        String outputFileName = "";
        
        int status = 200;
        String message = "Success";

        try {
            input_path = MultiPartFileHandler.convert(file);
            outputFileName = Encoder.Encode(new File(input_path));
            output_path = "src/main/resources/static/" + outputFileName;

        } catch (Exception e) {

            System.out.println("Error Converting file");
            status = 400;
            message = "Error converting file";
        }
        try {
            File output_file = new File(output_path);
            System.out.println(output_path);
            if (status == 200 && output_file.exists())
                YoutubeController.uploadVideo(outputFileName);
        } catch (Exception e) {
            System.out.println("Error uploading to youtube");
            status = 400;
            message = "Error uploading to youtube";
        }

        File inputFile = new File(input_path);
        File outputFile = new File(output_path);
        if (inputFile.exists()) {
            inputFile.delete();
        }

        if (outputFile.exists()) {
            // outputFile.delete();
        }
        return Response.create(status, message);
    }

    // Handler for download the video, filename is the name
    @PostMapping("/api/download")
    public Response Download(@RequestBody String fileId) {

        int status = 200;
        String message = "Success";
        byte[] data = null;
        String fileName =  YoutubeController.downloadVideo(fileId);
        String filePath = config.getTempPath() + fileName;
        File file = new File(filePath);


        if (file.exists()) {
            fileName=fileName.substring(0, fileName.lastIndexOf('.'));
            Decoder.Decode(fileName);
            try {
                InputStream in = new FileInputStream(config.getDownloadPath()+fileName);
                data = IOUtils.toByteArray(in);
                in.close();
                message = "Downloaded file successfully";
            } catch (Exception e) {
                System.out.println("Error reading file");
                status = 400;
                message = "Error reading file";
            }
        } else {
            status = 400;
            message = "File not found";
        }

        return Response.createWithData(status, message, data, fileName);
    }

    // Handler for list all the videos, put some params that will be required to
    // list
    @GetMapping("/api/list")
    public Response List() {
        return Response.create(200);
    }

}
