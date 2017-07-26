package roundforest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static roundforest.TestDataFactory.*;
import static roundforest.domain.Language.EN;
import static roundforest.domain.Language.FR;

public class TranslateServiceTest {

    private TranslateService service;

    @Before
    public void init() {
        service = new TranslateService();
    }

    @Test
    public void shouldTranslateSmallText() {
        Iterable<String> expected = getTranslatedSmallSentences();

        Iterable<String> actual = service.translateReviews(getSmallSentences(), EN, FR);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCallTranslateForLargeTextMoreThanOneTime() {
        RestTemplate template = mock(RestTemplate.class);
        service.setTemplate(template);
        when(template.postForObject(any(), any(), any())).thenReturn("response");


        service.translateReviews(getLargeSentence(), EN, FR);

        verify(template, atLeast(2)).postForObject(any(), any(), any());
    }
}
