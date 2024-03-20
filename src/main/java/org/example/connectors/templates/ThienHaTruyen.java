package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import org.example.utils.ChromeDriverUtils;
import java.util.List;

public class ThienHaTruyen extends Connector {
    public ThienHaTruyen() {
        setLabel("Thiên Hạ Truyện");
        setDomain("https://thienhatruyen.net/");
    }

    @Override
    public List<Chapter> fetchChapters() {
        String idSelector = "#scrollbar";
        return ChromeDriverUtils.fetchChapters(this, idSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "div.detail h1";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
