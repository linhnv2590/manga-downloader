package org.example.connectors;

import org.example.Chapter;
import org.example.Manga;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.util.List;

public interface ConnectorServices {
    Document connectToServer(String url);
    List<Chapter> fetchChapters();
    Manga fetchManga();
    void downloadFromURL(Connector connector, Chapter chapter, String outPath, JTextArea logTextArea);
    void downloadFile(String chapterUrl, String chapterDir, JTextArea logTextArea, String connectorReferer);
    String getDomain();
    String getLabel();
    String getUrl();
    Document getDocument();
    void setDocument(Document document);
    Manga getManga();
    void setManga(Manga manga);
}
