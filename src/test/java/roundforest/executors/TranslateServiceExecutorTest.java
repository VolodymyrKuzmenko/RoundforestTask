package roundforest.executors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.Assert.assertEquals;
import static roundforest.TestDataFactory.REAL_TEST_DATA;
import static roundforest.TestDataFactory.getReviews;
import static roundforest.TestDataFactory.getSizeOfRecords;
import static roundforest.executors.TranslateServiceExecutor.getSizeOfRecords;

public class TranslateServiceExecutorTest {

    private TranslateServiceExecutor executor;

    @Before
    public void init() {
        executor = new TranslateServiceExecutor();
    }

    @Test
    public void shouldReturnSiseOfRecords() {
        long expected = getSizeOfRecords();

        long actual = getSizeOfRecords(getReviews());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldTranslateParallel() {
        executor.executeParallelTranslation(
                getReviews(), 2, 5);
    }

    @Test
    @Ignore
    public void shouldTranslateParallelForManualRun() {
        executor.executeParallelTranslation(
                getFile(REAL_TEST_DATA), 5, 50);
    }
}
