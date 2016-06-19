package ua.di1.common;

import java.net.URL;

public interface IHtmlLinkProcessor {
    int process(URL url); // returns 0 if request successfully processed. -1 if skipped.
}
