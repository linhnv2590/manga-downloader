package org.example.connectors.templates;

import org.example.*;
import org.example.connectors.Connector;
import java.util.List;

public class NhaSachMienPhi extends Connector {
    public NhaSachMienPhi(String url) {
        super("Nhà Sách Miễn Phí", "https://nhasachmienphi.com/", url);
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "div.item_ch a";
        return connectorServicesImp.fetchChapters(this, cssSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "h1";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
