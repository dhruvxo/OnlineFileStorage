import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
// import org.bytedeco.javacv.FFmpegFrameGrabber;
// import org.bytedeco.javacv.Frame;
// import org.bytedeco.javacv.Java2DFrameConverter;
// import org.bytedeco.javacv.OpenCVFrameConverter;
import java.text.DecimalFormat;



public class file2img {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("convert file to video press 1 and Enter\nconvert video to file press 2 and Enter\nDownload video from youtube press 3 and Enter");
        String input_Data = scanner.nextLine();
        scanner.close();
        if(input_Data.equals("1")){
            // Convert the file to frames and save them as images
            String binaryData = fileToBinary();
            List<BufferedImage> frames = binaryToFrames(binaryData, 1280, 720, 4, 24);
            //framesToBinary(frames, 1280, 720, 4);
            System.out.println("File converted to images");
        }
        else if(input_Data.equals("2")){
            //convert the images to a back into the original file 
            List<BufferedImage> images = Image2Binary("temp0");
            framesToBinary(images, 1280, 720, 4);
            System.out.println("Images converted to file");
        }
        else{
            System.out.println("input WIP");
        }
    }

    public static String fileToBinary() {
        String fileName = "";
        // get file size
        File dir = new File(System.getProperty("user.dir"));
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".txt")) {
                System.out.println(file.getName());
                fileName = file.getName();
            }
        }
        File inputFile = new File(fileName);
        byte[] buffer = new byte[1024];
        StringBuilder binaryString = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                for (byte b : buffer) {
                    binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add an EOF token to the binary string
        String eofToken = "123456789012345678901234567890";
        //convert the binary string to be appended to the binaryString 
        String eofBinary = new String();
        for (int i = 0; i < eofToken.length(); i++) {
            eofBinary += String.format("%8s", Integer.toBinaryString(eofToken.charAt(i) & 0xFF)).replace(' ', '0');
        }
        binaryString.append(eofBinary);
        System.out.println(fileName + " converted to binary");
        return binaryString.toString();
    }


    public static List<BufferedImage> binaryToFrames(String binString, int width, int height, int pixelSize, int fps) {
        // Check if width is not provided, use default value
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

        // Calculate the total number of pixels needed to represent the binary string
        int numPixels = binString.length();

        // Calculate the number of pixels that can fit in one image
        int pixelsPerImage = (width / pixelSize) * (height / pixelSize);

        // Calculate the number of images needed to represent the binary string
        int numImages = (int) Math.ceil((double) numPixels / pixelsPerImage);
        int bitsPerFrame = (width / pixelSize) * (height / pixelSize);


        // Create a list to store the frames
        List<BufferedImage> frames = new ArrayList<>();

        // Loop through each image
        for (int i = 0; i < numImages; i++) {

            // Calculate the range of binary digits for the current frame
            int startIndex = i * bitsPerFrame;
            int endIndex = Math.min(startIndex + bitsPerFrame, binString.length());
            System.out.println("startIndex: " + startIndex + ", endIndex: " + endIndex);

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
                    System.out.println("Index stop" + binaryDigits.length() + " End index: " + end_index + " Padded: " + padding);
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

        // Create a directory to store the frames
        File directory = new File("temp0");
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Save the frames as images in the temp directory
        for (int i = 0; i < frames.size(); i++) {
            BufferedImage frame = frames.get(i);
            String filename = "frame_" + i + ".png";
            File file = new File(directory, filename);
            try {
                ImageIO.write(frame, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return frames;
    }

    public static List<BufferedImage> Image2Binary(String folderPath) {
        List<BufferedImage> images = new ArrayList<>();
        try {
            File folder = new File(folderPath);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                // parse the file in the order they were created
                for (int i = 0; i < listOfFiles.length; i++) {
                    File file = new File(folderPath + "/frame_" + i + ".png");
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        images.add(img);
                    } else {
                        System.out.println("Image not found , check the file path");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }


    public static void framesToBinary(List<BufferedImage> frames, int width, int height, int pixelSize) {
        StringBuilder binaryData = new StringBuilder();
        for (BufferedImage img : frames) {
            for (int y = 0; y < height / pixelSize; y++) {
                for (int x = 0; x < width / pixelSize; x++) {
                    int color = img.getRGB(x * pixelSize, y * pixelSize);
                    int luminance = (color & 0xFF) < 128 ? 1 : 0; // black pixel represents '1' and white pixel represents '0'
                    binaryData.append(luminance);
                }
            }
        }
        System.out.println("Binary data reconstructed from frames");

        // Convert the binary data to text
        String text = binaryToText(binaryData.toString());

        // check for the EOF character
        int eofIndex = text.indexOf("123456789012345678901234567890");
        if (eofIndex != -1) {
            System.out.println("EOF character found at index: " + eofIndex);
            text = text.substring(0, eofIndex);
        }

        System.out.println("Original text retrieved ");
        //write the text to a file
        try {
            Files.write(Paths.get("output.txt"), text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String binaryToText(String binaryData) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binaryData.length(); i += 8) {
            String byteString = binaryData.substring(i, Math.min(i + 8, binaryData.length()));
            int byteValue = Integer.parseInt(byteString, 2);
            text.append((char) byteValue);
        }
        return text.toString();
    }


}
