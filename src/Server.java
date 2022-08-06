import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    static ServerSocket MONITORING_SOCKET;
    final static int PORT_TO_MONITOR = 1221;

    public static void main(String[] args) {
        MONITORING_SOCKET = null;

        try {
            MONITORING_SOCKET = new ServerSocket(PORT_TO_MONITOR);
        } catch (Exception | Error e) {
            System.out.println("Exception/Error occurred while creating port: " + e.getMessage());
        }

        System.out.println("Starting server...");
        Socket incomingSocket;
        while (true) {
            try {
                incomingSocket = MONITORING_SOCKET.accept();
                System.out.println("\nAccepted incoming connection: " + incomingSocket.getInetAddress());

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