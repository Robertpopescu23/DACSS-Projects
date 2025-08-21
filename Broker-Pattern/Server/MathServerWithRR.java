package ToyORB.Server;

import ToyORB.MessageMarshaller.*;
import ToyORB.RequestReply.ByteStreamTransformer;

public class MathServerWithRR implements ByteStreamTransformer
{
    private MathServer server = new MathServer();

    public byte[] transform(byte[] input)
    {
        Marshaller m = new Marshaller();
        Message msg = m.unmarshal(input);
        String response;
        if (msg.data.startsWith("do_add")) {
            // for float param from message as 4.4, 1.15 etc
            String[] parts = msg.data.split("\\(")[1].replace(")", "").split(",");
            float a = Float.parseFloat(parts[0]);
            float b = Float.parseFloat(parts[1]);
            response = Float.toString(server.do_add(a, b));
        } else if (msg.data.startsWith("do_sqr")) {
            // for parameters of cast (float)
            float a = Float.parseFloat(msg.data.split("\\(")[1].replace(")", ""));
            response = Float.toString(server.do_sqr(a));
        } else {
            response = "Uninitialized method";
        }

        return m.marshal(new Message("MathServer", response));
    }
}