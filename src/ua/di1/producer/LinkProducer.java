package ua.di1.producer;

import sun.nio.ch.ThreadPool;
import ua.di1.common.PageData;

import java.util.concurrent.*;

public class LinkProducer {

    private final PageData pageData;
    private final BlockingQueue queue;
    private ExecutorService executorService;
    private int pagesCount;
    private int threadsCount;

    public LinkProducer(PageData pageData, BlockingQueue queue) {
        this.pageData = pageData;
        this.queue = queue;
    }

    public void start(int pagesCount, int threadsCount) {
        this.pagesCount = pagesCount;

        executorService = Executors.newFixedThreadPool(threadsCount);
        Future[] futures = new Future[pagesCount];
        for(int i = 0; i < pagesCount; i++) {
            futures[i] = executorService.submit(new LinkProducerThread(i));
            System.out.println(String.format("Started Producer Thread id: %d. futures[i]: %s",
                                i,
                                futures[i]
            ));
        }
        executorService.shutdown();
    }

    private class LinkProducerThread implements Runnable {
        private final int id;
        public LinkProducerThread(int id) {
            this.id = id;
        }
        @Override
        public void run() {
            String value = "link" + String.valueOf(id);
            boolean inserted;
            while (!(inserted = queue.offer(value))) {
                System.out.println(String.format("Producer Thread id: %d. Queue len: %d. Queue accepted value %s: %b",
                        id,
                        queue.size(),
                        value,
                        inserted));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(String.format("Producer Thread id: %d. Queue len: %d. Queue accepted value %s: %b",
                    id,
                    queue.size(),
                    value,
                    inserted));
        }
    }
}