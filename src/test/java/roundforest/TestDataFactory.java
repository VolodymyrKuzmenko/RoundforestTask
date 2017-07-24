package roundforest;

import java.io.File;

public class TestDataFactory {

    public static final String REVIEWS = "reviews.csv";

    public static File getReviews() {
        return loadFromResource(REVIEWS);
    }

    private static File loadFromResource(String name) {
        ClassLoader classLoader = TestDataFactory.class.getClassLoader();
        return new File(classLoader.getResource(name).getFile());
    }
}
