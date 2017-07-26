package roundforest;

import org.apache.commons.csv.CSVRecord;
import roundforest.domain.ReviewFields;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static roundforest.domain.ReviewFields.*;

public class AmazonReviewService {

    public List<String> findMostActiveUsers(File reviews, int maxItems) {
        return findMostUsingItemInFile(reviews, maxItems, PROFILE_NAME);
    }

    public List<String> findMostCommentedProduct(File reviews, int maxItems) {
        return findMostUsingItemInFile(reviews, maxItems, PRODUCT_ID);
    }

    public List<String> findMostUsedWords(File reviews, int limit) {
        Map<String, Integer> map = new HashMap<>();
        try (Reader in = new FileReader(reviews)) {
            Iterable<CSVRecord> records = DEFAULT.withHeader().parse(in);
            stream(records.spliterator(), false)
                    .map(x -> addAll(
                            x.get(TEXT).split("\\W+"),
                            x.get(SUMMARY).split("\\W+")))
                    .forEach(x -> {
                                for (String word : x) {
                                    putKeyIfNotExistOrIncrement(map, word.toLowerCase());
                                }
                            }
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortEntrySetByIntValue(map.entrySet(), limit);
    }

    private List<String> findMostUsingItemInFile(File reviews, int limit, ReviewFields field) {
        Map<String, Integer> map = new HashMap<>();
        try (Reader in = new FileReader(reviews)) {
            Iterable<CSVRecord> records = DEFAULT.withHeader().parse(in);
            stream(records.spliterator(), false)
                    .map(x -> x.get(field))
                    .forEach(x -> putKeyIfNotExistOrIncrement(map, x));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sortEntrySetByIntValue(map.entrySet(), limit);
    }

    private List<String> sortEntrySetByIntValue(Set<Map.Entry<String, Integer>> entrySet, int limit) {
        return entrySet
                .stream()
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void putKeyIfNotExistOrIncrement(Map<String, Integer> map, String key) {
        Integer count = map.get(key);
        if (count == null) {
            map.put(key, 1);
        } else {
            map.put(key, ++count);
        }
    }
}
