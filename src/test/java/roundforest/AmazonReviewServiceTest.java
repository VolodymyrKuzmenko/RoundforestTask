package roundforest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.Assert.assertEquals;
import static roundforest.TestDataFactory.getReviews;
import static roundforest.TestDataFactory.getTwoTheMostActiveUser;

public class AmazonReviewServiceTest {

    public static final String testFile = "C:\\Users\\Vova\\Downloads\\amazon-fine-food-reviews\\Reviews.csv";

    private AmazonReviewService service;

    @Before
    public void init() {
        service = new AmazonReviewService();
    }

    @Test
    public void shouldFindTheMostActiveUsers() throws IOException {
        Iterable<String> expected = getTwoTheMostActiveUser();

        Iterable<String> actual = service.findMostActiveUsers(getReviews(), 2);

        assertEquals(expected, actual);
    }

    @Test
    @Ignore
    public void shouldFindTheMostActiveUsersForManualRun() throws IOException {
        service.findMostActiveUsers(getFile(testFile), 1000);
    }

}
