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
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("convert file to video press 1 and Enter\nconvert video to file press 2 and Enter\nDownload video from youtube press 3 and Enter");
        String input_Data = scanner.nextLine();
        scanner.close();

        if (input_Data.equals("1")) {
            binaryToVideo(fileToBinary(),0,0,0,0);
        } else if (input_Data.equals("2")) {
            binaryToFile(processImages(extractFrames()));
        } else if (input_Data.equals("3")) {
            //youtubeVideoDownloader(scanner.nextLine());
            System.out.println("not implemented yet");
        } else {
            System.out.println("404");
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
        System.out.println(fileName + " converted to binary");
        return binaryString.toString();
    }

    public static List<BufferedImage> extractFrames() {
        List<BufferedImage> frames = new ArrayList<>();
        File dir = new File(System.getProperty("user.dir"));
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".webm") || name.endsWith(".mp4"));
        File videoFile = null;
        if (files != null && files.length > 0) {
            videoFile = files[0];
        }
        if (videoFile != null) {
            try {
                InputStream inputStream = new FileInputStream(videoFile);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] videoData = outputStream.toByteArray();
                inputStream.close();
                outputStream.close();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(videoData);
                // You can use your preferred library for reading frames from video
                // For simplicity, I'm assuming it's already implemented.
                // You may need to use libraries like OpenCV for this purpose in Java.
                // Pseudocode:
                // frames = readFramesFromVideo(byteArrayInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return frames;
    }

    public static String processImages(List<BufferedImage> frames) {
        StringBuilder binaryDigits = new StringBuilder();
        int threshold = 128;
        int pixelSize = 4;
        for (BufferedImage frame : frames) {
            for (int y = 0; y < frame.getHeight(); y += pixelSize) {
                for (int x = 0; x < frame.getWidth(); x += pixelSize) {
                    int color = frame.getRGB(x, y);
                    int red = (color >> 16) & 0xFF;
                    int green = (color >> 8) & 0xFF;
                    int blue = color & 0xFF;
                    int luminance = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
                    if (luminance < threshold) {
                        binaryDigits.append("1");
                    } else {
                        binaryDigits.append("0");
                    }
                }
            }
        }
        return binaryDigits.toString();
    }


    public static void binaryToFile(String binaryFilename) {
        StringBuilder binaryData = new StringBuilder();
        for (int i = 0; i < binaryFilename.length(); i += 8) {
            String byteString = binaryFilename.substring(i, Math.min(i + 8, binaryFilename.length()));
            int byteValue = Integer.parseInt(byteString, 2);
            binaryData.append((char) byteValue);
        }

        try (FileOutputStream fos = new FileOutputStream("reverse.zip")) {
            byte[] binaryBytes = binaryData.toString().getBytes();
            int chunkSize = 1024;
            int totalBytes = binaryBytes.length;
            int bytesWritten = 0;
            while (bytesWritten < totalBytes) {
                int remainingBytes = totalBytes - bytesWritten;
                int bytesToWrite = Math.min(remainingBytes, chunkSize);
                fos.write(binaryBytes, bytesWritten, bytesToWrite);
                bytesWritten += bytesToWrite;
            }
            System.out.println("Binary data converted to example_reverse.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void binaryToFile(String binaryFilename) {
        StringBuilder binaryData = new StringBuilder();
        for (int i = 0; i < binaryFilename.length(); i += 8) {
            String byteString = binaryFilename.substring(i, Math.min(i + 8, binaryFilename.length()));
            int byteValue = Integer.parseInt(byteString, 2);
            binaryData.append((char) byteValue);
        }

        try (FileOutputStream fos = new FileOutputStream("reverse.zip")) {
            byte[] binaryBytes = binaryData.toString().getBytes();
            int chunkSize = 1024;
            int totalBytes = binaryBytes.length;
            int bytesWritten = 0;
            while (bytesWritten < totalBytes) {
                int remainingBytes = totalBytes - bytesWritten;
                int bytesToWrite = Math.min(remainingBytes, chunkSize);
                fos.write(binaryBytes, bytesWritten, bytesToWrite);
                bytesWritten += bytesToWrite;
            }
            System.out.println("Binary data converted to example_reverse.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void binaryToText(String binaryFilename) {
        StringBuilder binaryData = new StringBuilder();
        for (int i = 0; i < binaryFilename.length(); i += 8) {
            String byteString = binaryFilename.substring(i, Math.min(i + 8, binaryFilename.length()));
            int byteValue = Integer.parseInt(byteString, 2);
            binaryData.append((char) byteValue);
        }

        try (FileWriter writer = new FileWriter("output.txt")) {
            writer.write(binaryData.toString());
            System.out.println("Binary data converted to output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void imagesToBinary() {
        File directory = new File("temp");
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".png"));
        if (files != null && files.length > 0) {
            StringBuilder binaryDigits = new StringBuilder();
            int threshold = 128;
            int pixelSize = 4;
            for (File file : files) {
                try {
                    BufferedImage image = ImageIO.read(file);
                    for (int y = 0; y < image.getHeight(); y += pixelSize) {
                        for (int x = 0; x < image.getWidth(); x += pixelSize) {
                            int color = image.getRGB(x, y);
                            int red = (color >> 16) & 0xFF;
                            int green = (color >> 8) & 0xFF;
                            int blue = color & 0xFF;
                            int luminance = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
                            if (luminance < threshold) {
                                binaryDigits.append("1");
                            } else {
                                binaryDigits.append("0");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            binaryToText(binaryDigits.toString());
        }
    }

    public static void binaryToVideo(String binString, int width, int height, int pixelSize, int fps) {
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
            System.out.println(frames);

            String binaryDigits = binString.substring(startIndex, endIndex); // Get the binary digits for the current frame

            // Create a new image object with the given size
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Loop through each row of binary digits
            for (int row_index = 0; row_index < height / pixelSize; row_index++) {
                // Calculate the range of binary digits for the current row
                int start_index = row_index * (width / pixelSize);
                int end_index = Math.min(start_index + (width / pixelSize), row_index * (width / pixelSize) + (width / pixelSize));
                if (end_index > binaryDigits.length() || end_index < start_index) {
                    // Handle the error, e.g., by logging an error message or throwing an exception
                    System.err.println("Invalid index range for substring: start_index=" + start_index + ", end_index=" + end_index);
                    continue; // Skip this iteration
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
        File directory = new File("temp");
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
    }

        // Create a video from the frames using some video creation library
        // Your existing binaryToVideo method code here...

        // // Output video file name
        // String outputVideoFileName = "output.mp4"; // Change this to your desired output file name

        // // Create a media writer
        // IMediaWriter writer = ToolFactory.makeWriter(outputVideoFileName);

        // // Add a video stream with the same width, height, and frame rate as the frames
        // writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, width, height);

        // // Set the global timebase to microseconds
        // Global.DEFAULT_TIME_UNIT = Global.NO_PTS;

        // // Convert frames to video
        // long frameDuration = Global.DEFAULT_TIME_UNIT.convert(1, Global.DEFAULT_TIME_UNIT) / fps;
        // long startTime = 0;
        // for (BufferedImage frame : frames) {
        //     // Convert BufferedImage to byte array
        //     ByteArrayOutputStream out = new ByteArrayOutputStream();
        //     try {
        //         ImageIO.write(frame, "png", out);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        //     byte[] bytes = out.toByteArray();

        //     // Write the frame to the video file
        //     writer.encodeVideo(0, bytes, startTime, Global.DEFAULT_TIME_UNIT);
        //     startTime += frameDuration;
        // }

        // // Close the writer
        // writer.close();

        // System.out.println("Video created: " + outputVideoFileName);
}