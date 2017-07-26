package roundforest.executors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class TranslateServiceExecutor {

    public void executeParalellTranslation(int maxParallelRequests, File data) {

    }

    public static long getSizeOfRecords(File file) {
        try (Stream<String> s = lines(file.toPath(), Charset.defaultCharset())) {
            return s.skip(1).count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
