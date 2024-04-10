import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipToVideoConverter {

     public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter 1 to convert a file to video or 2 to convert back to ZIP:");
            String userInput = reader.readLine();

            if ("1".equals(userInput)) {
                convertZipToVideo("Upload/test.zip", "Download/file.mp4");
            } else if ("2".equals(userInput)) {
                convertVideoToZip("Download/file.mp4", "Upload/testconv.zip");
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertZipToVideo(String zipFilePath, String videoFilePath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                File zipFile = new File(zipFilePath);
                byte[] fileData = Files.readAllBytes(zipFile.toPath());
                ZipEntry entry = new ZipEntry(zipFile.getName());
                zos.putNextEntry(entry);
                zos.write(fileData);
                zos.closeEntry();
            }

            Files.write(Path.of(videoFilePath), baos.toByteArray());
            System.out.println("Converting file in " + zipFilePath + " to video in " + videoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    private static void convertVideoToZip(String videoFilePath, String outputZipFilePath) {
        try {
            byte[] videoData = Files.readAllBytes(Path.of(videoFilePath));

            try (FileOutputStream fos = new FileOutputStream(outputZipFilePath);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                ZipEntry entry = new ZipEntry("restored.zip");
                zos.putNextEntry(entry);
                zos.write(videoData);
                zos.closeEntry();
            }
            System.out.println("Converting video in " + videoFilePath + " back to ZIP in " + outputZipFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
