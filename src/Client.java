import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    final static String HOST_IP = "https://tricky-crews-buy-89-64-64-187.loca.lt";
    final static int PORT_TO_REQUEST = 4944;

    static Socket socket;
    static InputStream inputStream;
    static OutputStream outputStream;

    public static void main(String[] args) {
        try {
            socket = new Socket(HOST_IP, PORT_TO_REQUEST);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            System.out.println("Connection established with Server...");
            transmitData();
        } catch (Exception | Error e) {
            System.out.println("Exception/Error occurred while requesting connection: " + e.getMessage());
        }
    }

    static void transmitData() {
        try {
            outputStream.write("Hello world".getBytes(StandardCharsets.UTF_8));
        } catch (Exception | Error e) {
            System.out.println("Exception/Error occurred while transmitting data: " + e.getMessage());
        }
    }
}
