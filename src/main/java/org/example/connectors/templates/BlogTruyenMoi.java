package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;

import java.util.List;

public class BlogTruyenMoi extends Connector {
    public BlogTruyenMoi(String url) {
        super("Blogtruyen", "https://blogtruyenmoi.com/", url);
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "div#list-chapters span.title a";
        return connectorServicesImp.fetchChapters(this, cssSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "section.manga-detail h1.entry-title";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
