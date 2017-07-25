package roundforest;

import roundforest.domain.ReviewDTO;

import java.io.File;

import static java.util.Arrays.asList;
import static roundforest.domain.Languages.EN;
import static roundforest.domain.Languages.FR;

public class TestDataFactory {

    public static final String REVIEWS = "reviews.csv";

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

    private static File loadFromResource(String name) {
        ClassLoader classLoader = TestDataFactory.class.getClassLoader();
        return new File(classLoader.getResource(name).getFile());
    }
}
