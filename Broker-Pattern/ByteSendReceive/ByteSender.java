package ToyORB.ByteSendReceive;

import ToyORB.Registry.Entry;
import java.io.OutputStream;
import java.net.Socket;

public class ByteSender {
    private String name;

    public ByteSender(String name) {
        this.name = name;
    }

    public void deliver(Entry dest, byte[] data) {
        try (Socket socket = new Socket(dest.dest(), dest.port())) {
            OutputStream out = socket.getOutputStream();
            out.write(data.length);
            out.write(data);
            out.flush();
        } catch (Exception e) {
            System.out.println("ByteSender: Failed to send data.");
            e.printStackTrace();
        }
    }
}
