package roundforest;

import lombok.Setter;
import org.springframework.web.client.RestTemplate;
import roundforest.domain.Language;
import roundforest.domain.ReviewDTO;
import roundforest.mock.MockRestTemplate;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class TranslateService {

    private static final int MAX_SIZE = 1000;
    private final String TRANSLATE_ENDPOINT = "https://api.google.com/translate";

    @Setter
    private RestTemplate template = new MockRestTemplate();

    public Iterable<String> translateReviews(Iterable<String> review, Language srcLang, Language destLang) {
        return StreamSupport.stream(review.spliterator(), false)
                .map(x -> translate(x, srcLang, destLang))
                .collect(Collectors.toList());
    }

    private String translate(String text, Language srcLang, Language destLang) {
        if (text.length() >= MAX_SIZE) {
            return splitAndTranslate(text, srcLang, destLang);
        }
        return translateData(text, srcLang, destLang);

    }

    private String splitAndTranslate(String text, Language srcLang, Language destLang) {
        String[] sentences = text.split("\\.");
        StringBuilder paragraph = new StringBuilder();
        for (String sentence : sentences) {
            paragraph
                    .append(translateData(sentence, srcLang, destLang))
                    .append(".");
        }
        return paragraph.toString();
    }

    private String translateData(String text, Language srcLang, Language destLang) {
        return template.postForObject(
                fromHttpUrl(TRANSLATE_ENDPOINT).build().encode().toUri(),
                new ReviewDTO(text, srcLang, destLang),
                String.class);
    }
}
