package ToyORB.RequestReply;

import ToyORB.Registry.Entry;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Replyer {
    private ServerSocket serverSocket;

    public Replyer(String name, Entry entry) {
        try {
            serverSocket = new ServerSocket(entry.port());
            System.out.println("Replyer: Listening on port " + entry.port());
        } catch (Exception e) {
            System.out.println("Replyer: Failed to open server socket.");
            e.printStackTrace();
        }
    }

    public void receive_transform_and_send_feedback(ByteStreamTransformer transformer) {
        try (Socket clientSocket = serverSocket.accept()) {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            int len = in.read();
            byte[] buffer = new byte[len];
            in.read(buffer);

            byte[] response = transformer.transform(buffer);

            out.write(response.length);
            out.write(response);
            out.flush();

        } catch (Exception e) {
            System.out.println("Replyer: Error handling request.");
            e.printStackTrace();
        }
    }
}
