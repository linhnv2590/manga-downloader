package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import java.util.List;

public class Truyenqq extends Connector {
    public Truyenqq(String url) {
        super("Truyenqq", "https://truyenqqvn.com/", url);
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "div.name-chap a";
        return connectorServicesImp.fetchChapters(this, cssSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "h1[itemprop=\"name\"]";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
