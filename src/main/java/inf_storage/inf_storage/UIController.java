package inf_storage.inf_storage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import inf_storage.inf_storage.Youtube.YoutubeController;

import java.util.*;
@Controller
public class UIController {
    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @GetMapping("/download")
    public String download(Model model) {
        List<List<String>> files = YoutubeController.listVideos();
        if(files == null) {
            files = new ArrayList<List<String>>();
        }
        model.addAttribute("files", files);
        for(List<String> file: files) {
            System.out.println(file.get(0));
        }
        return "download";
    }

}
