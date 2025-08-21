import java.util.*;

interface NewsEvent {}

interface Subscriber {
    void inform(NewsEvent event);
}

interface NewsEventBus {
    void publish(NewsEvent event);
    void subscribe(Class<?> eventType, Subscriber subscriber);
    void unsubscribe(Class<?> eventType, Subscriber subscriber);
}

class NewsEventBusImpl implements NewsEventBus {
    private static NewsEventBusImpl instance = null;
    private final List<Subscription> subscriptions = new ArrayList<>();

    private static class Subscription {
        private final Class<?> eventType;
        private final Subscriber subscriber;

        public Subscription(Class<?> eventType, Subscriber subscriber) {
            this.eventType = eventType;
            this.subscriber = subscriber;
        }

        public boolean matches(NewsEvent event) {
            return eventType.isAssignableFrom(event.getClass());
        }
    }

    private NewsEventBusImpl() {}

    public static NewsEventBusImpl getInstance() {
        if (instance == null) {
            instance = new NewsEventBusImpl();
        }
        return instance;
    }

    @Override
    public void publish(NewsEvent event) {
        for (Subscription subscription : subscriptions) {
            if (subscription.matches(event)) {
                subscription.subscriber.inform(event);
            }
        }
    }

    @Override
    public void subscribe(Class<?> eventType, Subscriber subscriber) {
        subscriptions.add(new Subscription(eventType, subscriber));
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

class NewsArticle implements NewsEvent {
    private final String content;
    private final String domain;
    private final String agency;

    public NewsArticle(String agency, String domain, String content) {
        this.agency = agency;
        this.domain = domain;
        this.content = content;
    }

    public String getAgency() {
        return agency;
    }

    public String getDomain() {
        return domain;
    }

    public String getContent() {
        return content;
    }
}

class NewsAgency {
    private final String name;

    public NewsAgency(String name) {
        this.name = name;
    }

    public void publishNews(String domain, String content) {
        NewsEventBusImpl.getInstance().publish(new NewsArticle(name, domain, content));
    }
}

class Person implements Subscriber {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public void inform(NewsEvent event) {
        if (event instanceof NewsArticle) {
            NewsArticle news = (NewsArticle) event;
            System.out.println(name + " received news from " + news.getAgency() + " in " + news.getDomain() + ": " + news.getContent());
        }
    }

    public void subscribeToDomain(Class<?> eventType) {
        NewsEventBusImpl.getInstance().subscribe(eventType, this);
    }

    public void unsubscribeFromDomain(Class<?> eventType) {
        NewsEventBusImpl.getInstance().unsubscribe(eventType, this);
    }
}

class NewsSystem {
    public static void main(String[] args) {
        NewsAgency sportsAgency = new NewsAgency("Sports Today");
        NewsAgency politicsAgency = new NewsAgency("Political Review");
        NewsAgency cultureAgency = new NewsAgency("Culture Insights");

        Person alice = new Person("Alice");
        Person bob = new Person("Bob");

        alice.subscribeToDomain(NewsArticle.class);
        bob.subscribeToDomain(NewsArticle.class);

        sportsAgency.publishNews("Sports", "Big match coming up this weekend!");
        politicsAgency.publishNews("Politics", "New election laws passed today.");
        cultureAgency.publishNews("Culture", "Art exhibit opens downtown.");

        bob.unsubscribeFromDomain(NewsArticle.class);
        sportsAgency.publishNews("Sports", "Championship game results are in!");
    }
}
