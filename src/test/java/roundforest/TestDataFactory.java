package roundforest;

import java.io.File;

import static java.util.Arrays.asList;

public class TestDataFactory {

    public static final String REVIEWS = "reviews.csv";

    public static File getReviews() {
        return loadFromResource(REVIEWS);
    }

    public static Iterable<String> getTwoTheMostActiveUser() {
        return asList("Canadian Fan", "delmartian");
    }

    private static File loadFromResource(String name) {
        ClassLoader classLoader = TestDataFactory.class.getClassLoader();
        return new File(classLoader.getResource(name).getFile());
    }
}
