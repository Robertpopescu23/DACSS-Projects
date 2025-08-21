package ToyORB.MessageMarshaller;

public class Marshaller {
    public byte[] marshal(Message msg) {
        String raw = msg.sender + ":" + msg.data;
        byte[] data = raw.getBytes();
        byte[] result = new byte[data.length + 1];
        result[0] = (byte) data.length;
        System.arraycopy(data, 0, result, 1, data.length);
        return result;
    }

    public Message unmarshal(byte[] bytes) {
        String content = new String(bytes, 1, bytes[0]);
        int split = content.indexOf(":");
        String sender = content.substring(0, split);
        String data = content.substring(split + 1);
        return new Message(sender, data);
    }
}
