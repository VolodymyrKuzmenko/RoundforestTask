package roundforest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static roundforest.TestDataFactory.getSmallSentences;
import static roundforest.TestDataFactory.getTranslatedSmallSentenses;

public class TranslateServiceTest {

    private TranslateService service;

    @Before
    public void init() {
        service = new TranslateService();
    }

    @Test
    public void shouldTranslateSmallText() {
        Iterable<String> expected = getTranslatedSmallSentenses();

        Iterable<String> actual = service.translateReviews(getSmallSentences());

        assertEquals(expected, actual);
    }
}
