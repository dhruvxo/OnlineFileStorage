package inf_storage.inf_storage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inf_storage.inf_storage.Model.*;


@RestController
public class APIController {

    //Handler for login
    @PostMapping("/api/login")
    public Response Login(@RequestBody LoginForm loginForm) {
        return new Response();
    }

    //Handler for register
    @PostMapping("/api/register")
    public Response Register(@RequestBody RegisterForm loginForm) {
        return new Response();
    }

    //Handler for upload
    @PostMapping("/api/upload")
    public Response Upload(@RequestBody UploadForm uploadForm) {
        return new Response();
    }
    
    //Handler for download the video, filename is the name
    @GetMapping("/api/download")
    public Response Download(@RequestParam String filename) {
        return new Response();
    }

    //Handler for list all the videos, put some params that will be required to list
    @GetMapping("/api/list")
    public Response List() {
        return new Response();
    }

    
}
