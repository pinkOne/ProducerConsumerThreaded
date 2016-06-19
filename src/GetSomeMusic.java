import ua.di1.common.*;
import ua.di1.consumer.LinkDownloader;
import ua.di1.producer.LinkProducer;

import java.net.MalformedURLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GetSomeMusic {


    private static BlockingQueue queue;
    private static Lock lock = new ReentrantLock(true);
    private static int queueCapacity;
    private static int pagesCount;
    private static int loadersCount;
    private static int pageLoadersThreadsCount;

    private static int processOnePage(PageData params) throws MalformedURLException {
        System.out.printf("\n\n\nProcessing page: %s\n", params.getPageUrl());

        HtmlPage htmlPage = new HtmlPage(params.getPageUrl());

        IHtmlPageLinkFinder linkFinder = new Mp3LinkFinder(htmlPage.downloadContent(), params);
        htmlPage.findLinks(linkFinder);

        IHtmlLinkProcessor linkProcessor = new Mp3LinkProcessor(params.getDestination());
        int processedLinksCount = htmlPage.processLinks(linkProcessor);
        return processedLinksCount;
    }

    public static void main(String[] args) throws InterruptedException {
        PageData pageData = new PageData(
                                    "http://zaycev.net/artist/130985?page=3", // http://zaycev.net/artist/73481?page=18");
                // http://zaycev.net/artist/510619
                                    "c:\\Media\\Music\\TNMK\\",
                                    "http://cdndl.zaycev.net/",
                                    "_(zaycev.net).mp3");

        queueCapacity = 5;
        queue = new LinkedBlockingDeque<String>(queueCapacity);


        LinkProducer producer = new LinkProducer(pageData, queue);
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

//        try {
//            while ((processOnePage(pageData) > 0)
//                    || (pageData.getPage() < 10)){
//                pageData.setPage(pageData.getPage() + 1);
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }
}
