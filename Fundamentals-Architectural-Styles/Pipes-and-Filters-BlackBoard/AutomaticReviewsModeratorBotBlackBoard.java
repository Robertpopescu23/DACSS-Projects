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

//-----SERIAL IMPLEMENTATION-----
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/*
In the blackboard implementation we can have the following components:
1.The BOARD(buffer type), where we can store all the data filtered that goes through it.
2.The ELEMENTS(classes with their corresponding methods), for filtering the flow of data.
3.Message Input, Message Output Elements that goes first, respectively the last.
4.Within elements in the code: -> a while infinite loop that iterates over all the ELEMENTS(FILTERS)
                               -> FILTERS don't have an order in which the data is transformed, so I have to implement it with a SWITCH case
                               -> ORDER OF ELEMENTS: FIRST => CHECK PROFANITIES/CHECK BUYER  SECOND => RESIZE IMAGE/SENTIMENT DETECTION
*/

/*-----BLACK BOARD PATTERN-----
1.Define Solution Space(a class with multiple methods/constructor => buffer)
2.Divide Solution Space into steps: -> Filter Profanities
                                    -> Certified Buyer Filter
                                    -> Validate Buyer Fitler
                                    -> Resize Image Filter
                                    -> Sentiment Filter
3.Define Black Board Vocabulary(Buffer type: Queue, Blocking Queue for parallelization, List, ArrayList)
4.Specify Control System(INSPECTION + SWITCH to give the order of the ELEMENTS + INFINITE LOOP => ACTIVE PIPES)
*/

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

/*
Solution Space is the shared buffer of the BlackBoard 
Has some methods implementation for futher going the buffer through all the filters
The buffer will always update the old information into new information.

old_information -> empty_buffer -> add_new_information
*/

class SolutionSpace
{
    private final BlockingQueue<Review> queue;
    private final List<Thread> filterThreads = new ArrayList<>();

