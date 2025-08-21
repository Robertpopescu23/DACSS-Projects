package ToyORB.Server;

import ToyORB.Configuration.Configuration;
import ToyORB.Registry.Registry;
import ToyORB.RequestReply.Replyer;
import ToyORB.Registry.Entry;

public class RunInfoServer {
    public static void main(String[] args) {
        new Configuration();

        Entry myEntry = Registry.instance().get("InfoServer");

        Replyer r = new Replyer("InfoServer", myEntry);

        InfoServerWithRR transformer = new InfoServerWithRR();

        while (true) {
            r.receive_transform_and_send_feedback(transformer);
        }
    }
}
