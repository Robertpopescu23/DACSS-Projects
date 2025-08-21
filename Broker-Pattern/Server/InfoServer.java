
package ToyORB.Server;

public class InfoServer
{
    public String get_road_info(int road_ID)
    {
        return "Info for road " + road_ID;
    }

    public String get_temp(String city)
    {
        return "Temperature in " + city + " is 25°C";
    }
}