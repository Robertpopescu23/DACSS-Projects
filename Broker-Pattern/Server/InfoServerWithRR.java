
package ToyORB.Server;

import ToyORB.MessageMarshaller.*;
import ToyORB.RequestReply.ByteStreamTransformer;

public class InfoServerWithRR implements ByteStreamTransformer
{
    private InfoServer server = new InfoServer();

    public byte[] transform(byte[] input) {
        Marshaller m = new Marshaller();
        Message msg = m.unmarshal(input);

        String response;
        if (msg.data.startsWith("get_road_info")) {
            // get roadID from get_road_info() method from InfoServer.java
            int roadID = Integer.parseInt(msg.data.split("\\(")[1].replace(")", ""));
            response = server.get_road_info(roadID);
        } else if (msg.data.startsWith("get_temp")) {
            // get the city from get_temp() method
            String city = msg.data.split("\\(\"")[1].replace("\")", "");
            response = server.get_temp(city);
        } else {
            response = "Uninitialized method";
        }

        return m.marshal(new Message("InfoServer", response));
    }
}