package roundforest.mock;

import org.junit.Before;
import org.junit.Test;
import roundforest.TestDataFactory;
import roundforest.mock.MockRestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static roundforest.TestDataFactory.getReviewDTO;

public class MockRestTemplateTest {

    private final String TRANSLATE_ENDPOINT = "https://api.google.com/translate";
    private MockRestTemplate mock;

    @Before
    public void init() {
        mock = new MockRestTemplate();
    }

    @Test
    public void shouldReturnMockResponse() {
        String expected = TestDataFactory.getTranslatedReview();

        String actual = mock.postForObject(
                fromHttpUrl(TRANSLATE_ENDPOINT).build().encode().toUri(),
                getReviewDTO(),
                String.class);

        assertEquals(expected, actual);
    }
}
