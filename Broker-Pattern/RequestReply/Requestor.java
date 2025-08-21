package ToyORB.RequestReply;

import ToyORB.Registry.Entry;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Requestor {
    private String name;

    public Requestor(String name) {
        this.name = name;
    }

    public byte[] deliver_and_wait_feedback(Entry dest, byte[] data) {
        byte[] buffer = null;

        try (Socket socket = new Socket(dest.dest(), dest.port())) {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write(data.length);
            out.write(data);
            out.flush();

            int len = in.read();
            buffer = new byte[len];
            in.read(buffer);
        } catch (Exception e) {
            System.out.println("Requestor: Error - " + e.getMessage());
            e.printStackTrace();
        }

        return buffer;
    }
}
