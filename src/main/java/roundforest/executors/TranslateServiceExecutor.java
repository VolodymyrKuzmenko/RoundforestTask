package roundforest.executors;

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
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.nio.file.Files.lines;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static roundforest.domain.Language.EN;
import static roundforest.domain.Language.FR;
import static roundforest.domain.ReviewFields.TEXT;

@Slf4j
public class TranslateServiceExecutor {

    private static final List<AtomicBoolean> hasDataFlags = new ArrayList<>();
    public static final Object DONE = new Object();

    public void executeParallelTranslation(File data, int producersCount, int consumersCount) {
        ExecutorService service = Executors.newFixedThreadPool(producersCount + consumersCount);

        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        Collection<Callable<Object>> workers = new ArrayList<>();
        workers.addAll(createProducerWorkers(data, producersCount, queue));
        workers.addAll(createConsumerWorkers(consumersCount, EN, FR, queue));
        List<Future<Object>> result;
        try {
            result = service.invokeAll(workers);

            //operations to wait, until all consumers processed all data
            for (Future<Object> objectFuture : result) {
                try {
                    if (objectFuture != null) {
                        objectFuture.get();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            service.shutdown();
            while (!service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES)) ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (service.isTerminated()) {
            log.info("Translation complete; queue size:" + queue.size());
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

    private class ProducerWorker implements Callable<Object> {
        private int threadID;
        private long startIndex;
        private long endIndex;
        private File data;
        private BlockingQueue<String> queue;
        private AtomicBoolean hasData = new AtomicBoolean(true);

        @Override
        public Object call() {
            try (Reader in = new FileReader(data)) {
                log.info(String.format("Producer with id %d started", threadID));
                Iterable<CSVRecord> records = DEFAULT.withHeader().parse(in);
                StreamSupport.stream(records.spliterator(), false)
                        .skip(startIndex)
                        .limit(endIndex - startIndex)
                        .forEach(x -> {

                            //to keep queue not so big, should sleep
                            if (queue.size()>100){
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            queue.offer(x.get(TEXT));
                        });
            } catch (IOException e) {
                e.printStackTrace();
                hasData.set(false);
            }
            log.info(String.format("Producer with id %d finished", threadID));

            hasData.set(false);
            return DONE;
        }
    }

    private class ConsumerWorker implements Callable<Object> {
        private int threadID;
        private TranslateService service;
        private Language srcLang;
        private Language destLang;
        private BlockingQueue<String> queue;
        private Iterable<AtomicBoolean> hasDataFlags;
        private int count = 0;
        @Override
        public Object call() {
            log.info(String.format("Consumer with id %d started", threadID));

            //process until has data or queue is not empty
            while (hasData() || !queue.isEmpty()) {
                String result = queue.poll();
                log.info(String.format("Consumer processing, queue size is %d", queue.size()));
                if (result != null) {
                    count++;
                    service.translate(result, srcLang, destLang);
                }
            }
            log.info(String.format("Consumer with id %d finished and processed %d elements", threadID, count));
            return DONE;

        }

        private boolean hasData() {
            boolean result = false;
            for (AtomicBoolean hasData : hasDataFlags) {
                result = hasData.get() || result;
            }
            return result;
        }
    }

    private Collection<Callable<Object>> createProducerWorkers(File data, int maxThreads, BlockingQueue<String> queue) {
        Collection<Callable<Object>> workers = new ArrayList<>(maxThreads);
        long countOfRecord = getSizeOfRecords(data);
        long itemsPerThread = countOfRecord / maxThreads;
        long startIndex = 0;
        long endIndex = startIndex + itemsPerThread;
        for (int i = 0; i < maxThreads; i++) {
            ProducerWorker worker = new ProducerWorker();
            hasDataFlags.add(worker.hasData);
            worker.threadID = i;
            worker.data = data;
            worker.startIndex = startIndex;
            worker.queue = queue;

            if (i == maxThreads - 1) {
                //last worker must process all other items
                worker.endIndex = countOfRecord;
            } else {
                worker.endIndex = endIndex;
            }
            workers.add(worker);
            startIndex = endIndex;
            endIndex = endIndex + itemsPerThread;
        }
        return workers;
    }

    private Collection<Callable<Object>> createConsumerWorkers(int maxThreads, Language src, Language dest, BlockingQueue<String> queue) {
        Collection<Callable<Object>> consumers = new ArrayList<>(maxThreads);
        for (int i = 0; i < maxThreads; i++) {
            ConsumerWorker worker = new ConsumerWorker();
            worker.service = new TranslateService();
            worker.queue = queue;
            worker.srcLang = src;
            worker.destLang = dest;
            worker.hasDataFlags = hasDataFlags;
            worker.threadID = i;
            consumers.add(worker);
        }
        return consumers;
    }

}
