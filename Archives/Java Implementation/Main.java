import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;

public class Main {

    private static final String INPUT_ZIP = "C:\\Users\\dhruv\\Documents\\cs-repo\\INF_File_Storage\\Upload\\template_programs.zip";
    private static final String OUTPUT_VIDEO = "output.mp4";

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(INPUT_ZIP);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry entry = zis.getNextEntry();

        if (entry != null) {
            FileOutputStream fos = new FileOutputStream(OUTPUT_VIDEO);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            fos.close();
        }
        zis.closeEntry();
        zis.close();
        fis.close();
    }
}