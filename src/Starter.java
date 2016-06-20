import ua.di1.common.*;
import ua.di1.consumer.LinkDownloader;
import ua.di1.producer.LinkProducer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Starter {


    private static BlockingQueue queue;
    private static Lock lock = new ReentrantLock(true);
    private static int queueCapacity;
    private static int pagesCount;
    private static int loadersCount;
    private static int pageLoadersThreadsCount;

    public static void main(String[] args) throws InterruptedException {
        queueCapacity = 5;
        queue = new LinkedBlockingDeque<String>(queueCapacity);


        LinkProducer producer = new LinkProducer(queue);
        pagesCount = 10;
        pageLoadersThreadsCount = 3;
        producer.start(pagesCount, pageLoadersThreadsCount);

        //System.out.println(queue.toString());

        while (queue.size() == 0) {
            int delay = 10;
//            System.out.println(String.format("Main thread is sleeping for %d millis waiting for items in the queue=",
//                    delay,  queue.toString()));
            Thread.sleep(delay);
        }
//        System.out.println(String.format("Main thread is awaken. queue=", queue.toString()));

        LinkDownloader loader = new LinkDownloader(queue);
        loadersCount = 10;
        loader.start(loadersCount);

    }
}
