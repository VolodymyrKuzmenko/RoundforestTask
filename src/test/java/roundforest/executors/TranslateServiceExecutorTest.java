package roundforest.executors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static roundforest.TestDataFactory.getReviews;
import static roundforest.TestDataFactory.getSizeOfRecords;
import static roundforest.executors.TranslateServiceExecutor.getSizeOfRecords;

public class TranslateServiceExecutorTest {

    @Test
    public void shouldReturnSiceOfRecords() {
        long expected = getSizeOfRecords();

        long actual = getSizeOfRecords(getReviews());

        assertEquals(expected, actual);
    }
}
