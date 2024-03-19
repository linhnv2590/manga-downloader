package org.example.connectors;

import org.example.Chapter;
import org.example.Manga;
import org.example.download.DownloadFile;
import org.example.download.DownloadFileUsingOkHttp;
import org.example.utils.Constant;
import org.example.utils.MangaUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectorServicesImp {
    Logger logger = Logger.getLogger(getClass().getName());

    public Document connectToServer(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Connect to server failed", e);
        }
        return null;
    }

    public List<Chapter> fetchChapters(ConnectorServices connector, String cssSelector) {
        List<Chapter> chapters = new ArrayList<>();
        if (connector == null || connector.getDocument() == null) {
            return chapters;
        }
        Elements elements = connector.getDocument().select(cssSelector);
        for (Element element : elements) {
            String url = element.attr("href");
            if (!url.startsWith("https://")) {
                url = connector.getDomain() + url;
            }
            String title = element.text();
            Chapter chapter = new Chapter(url, title);
            chapters.add(chapter);
        }
        return chapters;
    }

    public Manga fetchManga(ConnectorServices connector, String cssSelector) {
        Manga manga = new Manga("");
        if (connector == null || connector.getDocument() == null) {
            return manga;
        }
        Document document = connector.getDocument();
        Elements elements = document.select(cssSelector);
        if (!elements.isEmpty()) {
            Element firstElement = elements.first();
            if (firstElement != null) {
                manga.setTitle(firstElement.text());
                return manga;
            }
        }
        manga.setTitle(document.title());
        return manga;
    }

    public void downloadFromURL(Connector connector, Chapter chapter, String outDir, JTextArea logTextArea) {
        String saveDir = outDir.isEmpty() ? Constant.DEFAULT_SAVE_DIRECTORY : outDir;
        Manga manga = connector.getManga();
        if (manga == null || chapter == null) {
            return;
        }
        String mangaTitle = MangaUtils.formatFolderName(manga.getTitle(), Constant.MAX_LENGTH_FOLDER_NAME);
        String mangaDir = saveDir + File.separator + mangaTitle;
        String chapterTitle = MangaUtils.formatChapterTitle(chapter.getTitle());
        String chapterDir = saveDir + File.separator + (mangaDir.isEmpty() ? "" : mangaTitle + File.separator) + chapterTitle;
        if (MangaUtils.canCreateFolder(mangaDir) && MangaUtils.canCreateFolder(chapterDir)) {
            connector.downloadFile(chapter.getUrl(), chapterDir, logTextArea, connector.getDomain());
        }
    }

    public void downloadFile(String chapterUrl, String chapterDir, JTextArea logTextArea, String connectorReferer) {
        Document doc = connectToServer(chapterUrl);
        if (doc == null) {
            return;
        }
        int imageCount = 1;
        Elements imageElements = doc.select("img");
        for (Element element : imageElements) {
            try {
                String imageSrc = MangaUtils.getImageSrc(element);
                String fileExt = MangaUtils.getFileExt(imageSrc);
                if (fileExt == null) {
                    continue;
                }
                String fileName = String.format("%03d.%s", imageCount, fileExt);
                String filePath = chapterDir + File.separator + fileName;
                DownloadFile downloadFile = new DownloadFileUsingOkHttp();
                if (downloadFile.download(imageSrc, filePath, connectorReferer) && MangaUtils.isValidImageDimension(filePath)) {
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
