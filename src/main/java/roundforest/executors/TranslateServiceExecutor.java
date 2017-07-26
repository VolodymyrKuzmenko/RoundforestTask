package roundforest.executors;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import roundforest.TranslateService;
import roundforest.domain.Language;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.nio.file.Files.lines;
import static java.util.concurrent.Executors.callable;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static roundforest.domain.Language.EN;
import static roundforest.domain.Language.FR;
import static roundforest.domain.ReviewFields.TEXT;

@Slf4j
public class TranslateServiceExecutor {

    public void executeParallelTranslation(File data, int maxParallelReuests) {
        ExecutorService service = Executors.newCachedThreadPool();
        Collection<Callable<Object>> workers = createWorkers(data, EN, FR, maxParallelReuests);
        try {
            service.invokeAll(workers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long getSizeOfRecords(File file) {
        try (Stream<String> s = lines(file.toPath(), Charset.defaultCharset())) {
            return s.skip(1).count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Setter
    private class Worker implements Runnable {
        private int threadID;
        private long startIndex;
        private long endIndex;
        private TranslateService service;
        private File data;
        private Language srcLang;
        private Language destLang;

        @Override
        public void run() {
            try (Reader in = new FileReader(data)) {
                log.info(format("Thread with id %d started", threadID));
                Iterable<CSVRecord> records = DEFAULT.withHeader().parse(in);
                StreamSupport.stream(records.spliterator(), false)
                        .skip(startIndex)
                        .limit(endIndex - startIndex)
                        .forEach(x ->
                                service.translate(
                                        x.get(TEXT),
                                        srcLang,
                                        destLang));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Collection<Callable<Object>> createWorkers(File data, Language src, Language dest, int maxThreads) {
        Collection<Callable<Object>> workers = new ArrayList<>(maxThreads);
        long countOfRecord = getSizeOfRecords(data);
        long itemsPerThread = countOfRecord / maxThreads;
        long startIndex = 1;
        long endIndex = startIndex + itemsPerThread;
        for (int i = 0; i < maxThreads; i++) {
            Worker worker = new Worker();
            worker.setThreadID(i);
            worker.setData(data);
            worker.setSrcLang(src);
            worker.setDestLang(dest);
            worker.setService(new TranslateService());
            worker.setStartIndex(startIndex);

            if (i == maxThreads - 1) {
                //last worker must process all other items
                worker.setEndIndex(countOfRecord);
            } else {
                worker.setEndIndex(endIndex);
            }
            workers.add(callable(worker));
            startIndex = endIndex;
            endIndex = endIndex + itemsPerThread;
        }
        return workers;
    }

}
