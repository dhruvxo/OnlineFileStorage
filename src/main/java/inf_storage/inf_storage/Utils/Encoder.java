package inf_storage.inf_storage.Utils;

import java.io.FileInputStream;
// import java.io.FileOutputStream;
import java.io.IOException;
// import java.io.InputStream;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import java.util.concurrent.TimeUnit;
import inf_storage.inf_storage.Model.Params;

public class Encoder {

    static Boolean  duplicate = true;

    public static String Encode(File file) {
        Params config = Params.getInstance();
        String binString = fileToBinary(file);
        List<BufferedImage> frames = binaryToFrames(binString, 1280, 720, 4, 24,1);

        String outputFileName = file.getName() + ".mp4";
        String outputFilePath = "src/main/resources/static/" + outputFileName;
        File outputFile = new File(outputFilePath);

        try {
            if (outputFile.createNewFile()) {
                System.out.println("File created: " + outputFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }
        framesToVideo(frames, outputFilePath, 1280, 720, 24);
       
        
        if(duplicate) {
            File duplicateFile = new File(config.getUploadPath() + outputFileName);
            try {
                if (duplicateFile.createNewFile()) {
                    System.out.println("File created: " + duplicateFile.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
            }
            framesToVideo(frames, config.getUploadPath() + outputFileName, 1280, 720, 24);
        }
        return outputFileName;
    }

    private static String fileToBinary(File inputFile) {
        byte[] buffer = new byte[1024];
        StringBuilder binaryString = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    binaryString.append(String.format("%8s", Integer.toBinaryString(buffer[i] & 0xFF)).replace(' ', '0'));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String eofToken = "123456789012345678901234567890123456789012345678901234567890";
        String eofBinary = new String();

        for (int i = 0; i < eofToken.length(); i++) {
            eofBinary += String.format("%8s", Integer.toBinaryString(eofToken.charAt(i) & 0xFF)).replace(' ', '0');
        }
        binaryString.append(eofBinary);

        return binaryString.toString();
    }

    private static List<BufferedImage> binaryToFrames(String binString, int width, int height, int pixelSize, int fps , int genimages) {
        if (width == 0) {
            width = 1280; // Default width for 720p
        }
        // Check if height is not provided, use default value
        if (height == 0) {
            height = 720; // Default height for 720p
        }
        // Check if pixelSize is not provided, use default value
        if (pixelSize == 0) {
            pixelSize = 4; // Default pixel size
        }
        // Check if fps is not provided, use default value
        if (fps == 0) {
            fps = 24; // Default frames per second
        }

        int numPixels = binString.length();
        int pixelsPerImage = (width / pixelSize) * (height / pixelSize);
        int numImages = (int) Math.ceil((double) numPixels / pixelsPerImage);
        int bitsPerFrame = (width / pixelSize) * (height / pixelSize);

        List<BufferedImage> frames = new ArrayList<>();

        for (int i = 0; i < numImages; i++) {

            int startIndex = i * bitsPerFrame;
            int endIndex = Math.min(startIndex + bitsPerFrame, binString.length());
            String binaryDigits = binString.substring(startIndex, endIndex); // Get the binary digits for the current frame

            // Create a new image object with the given size
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Loop through each row of binary digits
            for (int row_index = 0; row_index < height / pixelSize; row_index++) {
                // Calculate the range of binary digits for the current row
                int start_index = row_index * (width / pixelSize);
                int end_index = start_index + (width / pixelSize);
                if (end_index > binaryDigits.length() ) { //
                    // Pad the binary digits with zeros if needed before that add the EOF token
                    int padding = end_index - binaryDigits.length();
                    //System.out.println("Index stop" + binaryDigits.length() + " End index: " + end_index + " Padded: " + padding);
                    binaryDigits += "0".repeat(padding);
                }
                String row = binaryDigits.substring(start_index, end_index);
                // Loop through each column of binary digits
                for (int col_index = 0; col_index < row.length(); col_index++) {
                    char digit = row.charAt(col_index);

                    // Determine the color of the pixel based on the binary digit
                    int color = digit == '1' ? 0 : 255; // Black or white

                    // Calculate the coordinates of the pixel
                    int x1 = col_index * pixelSize;
                    int y1 = row_index * pixelSize;
                    int x2 = x1 + pixelSize;
                    int y2 = y1 + pixelSize;

                    // Set the color of the pixel
                    for (int x = x1; x < x2; x++) {
                        for (int y = y1; y < y2; y++) {
                            img.setRGB(x, y, color << 16 | color << 8 | color);
                        }
                    }
                }
            }

            // Add the frame to the list of frames
            frames.add(img);
        }
        //finally add a black frame to the end of the video
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                img.setRGB(x, y, 0);
            }
        }
        frames.add(img);
        return frames;

    }

    private static void framesToVideo(List<BufferedImage> frames, String outputFilePath, int width, int height, int fps) {
        IMediaWriter writer = ToolFactory.makeWriter(outputFilePath);
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, width, height);
    
        // Calculate the time per frame in microseconds
        long timePerFrame = (long) (TimeUnit.MICROSECONDS.convert(1, TimeUnit.SECONDS) / fps);
        long frameTime = 0;
    
        for (BufferedImage frame : frames) {
            // Convert BufferedImage to a format Xuggler can work with
            BufferedImage convertedFrame = convertToType(frame, BufferedImage.TYPE_3BYTE_BGR);
            // Encode the frame with the calculated frame time
            writer.encodeVideo(0, convertedFrame, frameTime, TimeUnit.MICROSECONDS);
            frameTime += timePerFrame; // Increment the frame time
        }
    
        writer.close();
    }

    private static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        } else {
            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }
        return image;
    }
    
}
