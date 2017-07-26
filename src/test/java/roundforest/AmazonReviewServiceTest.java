package roundforest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.Assert.assertEquals;
import static roundforest.TestDataFactory.*;

public class AmazonReviewServiceTest {

    public static final String testFile = "C:\\Users\\volodymyrk\\Downloads\\amazon-fine-food-reviews\\Reviews.csv";

    private AmazonReviewService service;

    @Before
    public void init() {
        service = new AmazonReviewService();
    }

    @Test
    public void shouldFindMostActiveUsers() throws IOException {
        Iterable<String> expected = getTwoTheMostActiveUser();

        Iterable<String> actual = service.findMostActiveUsers(getReviews(), 2);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindMostCommentedProduct() throws IOException {
        Iterable<String> expected = getTwoTheMostCommentedProduct();

        Iterable<String> actual = service.findMostCommentedProduct(getReviews(), 2);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindMostUsedWordInText() throws IOException {
        Iterable<String> expected = getTwoTheMostUsedWords();

        Iterable<String> actual = service.findMostUsedWords(getReviews(), 2);

        assertEquals(expected, actual);
    }

    @Test
    @Ignore
    public void shouldFindTheMostActiveUsersForManualRun() throws IOException {
        service.findMostActiveUsers(getFile(testFile), 1000);
    }

}
