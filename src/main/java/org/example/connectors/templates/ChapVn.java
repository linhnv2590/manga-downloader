package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import java.util.List;

public class ChapVn extends Connector {
    public ChapVn(String url) {
        super("Chapvn - Alo8", "https://chap.vn/", url);
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "div.titleText a.PreviewTooltip";
        return connectorServicesImp.fetchChapters(this, cssSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
