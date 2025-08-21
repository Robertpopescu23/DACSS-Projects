
package ToyORB.Server;

public class MathServer
{
    public float do_add(float a, float b)
    {
        return a + b;
    }

    public float do_sqr(float a)
    {
        return (float) Math.sqrt(a);
    }
}