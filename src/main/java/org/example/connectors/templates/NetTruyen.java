package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import java.util.List;

public class NetTruyen extends Connector {
    public NetTruyen() {
        setLabel("Nettruyen");
        setDomain("https://nettruyenx.com/");
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "div.list-chapter ul li.row div.chapter a";
        return connectorServicesImp.fetchChapters(this, cssSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "h1.title-detail";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
