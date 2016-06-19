package ua.di1.common;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


public class Mp3LinkFinder implements IHtmlPageLinkFinder {
    private Document htmlPage;
    private List<URL> mp3Links;
    private String singerName;
    private int dataPage;
    private int dataPages;
    private String singerID;
    private String nextPageLinkPattern;
    private StringBuilder link;
    private PageData staticData;

    public Mp3LinkFinder(Document htmlPage, PageData staticData) {
        this.htmlPage = htmlPage;
        this.staticData = staticData;
        mp3Links = new LinkedList<>();
    }

    private String formatLinkPart(String part){
        return part.toLowerCase().replaceAll("[\"' ]", "_");
    }

    private List<URL> gatherLinks(Elements tags){
        List<URL> links = new LinkedList<>();
        for( Element tag : tags){
            System.out.println("tag = " + tag);

            Element mp3 = tag.select("a").first();
            String href = mp3.attr("href");
            // get song id
            String songId = retrieveSongId(href);
            if((songId == null) || (songId.length() == 0)){
                System.out.println("FAILED item on songId find: tag = " + tag);
                continue;
            }

            String songName = retrieveSongName(mp3);
            if((songName == null) || (songName.length() == 0)){
                System.out.println("FAILED item on songName find: tag = " + tag);
                continue;
            }

            link = new StringBuilder(staticData.getSiteRoot());
            link.append(singerID)
                    .append('/')
                    .append(songId)
                    .append('/')
                    .append(formatLinkPart(singerName))
                    .append("_-_")
                    .append(formatLinkPart(songName))
                    .append(staticData.getLinkTail());
            System.out.println("link = " + link);
            try {
                links.add(new URL(link.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return links;
    }

    private String retrieveSongName(Element element) {
        String songName = element.select("div.musicset-track__track-name").text();
        if(songName != null && songName.length() > 0) return songName;

        songName = element.text();
        if(songName != null && songName.length() > 0) return songName;

        return "";
    }

    private String retrieveSongId(String inputText) {
        int startIndex = inputText.lastIndexOf('/');
        int endIndex = inputText.lastIndexOf('.');
        if((startIndex >=0) && (endIndex >=0)
                && (endIndex > startIndex)) {
            return inputText.substring(startIndex+1, endIndex);
        }
        return "";
    }

    @Override
    public List<URL> find() {

/*
        Elements els = htmlPage.getAllElements();
        els.forEach(el->{
            if(el.outerHtml().contains("page="))
                System.out.printf("\n\n\n >>>>>>>>>>>>>>> %s", el.outerHtml());
        });
*/

        Elements artistName = htmlPage.select("meta[itemprop=\"name\"]");
        singerName = artistName.attr("content");
        System.out.println("singerName = " + singerName);

        Element artistInfo = htmlPage.select("div[^data-page]").first();
        //htmlPage.select("div#artist-info");
        String dataPage = artistInfo.attr("data-page");
        this.dataPage = Integer.valueOf(dataPage);
        System.out.println("dataPage = " + dataPage);
        dataPages = Integer.valueOf(artistInfo.attr("data-pages"));
        System.out.println("dataPages = " + dataPages);
        singerID = getSingerID(htmlPage);
        System.out.println("singerID = " + singerID);

        //TODO process sub pages. Behold looping.
        nextPageLinkPattern = artistInfo.attr("data-next-page");

        Elements tags = htmlPage.select("div.musicset-track-list__items > div.musicset-track.clearfix"
                + " > div.musicset-track__title.track-geo__title"
                //+ " >
                + " a.musicset-track__link")
                .not("[href*=Mylene]");

        System.out.println("tags.size() = " + tags.size());
        tags.forEach(System.out::println);

        List links = gatherLinks(tags);
        System.out.println("links.size() = " + links.size());
        mp3Links.addAll(links);

        return mp3Links;
    }

    private String getSingerID(Document htmlPage) {
        Elements baseEl = htmlPage.select("link[itemprop]");
        String base;
        base = baseEl.attr("href");
        int slashIndex = base.lastIndexOf("/");
        return base.substring(slashIndex+1);
    }
}
