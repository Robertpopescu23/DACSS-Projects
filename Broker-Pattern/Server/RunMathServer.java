package ToyORB.Server;

import ToyORB.Configuration.Configuration;
import ToyORB.Registry.Registry;
import ToyORB.RequestReply.Replyer;
import ToyORB.Registry.Entry;

public class RunMathServer
{
    public static void main(String[] args)
    {
        new Configuration();
        //Get entry for InfoServer from registry
        Entry myEntry = Registry.instance().get("MathServer");
        // init the Replyer with InfoServer entry
        Replyer r = new Replyer("MathServer", myEntry);

        MathServerWithRR transformer = new MathServerWithRR();
        while(true)
        {
            r.receive_transform_and_send_feedback(transformer);
        }
    }
}