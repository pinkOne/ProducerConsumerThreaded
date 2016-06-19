package ua.di1.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HtmlPage implements IHtmlPage {
    private final URL url;
    private List <URL> mp3Links;
    private Document htmlPage = null;

    public HtmlPage(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    @Override
    public void findLinks(IHtmlPageLinkFinder linkFinder) {
        this.mp3Links = linkFinder.find();
    }

    @Override
    public int processLinks(IHtmlLinkProcessor linkProcessor) {
        int processedCount = 0;
        if(mp3Links != null) {
            for(URL link : mp3Links){
                if(linkProcessor.process(link) == 0) processedCount++;
            }
        }
        return processedCount;
    }

    @Override
    public Document downloadContent() {
        try {
            //TODO switch to enet
            htmlPage = Jsoup.connect(url.toString()).get();
            /*String fileLocation = "c:\\prg\\java\\GetSomeMusic\\docs\\";
            File file = new File(fileLocation.concat("MyleneFarmer3.htm"));
            htmlPage = Jsoup.parse(file,"UTF8");*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlPage;
    }
/*
    public void downloadCharContent() {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null){
                content.append(line);
            }
            System.out.println("content = " + content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
