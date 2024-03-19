package org.example;

public class Chapter {
    private final String title;
    private final String url;

    public Chapter(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
