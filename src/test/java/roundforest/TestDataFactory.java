package roundforest;

import org.apache.commons.io.FileUtils;
import roundforest.domain.ReviewDTO;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static java.util.Arrays.asList;
import static roundforest.domain.Language.EN;
import static roundforest.domain.Language.FR;

public class TestDataFactory {

    public static final String REVIEWS = "reviews.csv";
    public static final String LARGE_REVIEW = "largeReview.txt";

    public static File getReviews() {
        return loadFromResource(REVIEWS);
    }

    public static Iterable<String> getTwoTheMostActiveUser() {
        return asList("Canadian Fan", "delmartian");
    }

    public static Iterable<String> getTwoTheMostCommentedProduct() {
        return asList("B001EO5QW8", "B001GVISJM");
    }

    public static Iterable<String> getTwoTheMostUsedWords() {
        return asList("the", "and");
    }

    public static String getTranslatedReview() {
        return "Salut Jean, comment vas tu?";
    }

    public static ReviewDTO getReviewDTO() {
        ReviewDTO dto = new ReviewDTO();
        dto.setInputLanguage(EN);
        dto.setOutputLanguages(FR);
        dto.setText("Hello John, how are you?");
        return dto;
    }

    public static Iterable<String> getSmallSentences() {
        return asList(
                "My cats have been happily eating Felidae Platinum for more than two years",
                "I have lived out of the US for over 7 yrs now, and I so miss my Twizzlers!!",
                "The candy is just red , No flavor . Just  plan and chewy .  I would never buy them again",
                "I have never been a huge coffee fan"
        );
    }

    public static Iterable<String> getTranslatedSmallSentences() {
        return asList(
                "Salut Jean, comment vas tu?",
                "Salut Jean, comment vas tu?",
                "Salut Jean, comment vas tu?",
                "Salut Jean, comment vas tu?"
        );
    }

    public static Iterable<String> getLargeSentence() {
        try {
            return asList(FileUtils.readFileToString(loadFromResource(LARGE_REVIEW), "UTF-8")
            );
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static File loadFromResource(String name) {
        ClassLoader classLoader = TestDataFactory.class.getClassLoader();
        return new File(classLoader.getResource(name).getFile());
    }
}
