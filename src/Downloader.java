import java.io.*;
import java.nio.Buffer;
import java.util.Arrays;

public class Downloader {
    static String PATH_TO_MONITOR = "gs://mercurybucket";
    static String DIRECTORY_TO_SAVE_TO = "C:\\Users\\sarth\\Desktop\\MercuryDownloads";

    public static void main(String[] args) {
        try {
            while (true) {
                String[] listCommand = ("gsutil.cmd ls " + PATH_TO_MONITOR).split(" ");
                System.out.println("Command: " + Arrays.toString(listCommand));

                ProcessBuilder builder = new ProcessBuilder(listCommand);
                Process listProcess = builder.start();
                InputStream inputStream = listProcess.getInputStream();

                byte[] chunk = new byte[1024 * 4];
                StringBuilder filesToDownloadUrl = new StringBuilder();
                while (inputStream.read(chunk) > 0) {
                    filesToDownloadUrl.append(new String(chunk));
                }
                System.out.println("Files to move: " + filesToDownloadUrl);


                for (String downloadUrl : filesToDownloadUrl.toString().split("\n")) {
                    downloadUrl = downloadUrl.trim();
                    if (downloadUrl.isEmpty()) continue;

                    String[] moveCommand = ("gsutil.cmd mv \"" + downloadUrl + "\" \"" + DIRECTORY_TO_SAVE_TO + "\"").split(" ");
                    System.out.println("Command: " + Arrays.toString(moveCommand));

                    Process downloadProcess = new ProcessBuilder(moveCommand).inheritIO().start();
                    downloadProcess.waitFor();
                }


                System.out.println("Sleeping for 10s...");
                Thread.sleep(10_000);
                System.out.println("Woke up !!!");
            }
        } catch (Exception | Error e) {
            System.out.println("Error occurred while downloading from bucket: " + e.getMessage());
        }
    }
}