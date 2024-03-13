import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(4221)) {
      serverSocket.setReuseAddress(true);
      while (true) {
        try (Socket clientSocket = serverSocket.accept()) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          OutputStream outputStream = clientSocket.getOutputStream();

          String requestLine = reader.readLine();
          String[] requestParts = requestLine.split(" ");
          String path = requestParts[1];

          if (path.startsWith("/echo/")) {
            String randomString = path.substring(6);
            String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + randomString.length()+ "\r\n\r\n" + randomString;
            outputStream.write(response.getBytes());
          } else if (path.equals("/")){
            outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
          }else{
            outputStream.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
          }
          outputStream.flush(); 
        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}