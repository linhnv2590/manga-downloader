package org.example.connectors;

import org.example.Chapter;
import org.example.Manga;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Connector implements ConnectorServices {
    private String label;
    private String domain;
    private String url;
    private Document document;
    private Manga manga;
    protected final ConnectorServicesImp connectorServicesImp = new ConnectorServicesImp();
    protected Logger logger = Logger.getLogger(getClass().getName());

    public Connector() {
    }

    @Override
    public Document connectToServer(String url) {
        return connectorServicesImp.connectToServer(url);
    }

    @Override
    public List<Chapter> fetchChapters() {
        return new ArrayList<>();
    }

    @Override
    public Manga fetchManga() {
        return new Manga("");
    }

    @Override
    public void downloadFromURL(Connector connector, Chapter chapter, String outDir, JTextArea logTextArea) {
        connectorServicesImp.downloadFromURL(this, chapter, outDir, logTextArea);
    }

    @Override
    public void downloadFile(String chapterUrl, String chapterDir, JTextArea logTextArea, String connectorReferer) {
        connectorServicesImp.downloadFile(chapterUrl, chapterDir, logTextArea, connectorReferer);
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public Manga getManga() {
        return manga;
    }

    @Override
    public void setManga(Manga manga) {
        this.manga = manga;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
