package ua.di1.common;

public class PageData {

    private final String baseUrl;
    private String pageUrl;
    private String siteRoot;
    private String linkTail;
    private String destination;
    private int page;

    public PageData(String pageURL, String destination, String siteRoot, String linkTail) {
        this.siteRoot = siteRoot;
        this.linkTail = linkTail;
        this.destination = destination;
        this.pageUrl = pageURL;

        page = getPageFromURLString(pageURL);

        if(page == 0){
            baseUrl = pageURL;
        }else{
            baseUrl = pageURL.substring(0, pageURL.indexOf("?"));
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        pageUrl = baseUrl + "?page=" + String.valueOf(page);
    }

    public String getBaseURL() {
        return baseUrl;
    }

    public String getSiteRoot() {
        return siteRoot;
    }

    public void setSiteRoot(String siteRoot) {
        this.siteRoot = siteRoot;
    }

    public String getLinkTail() {
        return linkTail;
    }

    public void setLinkTail(String linkTail) {
        this.linkTail = linkTail;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    private static int getPageFromURLString(String pageURLString) {
        int indexOfPageParameter = pageURLString.lastIndexOf("=");
        String pageNumber = (indexOfPageParameter > 0)?
                pageURLString.substring(indexOfPageParameter +1)
                : "0";
        return Integer.valueOf(pageNumber);
    }
}
