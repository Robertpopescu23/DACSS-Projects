package ToyORB.Client;

import ToyORB.Configuration.Configuration;
import ToyORB.Registry.Entry;
import ToyORB.Registry.Registry;
import ToyORB.RequestReply.Requestor;
import ToyORB.MessageMarshaller.*;

public class RunClient {
    public static void main(String[] args) {
        new Configuration(); // Ensure servers are registered

        Requestor requestor = new Requestor("Client");
        Marshaller m = new Marshaller();

        // InfoServer call
        Entry infoEntry = Registry.instance().get("InfoServer");
        Message roadRequest = new Message("Client", "get_road_info(67)");
        byte[] response1 = requestor.deliver_and_wait_feedback(infoEntry, m.marshal(roadRequest));
        System.out.println("InfoServer replied: " + m.unmarshal(response1).getData());

        // MathServer call
        Entry mathEntry = Registry.instance().get("MathServer");
        Message addRequest = new Message("Client", "do_add(4.1,22.2)");
        byte[] response2 = requestor.deliver_and_wait_feedback(mathEntry, m.marshal(addRequest));
        System.out.println("MathServer replied: " + m.unmarshal(response2).getData());
    }
}
