package org.example.download;

public interface DownloadFile {
    boolean download(String fileUrl, String filePath, String connectorReferer);
}
