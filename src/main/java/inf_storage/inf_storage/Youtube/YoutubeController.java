package inf_storage.inf_storage.Youtube;

// import inf_storage.Youtube.LIB.Upload;
import inf_storage.inf_storage.Youtube.LIB.ListFiles;
import java.util.List;


public class YoutubeController {
    public static void uploadVideo() {
        // Upload.__upload("/test.mp4");
        System.out.println("Uploading video to Youtube");
    }

    public static List<List<String>> listVideos() {
        System.out.println("Listing videos from Youtube");
        return ListFiles.__listFiles();
    }
}
