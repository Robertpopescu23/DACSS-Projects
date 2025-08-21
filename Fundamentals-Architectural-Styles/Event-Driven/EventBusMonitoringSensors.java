import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

interface Event {}

interface Subscriber {
    void inform(Event event);
}

interface BasicEventBus {
    void publish(Event event);
    void subscribe(Class<?> eventType, Subscriber subscriber);
    void unsubscribe(Class<?> eventType, Subscriber subscriber);
}

class InvalidEventTypeException extends Exception {
    public InvalidEventTypeException() {
        super("Invalid event type provided.");
    }
}

class BasicEventImpl implements BasicEventBus {
    private static BasicEventImpl singleton = null;
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final Class<?> eventClass = Event.class;

    private static class Subscription {
        private final Class<?> eventType;
        private final Subscriber subscriber;

        public Subscription(Class<?> eventType, Subscriber subscriber) {
            this.eventType = eventType;
            this.subscriber = subscriber;
        }

        public boolean matches(Event event) {
            return eventType.isAssignableFrom(event.getClass());
        }
    }

    private BasicEventImpl() {}

    public static BasicEventImpl instance() {
        if (singleton == null) {
            singleton = new BasicEventImpl();
        }
        return singleton;
    }

    @Override
    public void publish(Event event) {
        for (Subscription subscription : subscriptions) {
            if (subscription.matches(event)) {
                subscription.subscriber.inform(event);
            }
        }
    }

    @Override
    public void subscribe(Class<?> eventType, Subscriber subscriber) {
        if (!eventClass.isAssignableFrom(eventType)) {
            System.err.println("Invalid event type provided.");
            return;
        }
        Subscription subscription = new Subscription(eventType, subscriber);
        if (!subscriptions.contains(subscription)) subscriptions.add(subscription);
    }

    @Override
    public void unsubscribe(Class<?> eventType, Subscriber subscriber) {
    	Iterator<Subscription> iterator = subscriptions.iterator();
    	while(iterator.hasNext())
    	{
    		Subscription s = iterator.next();
    		if(s.eventType.equals(eventType) && s.subscriber.equals(subscriber))
    		{
    			iterator.remove();
    		}
    	}
    }
}

class TemperatureSensor {
    private int temperatureValue;
    private final String ID;
    private final String location;
    private final float precision;

    public TemperatureSensor(String ID, String location, float precision) {
        this.ID = ID;
        this.location = location;
        this.precision = precision;
        this.temperatureValue = 0;
    }

    public void generateTemperature() {
        this.temperatureValue = ThreadLocalRandom.current().nextInt(-10, 40);
        BasicEventImpl.instance().publish(new TemperatureEvent(this));
    }

    public int getTemperatureValue() {
        return temperatureValue;
    }

    public String getLocation() {
        return location;
    }
}

class TemperatureEvent implements Event {
    private final TemperatureSensor sensor;

    public TemperatureEvent(TemperatureSensor sensor) {
        this.sensor = sensor;
    }

    public TemperatureSensor getSensor() {
        return sensor;
    }
}

class HumiditySensor {
    private int humidityValue;
    private final String ID;
    private final String location;

    public HumiditySensor(String ID, String location) {
        this.humidityValue = 0;
        this.ID = ID;
        this.location = location;
    }

    public void generateHumidity() {
        this.humidityValue = ThreadLocalRandom.current().nextInt(10, 100);
        BasicEventImpl.instance().publish(new HumidityEvent(this));
    }

    public int getHumidityValue() {
        return humidityValue;
    }
    
    public String getLocation() {
        return location;
    }
}

class HumidityEvent implements Event {
    private final HumiditySensor sensor;

    public HumidityEvent(HumiditySensor sensor) {
        this.sensor = sensor;
    }

    public HumiditySensor getSensor() {
        return sensor;
    }
}

abstract class Display implements Subscriber {
    protected final String name;

    public Display(String name) {
        this.name = name;
    }
}

class NumericDisplay extends Display {
    public NumericDisplay(String name) {
        super(name);
    }

    @Override
    public void inform(Event event) {
        if (event instanceof TemperatureEvent) {
            TemperatureEvent tempEvent = (TemperatureEvent) event;
            System.out.println(name + " - Temperature: " + tempEvent.getSensor().getTemperatureValue() + "°C at " + tempEvent.getSensor().getLocation());
        } else if (event instanceof HumidityEvent) {
            HumidityEvent humEvent = (HumidityEvent) event;
            System.out.println(name + " - Humidity: " + humEvent.getSensor().getHumidityValue() + "% at " + humEvent.getSensor().getLocation());
        }
    }
}

class MaxValueDisplay extends Display {
    private int maxValue = Integer.MIN_VALUE;

    public MaxValueDisplay(String name) {
        super(name);
    }

    @Override
    public void inform(Event event) {
        if (event instanceof TemperatureEvent) {
            TemperatureEvent tempEvent = (TemperatureEvent) event;
            maxValue = Math.max(maxValue, tempEvent.getSensor().getTemperatureValue());
            System.out.println(name + " - Max Temperature: " + maxValue + "°C");
        }
    }
}

class MinValueDisplay extends Display {
    private int minValue = Integer.MAX_VALUE;

    public MinValueDisplay(String name) {
        super(name);
    }

    @Override
    public void inform(Event event) {
        if (event instanceof TemperatureEvent) {
            TemperatureEvent tempEvent = (TemperatureEvent) event;
            minValue = Math.min(minValue, tempEvent.getSensor().getTemperatureValue());
            System.out.println(name + " - Min Temperature: " + minValue + "°C");
        }
    }
}

class MainSensor {
    public static void main(String[] args) {
        TemperatureSensor tsTimisoara = new TemperatureSensor("TS01", "Timisoara", 0.5f);
        TemperatureSensor tsArad = new TemperatureSensor("TS02", "Arad", 0.5f);
        HumiditySensor hsTimisoara = new HumiditySensor("HS01", "Timisoara");
        HumiditySensor hsArad = new HumiditySensor("HS02", "Arad");

        NumericDisplay numericTimisoara = new NumericDisplay("NumericDisplayTimisoara");
        NumericDisplay numericArad = new NumericDisplay("NumericDisplayArad");
        MaxValueDisplay maxTimisoara = new MaxValueDisplay("MaxValueTimisoara");
        MinValueDisplay minTimisoara = new MinValueDisplay("MinValueTimisoara");

        BasicEventImpl.instance().subscribe(TemperatureEvent.class, numericTimisoara);
        BasicEventImpl.instance().subscribe(TemperatureEvent.class, numericArad);
        BasicEventImpl.instance().subscribe(TemperatureEvent.class, minTimisoara);
        BasicEventImpl.instance().subscribe(TemperatureEvent.class, maxTimisoara);
        BasicEventImpl.instance().subscribe(HumidityEvent.class, maxTimisoara);
        BasicEventImpl.instance().subscribe(HumidityEvent.class, numericTimisoara);
        BasicEventImpl.instance().subscribe(HumidityEvent.class, numericArad);
        //add new features...

        tsTimisoara.generateTemperature();
        tsArad.generateTemperature();
        hsTimisoara.generateHumidity();
        hsArad.generateHumidity();
        //add new features...
    }
}
