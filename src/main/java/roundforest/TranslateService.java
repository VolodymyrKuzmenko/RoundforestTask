package roundforest;

import org.springframework.web.client.RestTemplate;
import roundforest.domain.ReviewDTO;
import roundforest.mock.MockRestTemplate;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static roundforest.domain.Languages.EN;
import static roundforest.domain.Languages.FR;

public class TranslateService {

    private static final int MAX_SIZE = 1000;
    private final String TRANSLATE_ENDPOINT = "https://api.google.com/translate";
    private RestTemplate template = new MockRestTemplate();

    public Iterable<String> translateReviews(Iterable<String> review) {
        return StreamSupport.stream(review.spliterator(), false)
                .map(this::translate)
                .collect(Collectors.toList());
    }

    private String translate(String text) {
        if (text.length() >= MAX_SIZE) {
            return splitAndTranslate(text);
        }
        return translateData(text);

    }

    private String translateData(String text) {
        return template.postForObject(
                fromHttpUrl(TRANSLATE_ENDPOINT).build().encode().toUri(),
                new ReviewDTO(EN, FR, text),
                String.class);
    }

    private String splitAndTranslate(String text) {
        String[] sentences = text.split("(?<=[a-z])\\.\\s+");
        for (String sentence : sentences){
            
        }
    }
}
