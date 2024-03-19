package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import org.example.download.DownloadFile;
import org.example.download.DownloadFileUsingOkHttp;
import org.example.utils.Constant;
import org.example.utils.MangaUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TheGioiManga extends Connector {
    public TheGioiManga(String url) {
        super("Thế Giới Manga", "https://thegioimanga.vn/", url);
    }

    @Override
    public List<Chapter> fetchChapters() {
        String cssSelector = "header.tgm-card--titles h3.tgm-card--title" ;
        List<Chapter> chapters = new ArrayList<>();

        Elements elements = this.getDocument().select(cssSelector);
        for (Element element : elements) {
            Element firstLink = element.selectFirst("a");
            if (firstLink != null) {
                String firstLinkUrl = firstLink.attr("href");
                if (!firstLinkUrl.startsWith("https://")) {
                    firstLinkUrl = this.getDomain() + firstLinkUrl;
                }
                String title = element.text();
                Chapter chapter = new Chapter(firstLinkUrl, title);
                chapters.add(chapter);
            }
        }
        return chapters;
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "h1.tgm-page--title";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }

    @Override
    public void downloadFile(String chapterUrl, String chapterDir, JTextArea logTextArea, String connectorReferer) {
        Document doc = connectToServer(chapterUrl);
        if (doc == null) {
            return;
        }
        int imageCount = 1;
        Elements imageElements = doc.select("section#tgmid-sec-intro-card div.tgm-sw-c article figure.tgm-card--cover img");
        for (Element element : imageElements) {
            try {
                String imageSrc = MangaUtils.getImageSrc(element);
                String fileExt = MangaUtils.getFileExt(imageSrc);
                if (fileExt == null) {
                    continue;
                }
                if (fileExt.equals("webp")) {
                    fileExt = "jpg";
                }
                String fileName = String.format("%03d.%s", imageCount, fileExt);
                String filePath = chapterDir + File.separator + fileName;
                DownloadFile downloadFile = new DownloadFileUsingOkHttp();
                if (downloadFile.download(imageSrc, filePath, connectorReferer)) {
                    imageCount++;
                    String message = " - " + fileName + " done";
                    logTextArea.append(message + "\n");
                    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                }
                Thread.sleep(Constant.TOO_MANY_REQUEST_TIMEOUT);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Thread sleep failed", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
