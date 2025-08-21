/*
-> handling users reviews of the products of online store
-> incoming messages simulated by lines of text
-> contains tokens separated by comma
-> Format: productname, reviewtext, attachment(substring mocks a picture)
-> assume all lines have valid format
-> elliminate messages from users who have not bought the reviewed product (username is not found in a list of buyers of the product)
-> elliminate messages that contain profanities in the review text (mock implementation: reviewtext substring does not contain @#$%)
-> elliminate messages that contain political propaganda in the review text (mock implementation: reviewtext does not contain +++ or ---)
-> resize pictures in attachment if they are too large(mock implementation: if the attachment substring representing the attached picture contains uppercase letters, transform them in lowercase).
-> remove competitor website links from the text of the message (mock implementation: elliminate substring http from review text)
-> analyze sentiment of the review (positive, negative or neutral) and attach a classification (mock implementation: reviewtext contains more uppercase letters or more lowercase letters determine pozitive or negative sentiment. Append a +, - or = sign at the end of the reviewtext)


Input:
John, Laptop, ok, PICTURE
Mary, Phone, @#$%), IMAGE
Peter, Phone, GREAT, ManyPictures
Ann, Book, So GOOD, Image

The output will be:
John, Laptop, ok-, picture
Ann, Book, So GOOD+, image
*/

//----------PARALLEL IMPLEMENTATION----------
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class Review
{
    String reviewerName;
    String productName;
    String reviewerText;
    String attach;
    public static final Review RED_FLAG = new Review("", "", "", "");

    public Review(String reviewerName, String productName, String reviewerText, String attach)
    {
        this.reviewerName = reviewerName;
        this.productName = productName;
        this.reviewerText = reviewerText;
        this.attach = attach;
    }

    public String toString()
    {
        return reviewerName + " " + productName + " " + reviewerText + " " + attach;
    }
}


interface Filters extends Runnable {}

class ProfanityFilter implements Filters
{
    private final BlockingQueue<Review> inputQueue;
    private final BlockingQueue<Review> outputQueue;