    public SolutionSpace()
    {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void reviewPut(Review review) throws InterruptedException
    {
        queue.put(review);
    }

    public Review reviewGet() throws InterruptedException
    {
        return queue.take();
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    public void clearBuffer()
    {
        queue.clear();
    }

    public void addFilter(KnowledgeSource filter)
    {
        Thread filterThread = new Thread(filter);
        filterThreads.add(filterThread);
        filterThread.start();
    }

    public void waitForCompletion()
    {
        for(Thread thread : filterThreads)
        {
            try
            {
                thread.join();
            }catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }


    public void printResults()
    {
        waitForCompletion();

        List<Review> results = new ArrayList<>();

        queue.drainTo(results);

        for(Review processedReview : results)
        {
            if(processedReview != Review.RED_FLAG)
            {
                System.out.println(processedReview);
            }
        }
    }

}

/*
The KnowledgeSource is the Control System of the BlackBoard implementation
It iterates in an infinite while loop(while(true)).
It executes each method of each subtask from the BlackBoard(each class added).
It executes with threads for each subtask in the BlackBoard

Loop iteration forever -> next_source_filter -> executes_with_threads
*/

abstract class KnowledgeSource implements Runnable {
    protected SolutionSpace sharedBuffer;

    public KnowledgeSource(SolutionSpace sharedBuffer)
    {
        this.sharedBuffer = sharedBuffer;
    }

    public abstract void processReview(Review review);

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Review processingReview = sharedBuffer.reviewGet();
                if(processingReview == Review.RED_FLAG)
                {
                    sharedBuffer.reviewPut(processingReview);
                    break;
                }
                processReview(processingReview);
            }catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

class BuyerRecordsLoader
{
    public static Map<String, String> loadBuyerRecords(String filename)
    {
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

/*
It has a method for filtering the data read from file.
It will be inspected by the BlackBoard.
It will be executed + Threads by Control System(KnowledgeSource)
*/

class ProfanityFilter extends KnowledgeSource {

    public ProfanityFilter(SolutionSpace sharedBuffer) {
        super(sharedBuffer);
    }

    @Override
    public void processReview(Review review) {

        if(review == Review.RED_FLAG) return;

        if (!review.reviewerText.contains("@#$%)") && !review.reviewerText.contains("http")) {
            try {
                sharedBuffer.reviewPut(review);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}


/*
It has a method for filtering the data read from file.
It will be inspected by the BlackBoard.
It will be executed + Threads by Control System(KnowledgeSource)
*/

class CertifiedBuyerFilter extends KnowledgeSource
{

    public CertifiedBuyerFilter(SolutionSpace sharedBuffer)
    {
        super(sharedBuffer);
    }

    @Override
    public void processReview(Review review)
    {
        if(review == Review.RED_FLAG) return;

        if(!review.reviewerName.isEmpty() && !review.productName.isEmpty())
        {
            try{
            sharedBuffer.reviewPut(review);
            }catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}


/*
It has a method for filtering the data read from file.
It will be inspected by the BlackBoard.
It will be executed + Threads by Control System(KnowledgeSource)
*/

class ValidateBuyerFilter extends KnowledgeSource
{

    private final Map<String, String> buyerRecords;

    public ValidateBuyerFilter(SolutionSpace sharedBuffer, Map<String, String> buyerRecords)
    {
        super(sharedBuffer);
        this.buyerRecords = buyerRecords;
    }

    @Override
    public void processReview(Review review)
    {
        if(review == Review.RED_FLAG) return;

        String productInDatabase = buyerRecords.get(review.reviewerName);
        if (productInDatabase == null || !productInDatabase.equals(review.productName))
        {
            System.out.println("The buyer: " + review.reviewerName + " did not purchase: " + review.productName);
        }
        else
        {
            try
            {
                sharedBuffer.reviewPut(review);
            }catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}


/*
It has a method for filtering the data read from file.
It will be inspected by the BlackBoard.
It will be executed + Threads by Control System(KnowledgeSource)
*/

class ImageFilter extends KnowledgeSource
{
    public ImageFilter(SolutionSpace sharedBuffer)
    {
        super(sharedBuffer);
    }

    @Override
    public void processReview(Review review)
    {
        if(review == Review.RED_FLAG) return;

        if(review.attach.contains("IMAGE") || review.attach.contains("PICTURE"))
        {
            review.attach = review.attach.toLowerCase();
            try{
            sharedBuffer.reviewPut(review);
            }catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}


/*
It has a method for filtering the data read from file.
It will be inspected by the BlackBoard.
It will be executed + Threads by Control System(KnowledgeSource)
*/

class SentimentFilter extends KnowledgeSource
{

    public SentimentFilter(SolutionSpace sharedBuffer)
    {
        super(sharedBuffer);
    }

    @Override
    public void processReview(Review review)
    {
        if(review == Review.RED_FLAG) return;

        long upperCaseCount = review.reviewerText.chars().filter(Character::isUpperCase).count();
        long lowerCaseCount = review.reviewerText.chars().filter(Character::isLowerCase).count();

        if(upperCaseCount > lowerCaseCount)
        {
            review.reviewerText = review.reviewerText.concat("+");
        }
        else if(lowerCaseCount > upperCaseCount)
        {
            review.reviewerText = review.reviewerText.concat("-");
        }
        else if(upperCaseCount == lowerCaseCount && upperCaseCount > 0)
        {
            review.reviewerText = review.reviewerText.concat("=");
        }

        try
        {
            sharedBuffer.reviewPut(review);
        }catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

}

/*
It will have a method to inspect each subtask(class) within it
It will put conditions to establish the order of each subtask
It will update the BlackBoard buffer after dataFlow filtered
All of these tasks will be implemented in one method or two at least
*/

/*
Here in the activepipesfilters is the entry point where all blackboard implementation is added
Also here is the extracting data from the file, with corresponding implementation.
Also here is the printing dataFlow filtered into the stdout
*/

class ActivePipesFilters
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the max number of filters: ");
        int maxFilters = scanner.nextInt();
        scanner.nextLine();


        Map<String, String> buyerRecords = BuyerRecordsLoader.loadBuyerRecords("database.txt");
        SolutionSpace sharedBuffer = new SolutionSpace();

        try (BufferedReader reviewBuffer = new BufferedReader(new FileReader("reviews.txt"))) {
            String line;
            while ((line = reviewBuffer.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4) {
                    sharedBuffer.reviewPut(new Review(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            sharedBuffer.reviewPut(Review.RED_FLAG);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sharedBuffer.addFilter(new CertifiedBuyerFilter(sharedBuffer));
        sharedBuffer.addFilter(new ValidateBuyerFilter(sharedBuffer, buyerRecords));
        sharedBuffer.addFilter(new ProfanityFilter(sharedBuffer));
        sharedBuffer.addFilter(new ImageFilter(sharedBuffer));
        sharedBuffer.addFilter(new SentimentFilter(sharedBuffer));

        sharedBuffer.printResults();
    }
}

