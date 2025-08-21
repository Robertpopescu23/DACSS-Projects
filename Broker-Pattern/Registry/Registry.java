
package ToyORB.Registry;

import java.util.Hashtable;

public class Registry
{
    private static Registry instance;

    private Hashtable<String, Entry> hTable = new Hashtable<>();

    private Registry() {} //Singleton principle Hide important implementations

    public static Registry instance()
    {
        if(instance == null)
            instance = new Registry();
        return instance;
    }
    public void put(String key, Entry entry)
    {
        hTable.put(key, entry);
    }
    public Entry get(String key)
    {
        return hTable.get(key);
    }
}