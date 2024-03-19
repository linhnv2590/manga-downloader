package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;

import java.util.List;

public class KenhSinhVien extends Connector {
    public KenhSinhVien() {
        setLabel("Kênh Sinh Viên");
        setDomain("https://kenhsinhvien.vn/");
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "div.structItem-title a";
        return connectorServicesImp.fetchChapters(this, cssSelector);
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "h1.p-title-value";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
