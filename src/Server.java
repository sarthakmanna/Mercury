import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.time.*;

public class Server {
    static ServerSocket MONITORING_SOCKET;
    final static int PORT_TO_MONITOR = 4944;

    public static void main(String[] args) {
        MONITORING_SOCKET = null;

        try {
            MONITORING_SOCKET = new ServerSocket(PORT_TO_MONITOR, 0, InetAddress.getByName("localhost"));
        } catch (Exception | Error e) {
            System.out.println("Exception/Error occurred while creating port: " + e.getMessage());
        }
        System.out.println("Starting server at " + MONITORING_SOCKET.getInetAddress());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        Socket incomingSocket;
        while (true) {
            try {
                incomingSocket = MONITORING_SOCKET.accept();
                System.out.println("\n[" + dtf.format(LocalDateTime.now()) + "] Accepted incoming connection: " + incomingSocket.getInetAddress());

                ParallelServer server = new ParallelServer(incomingSocket);
                server.start();
            } catch (Exception | Error e) {
                System.out.println("Exception/Error occurred while accepting connection: " + e.getMessage());
            }
        }
    }
}

class ParallelServer extends Thread {
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    final static int CHUNK_SIZE = 2 * 1024;

    public ParallelServer(Socket incomingConnection) {
        socket = incomingConnection;
        try {
            inputStream = incomingConnection.getInputStream();
            outputStream = incomingConnection.getOutputStream();
        } catch (Error | Exception e) {
            System.out.println("Exception/Error occurred while connecting inputStream: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            byte[] data = new byte[CHUNK_SIZE];

            int count;
            while ((count = inputStream.read(data)) > 0) {
                System.out.println(Arrays.toString(data));
                outputStream.write(data, 0, count);
            }

            System.out.println("Data transmission over!!!");
        } catch (Exception | Error e) {
            System.out.println("Exception/Error occurred while transmitting data: " + e.getMessage());
        }

        System.out.println("Closing server for " + socket.getInetAddress());
    }
}