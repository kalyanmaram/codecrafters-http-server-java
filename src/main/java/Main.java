import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    ServerSocket serverSocket = null; // Create a server socket to listen for client connection.
    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      while (true) {
        try (Socket clienSocket = serverSocket.accept()) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          OutputStream outputStream = clientSocket.getOutputStream();

          String requestLine = reader.readLine();
          String[] requestParts = requestLine.split(" ");
          String path = requestParts[1];

          if ("/".equals(path)) {
            outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
          } else {
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

// try {
// serverSocket = new ServerSocket(4221);
// serverSocket.setReuseAddress(true);
// clientSocket = serverSocket.accept(); // Accept the client connection.
// InputStream inputStream = clientSocket.getInputStream();

// // what is InputStream and OutputStream?
// // InputStream is used to read data from a source and OutputStream is used
// for writing data to a destination.
// OutputStream outputStream = clientSocket.getOutputStream(); // Get the output
// stream to send data to client.
// outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes()); // Write the HTTP
// response to the client.
// outputStream.flush(); // Flush the output stream to send the data to the
// client.
// clientSocket.close();
// System.out.println("accepted new connection");