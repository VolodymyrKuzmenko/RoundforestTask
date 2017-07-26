package roundforest;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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

    public String translate(String text, Language srcLang, Language destLang) {
        if (text == null) {
            return StringUtils.EMPTY;
        }
        if (text.length() >= MAX_SIZE) {
            return splitAndTranslate(text, srcLang, destLang);
        }
        return translateData(text, srcLang, destLang);
    }

    private String splitAndTranslate(String text, Language srcLang, Language destLang) {
        int indexOfValidPArt = searchIndexValidParagraph(text, MAX_SIZE);

        String validParagraph = text.substring(0, indexOfValidPArt);
        String otherPart = text.substring(indexOfValidPArt + 1);

        StringBuilder paragraph = new StringBuilder();
        paragraph
                //can process valid paragraph
                .append(translateData(validParagraph, srcLang, destLang))
                //may be large, must processed from the begin
                .append(translate(otherPart, srcLang, destLang));
        return paragraph.toString();
    }

    private String translateData(String text, Language srcLang, Language destLang) {
        return template.postForObject(
                fromHttpUrl(TRANSLATE_ENDPOINT).build().encode().toUri(),
                new ReviewDTO(text, srcLang, destLang),
                String.class);
    }

    private int searchIndexValidParagraph(String paragraph, int limit) {
        int searchIndex = 0;
        int lastFound = 0;
        while (searchIndex < limit) {
            lastFound = searchIndex;
            searchIndex = paragraph.indexOf(".", ++searchIndex);
            if (searchIndex == -1) {
                break;
            }
        }
        return lastFound;
    }
}
