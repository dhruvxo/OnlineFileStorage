package inf_storage.inf_storage.Model;

public class Response {
    private int status;
    private String message;
    private byte[] data;
    private String fileName;

    // Private constructor to prevent instantiation outside this class
    private Response(int status, String message, byte[] data, String fileName) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.fileName = fileName;
    }

    // Factory method to create Response objects with status and message
    public static Response create(int status, String message) {
        return new Response(status, message, null, null);
    }

    // Factory method to create Response objects with only status
    public static Response create(int status) {
        return new Response(status, null, null, null);
    }

    // Factory method to create Response objects with status, message, data, and file name
    public static Response createWithData(int status, String message, byte[] data, String fileName) {
        return new Response(status, message, data, fileName);
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getData() {
        return data;
    }

    public String getFileName() {
        return fileName;
    }
}
