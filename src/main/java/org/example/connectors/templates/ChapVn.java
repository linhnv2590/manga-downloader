package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import java.util.List;

public class ChapVn extends Connector {
    public ChapVn() {
        setLabel("Chapvn - Alo8");
        setDomain("https://chap.vn/");
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
