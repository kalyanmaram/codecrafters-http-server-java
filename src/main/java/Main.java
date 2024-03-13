import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) {
    String directory = args[0]; // Get the directory from command line arguments

    try (ServerSocket serverSocket = new ServerSocket(4221)) {
      serverSocket.setReuseAddress(true);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        new Thread(() -> {
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
               OutputStream outputStream = clientSocket.getOutputStream()) {

            String requestLine = reader.readLine();
            String[] requestParts = requestLine.split(" ");
            String path = requestParts[1];

            String userAgent = "";
            String line;
            while (!(line = reader.readLine()).equals("")) {
              if (line.startsWith("User-Agent: "))
                userAgent = line.substring(12);
            }

            if (path.startsWith("/files/")) {
              String fileName = path.substring(7);
              Path filePath = Paths.get(directory, fileName);
              if (Files.exists(filePath)) {
                byte[] fileBytes = Files.readAllBytes(filePath);
                String response = "HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length: " + fileBytes.length + "\r\n\r\n";
                outputStream.write(response.getBytes());
                outputStream.write(fileBytes);
              } else {
                outputStream.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
              }
            } else if (path.startsWith("/user-agent")) {
              String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgent.length() + "\r\n\r\n" + userAgent;
              outputStream.write(response.getBytes());
            } else if (path.startsWith("/echo/")) {
              String randomString = path.substring(6);
              String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + randomString.length() + "\r\n\r\n" + randomString;
              outputStream.write(response.getBytes());
            } else if (path.equals("/")) {
              outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
            } else {
              outputStream.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            }
            outputStream.flush();
          } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
          } finally {
            try {
              clientSocket.close();
            } catch (IOException e) {
              System.out.println("IOException: " + e.getMessage());
            }
          }
        }).start();
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}