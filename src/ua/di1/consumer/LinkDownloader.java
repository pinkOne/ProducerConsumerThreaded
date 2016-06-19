package ua.di1.consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinkDownloader {
    private final BlockingQueue queue;
    private int loadersCount;
    private ExecutorService executorService;

    public LinkDownloader(BlockingQueue queue) {
        this.queue = queue;
    }

    public void start(int loadersCount) {
        this.loadersCount = loadersCount;
        executorService = Executors.newFixedThreadPool(loadersCount);
        for(int i = 0; i < loadersCount; i++) {
            executorService.submit(new LinkDownloaderThread(i));
        }
        executorService.shutdown();
    }

    private class LinkDownloaderThread implements Runnable {
        private final int id;
        public LinkDownloaderThread(int id) {
            this.id = id;
        }
        @Override
        public void run() {
            System.out.println(String.format("Loader Thread id: %d. Queue len: %d", id, queue.size()));
            try {
                String queueElement = (String) queue.take();
                System.out.println(String.format("Loader Thread id: %d. Queue len: %d. Got value: %s.",
                                                    id,
                                                    queue.size(),
                                                    queueElement));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
