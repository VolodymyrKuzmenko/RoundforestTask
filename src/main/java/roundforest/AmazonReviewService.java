package roundforest;

import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static roundforest.ReviewFields.PROFILE_NAME;

public class AmazonReviewService {

    public Iterable<String> findMostActiveUsers(File reviews, int maxItems) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        Reader in = new FileReader(reviews);
        Iterable<CSVRecord> records = DEFAULT.withHeader().parse(in);
        StreamSupport.stream(records.spliterator(), false)
                .map(x -> x.get(PROFILE_NAME))
                .forEach(x -> {
                    Integer count = map.get(x);
                    if (count == null) {
                        map.put(x, 1);
                    } else {
                        map.put(x, ++count);
                    }
                });

        return map.entrySet()
                .stream()
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .limit(maxItems)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
