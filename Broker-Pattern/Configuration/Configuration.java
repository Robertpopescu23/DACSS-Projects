
package ToyORB.Configuration;

import ToyORB.Registry.Entry;
import ToyORB.Registry.Registry;

public class Configuration
{
    public Configuration()
    {
        Registry r = Registry.instance();
        r.put("InfoServer", new Entry("127.0.0.1", 2222));
        r.put("MathServer", new Entry("127.0.0.1", 3333));
        //with 0 instead of 1 will cause socket connection issues
        //invalid loopback IP
    }
}