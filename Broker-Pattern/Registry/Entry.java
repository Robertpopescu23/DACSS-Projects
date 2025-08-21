
package ToyORB.Registry;

public class Entry
{
    private String destinationId;
    private int portNr;

    public Entry(String destinationId, int portNr)
    {
        this.destinationId = destinationId;
        this.portNr = portNr;
    }

    public String dest() { return destinationId; }
    public int port() { return portNr; }
}