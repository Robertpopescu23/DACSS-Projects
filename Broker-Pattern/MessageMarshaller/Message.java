
package ToyORB.MessageMarshaller;

public class Message
{
    public String sender;
    public String data;

    public Message(String sender, String data)
    {
        this.sender = sender;
        this.data = data;
    }
    public String getSender()
    {
        return sender;
    }
    public String getData()
    {
        return data;
    }
}