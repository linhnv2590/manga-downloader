package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import java.util.List;

public class TruyenqqVn extends Connector {
    public TruyenqqVn() {
        setLabel("Truyenqq");
        setDomain("https://truyenqqvn.vn/");
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "div.list_chapter ul li a";
        return connectorServicesImp.fetchChapters(this, cssSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "h1[itemprop=\"name\"]";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
