package roundforest;

import roundforest.executors.TranslateServiceExecutor;

import java.io.File;
import java.io.IOException;

import static roundforest.Utils.ALPHABETICAL_ORDER;

/**
 * Must provide path to Reviews.csv file
 */
public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            return;
        }
        File data = new File(args[0]);
        AmazonReviewService service = new AmazonReviewService();

        Thread t1 = new Thread(firstTask(data, service));
        Thread t2 = new Thread(secondTask(data, service));
        Thread t3 = new Thread(thirdTask(data, service));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        if (args.length == 2 && "translate=true".equals(args[1])) {
            TranslateServiceExecutor executor = new TranslateServiceExecutor();
            executor.executeParallelTranslation(data, 2, 5);
        }
    }


    public static Runnable firstTask(File data, AmazonReviewService service) {
        return () -> service.findMostCommentedProduct(data, 1000)
                .stream()
                .sorted(ALPHABETICAL_ORDER)
                .forEachOrdered(System.out::println);
    }

    public static Runnable secondTask(File data, AmazonReviewService service) {
        return () -> service.findMostActiveUsers(data, 1000)
                .stream()
                .sorted(ALPHABETICAL_ORDER)
                .forEachOrdered(System.out::println);
    }

    public static Runnable thirdTask(File data, AmazonReviewService service) {
        return () -> service.findMostUsedWords(data, 1000)
                .stream()
                .sorted(ALPHABETICAL_ORDER)
                .forEachOrdered(System.out::println);
    }
}
