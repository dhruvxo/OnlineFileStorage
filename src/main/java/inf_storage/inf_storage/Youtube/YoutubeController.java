package inf_storage.inf_storage.Youtube;

import inf_storage.inf_storage.Youtube.LIB.*;
import java.util.List;

/**
 * InnerYoutubeController
 */



public class YoutubeController {
    
    public static void uploadVideo(String name) {
        String videoName = name.substring(0, name.indexOf('.', 0));
        System.out.println("Uploading video to Youtube "+videoName+"...");
        Upload.__upload("/static/"+name,videoName);
        System.out.println("Uploading video to Youtube");
    }

    public static List<List<String>> listVideos() {
        System.out.println("Listing videos from Youtube");
        return ListFiles.__listFiles();
    }

    public static String downloadVideo(String id) {
        System.out.println("Downloading video from Youtube");
        // Download.__download(id);
        return  new String("Sherlock.txt.mp4");
    }
}