    public ProfanityFilter(BlockingQueue<Review> inputQueue, BlockingQueue<Review> outputQueue)
    {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    @Override
    public void run()
    {
        try
        {
            while(true) 
            {
                Review review = inputQueue.take();
                if(review == Review.RED_FLAG)
                {
                    outputQueue.put(Review.RED_FLAG);
                    break;
                }
                if(!review.reviewerText.contains("@#$%)") && !review.reviewerText.contains("http"))
                {
                    outputQueue.put(review);
                }
            }
        }catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}

//Validate buyer name - product
/*
Here we can have the following situations:
1.UserName cannot exists => UNKNOWN BUYER
2.ProductName cannot exists  => NOT CERTIFIED BUYER
*/

class CertifiedBuyerFilter implements Filters {
    private final BlockingQueue<Review> inputQueue;
    private final BlockingQueue<Review> outputQueue;

    public CertifiedBuyerFilter(BlockingQueue<Review> inputQueue, BlockingQueue<Review> outputQueue) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Review review = inputQueue.take();
                if (review == Review.RED_FLAG) { // Forward termination signal
                    outputQueue.put(Review.RED_FLAG);
                    break;
                }
                if (!review.reviewerName.isEmpty() && !review.productName.isEmpty()) {
                    outputQueue.put(review); // Correctly pass valid reviews
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


// Buyer Verification Against Database
class ValidateBuyerFilter implements Filters {
    private final BlockingQueue<Review> inputQueue;
    private final BlockingQueue<Review> outputQueue;
    private final Map<String, String> buyerRecords;

    public ValidateBuyerFilter(BlockingQueue<Review> inputQueue, BlockingQueue<Review> outputQueue, Map<String, String> buyerRecords) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.buyerRecords = buyerRecords;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Review review = inputQueue.take();
                if (review == Review.RED_FLAG) {  // Ensure termination signal propagates
                    outputQueue.put(Review.RED_FLAG);
                    break;
                }
                
                String productInDatabase = buyerRecords.get(review.reviewerName);
                if (productInDatabase == null || !productInDatabase.equals(review.productName)) {
                    System.out.println("The buyer: " + review.reviewerName + " did not purchase: " + review.productName);
                } else {
                    outputQueue.put(review); // Forward only valid reviews
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


// Image Resize Filter
/*
Here we can have the following situations:
1.All attachements ARE NOT UPPERCASE LETTERS => meaning that NOT ATTACHMENT GOOD OR NOT PROPER
2.We can have pictures given of form: PICTURE OR IMAGE
3.Ignore everything else
*/

// Image Resizing Filter (Only keeps "IMAGE" or "PICTURE" and makes them lowercase)
class ImageFilter implements Filters {
    private final BlockingQueue<Review> inputQueue;
    private final BlockingQueue<Review> outputQueue;

    public ImageFilter(BlockingQueue<Review> inputQueue, BlockingQueue<Review> outputQueue)
    {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                Review review = inputQueue.take();
                if(review == Review.RED_FLAG)
                {
                    outputQueue.put(Review.RED_FLAG);
                    break;
                }
                if(review.attach.contains("IMAGE") || review.attach.contains("PICTURE"))
                {
                    review.attach = review.attach.toLowerCase();
                    outputQueue.put(review);
                }
            }
        }catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}


//Sentiment Analysis Filter
/*
Here we can have the following situations:
1.We have a situation when in the string are word with UPPERCASE LETTERS => Positive messages: +
2.We have a situation when in the string are only LOWERCASE LETTERS => Negative messages: -
3.We have a situation when the number of LOWERCASE LETTERS are equals > UPPERCASE LETTERS but != 0 => Neutral messages: =

*/

class SentimentFilter implements Filters {
    private final BlockingQueue<Review> inputQueue;
    private final BlockingQueue<Review> outputQueue;

    public SentimentFilter(BlockingQueue<Review> inputQueue, BlockingQueue<Review> outputQueue) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Review review = inputQueue.take();
                if (review == Review.RED_FLAG)
                {
                    outputQueue.put(Review.RED_FLAG);
                    break;
                }
                long upperCaseCount = review.reviewerText.chars().filter(Character::isUpperCase).count();
                long lowerCaseCount = review.reviewerText.chars().filter(Character::isLowerCase).count();

                if (lowerCaseCount > upperCaseCount) {
                    review.reviewerText = review.reviewerText.concat("-");
                } else if (upperCaseCount > lowerCaseCount) {
                    review.reviewerText = review.reviewerText.concat("+");
                } else if (upperCaseCount == lowerCaseCount && upperCaseCount > 0) {
                    review.reviewerText = review.reviewerText.concat("=");
                }

                outputQueue.put(review);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


// Buyer Database Loader
class BuyerRecordsLoader {
    public static Map<String, String> loadBuyerRecords(String filename) {
        Map<String, String> buyerRecords = new HashMap<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    buyerRecords.put(parts[0], parts[1]); // Name -> Product
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading buyer records: " + e.getMessage());
        }
        return buyerRecords;
    }
}

// Main Entry Point
class PassivePipesFilters {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter max number of filters to apply: ");
        int maxFilters = scanner.nextInt();
        scanner.nextLine();

        Map<String, String> buyerRecords = BuyerRecordsLoader.loadBuyerRecords("database.txt");

        BlockingQueue<Review> inputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Review> queue1 = new LinkedBlockingQueue<>();
        BlockingQueue<Review> queue2 = new LinkedBlockingQueue<>();
        BlockingQueue<Review> queue3 = new LinkedBlockingQueue<>();
        BlockingQueue<Review> queue4 = new LinkedBlockingQueue<>();
        BlockingQueue<Review> outputQueue = new LinkedBlockingQueue<>();

        List<Filters> filters = Arrays.asList(
            new ProfanityFilter(inputQueue, queue1),
            new CertifiedBuyerFilter(queue1, queue2),
            new ValidateBuyerFilter(queue2, queue3, buyerRecords),
            new ImageFilter(queue3, queue4),
            new SentimentFilter(queue4, outputQueue)
        );

        if(maxFilters > filters.size())
        {
            System.out.println("Error, not enough filters, only 5 disposable!");
            System.exit(1);
        }

        for(Filters filter : filters)
        {
            new Thread(filter).start();
        }

        try (BufferedReader reviewBuffer = new BufferedReader(new FileReader("reviews.txt"))) {
            String line;
            while ((line = reviewBuffer.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4) {
                    inputQueue.put(new Review(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            inputQueue.put(Review.RED_FLAG);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (true) {
            try {
                Review processedReview = outputQueue.take();
                if (processedReview == Review.RED_FLAG) break;
                System.out.println(processedReview);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
/*
This is a parallel version with ExecutorService and ThreadPool which has Runnable interface implemented and Callable<Feature> implemented.
Which assigns Threads from PoolThread to each of the filter interconnect through a interface Filters which has a method Overriden by each filter
It has the work done of filtering dataflow by the class ReviewProcessor which activates each filter, which is not a filter type, but declared in main
BuyerRecordsLoader which is not either a filtering class, but does some processing by reading from database.txt
Main Entry point describes adding filters to a List connecting them with the respectives Pipes and iterating over with a simple for loop
Also the Entry Point has the other classes declared there and got use of each one in MAIN, even though are not filters
I considered Filters only those Classes with the assignment of filtering the dataflow.
I know also that the main program with reading from reviews.txt and writing into the stdout could be considered filters and implementing Filters interface.
I only wanted to keep the essentials filter without adding new methods in Filters inteface and then overriding them in the two Classes unimplemented for reading/writing

Parallelization is with ExecutorService with provides returning values and fixed number of threads where I can pull of from.
Also by this implementation I managed to check also exceptions, because ExecutorService provides Exception Handling
*/




