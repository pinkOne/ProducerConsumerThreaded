package ua.di1.common;

import org.jsoup.nodes.Document;

public interface IHtmlPage {
    void findLinks(IHtmlPageLinkFinder linkFinder);
    int processLinks(IHtmlLinkProcessor linkProcessor); //returns count of requests that were successfully processed.

    Document downloadContent();
}
