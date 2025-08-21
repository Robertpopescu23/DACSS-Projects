package ToyORB.ByteSendReceive;

import ToyORB.Registry.Entry;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ByteReceiver {
    private ServerSocket serverSocket;

    public ByteReceiver(String name, Entry myEntry) {
        try {
            serverSocket = new ServerSocket(myEntry.port());
            System.out.println("ByteReceiver: Listening on port " + myEntry.port());
        } catch (Exception e) {
            System.out.println("ByteReceiver: Failed to start server socket.");
            e.printStackTrace();
        }
    }

    public byte[] receive() {
        byte[] buffer = null;
        try (Socket socket = serverSocket.accept()) {
            InputStream in = socket.getInputStream();
            int len = in.read();
            buffer = new byte[len];
            in.read(buffer);
        } catch (Exception e) {
            System.out.println("ByteReceiver: Failed to receive.");
            e.printStackTrace();
        }
        return buffer;
    }
}
