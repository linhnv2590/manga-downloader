package org.example.download;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadFileUsingOkHttp implements DownloadFile {
    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public boolean download(String fileUrl, String filePath, String connectorReferer) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fileUrl)
                .header("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                .header("Referer", connectorReferer)
                .header("sec-ch-ua-mobile", "?0")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .build();
        try (Response response = client.newCall(request).execute();
             FileOutputStream fos = new FileOutputStream(filePath)) {
            if (!response.isSuccessful()) {
                logger.log(Level.SEVERE, () -> "Something went wrong: " + response);
                return false;
            }
            fos.write(response.body().bytes());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred", e);
            return false;
        }
        return true;
    }
}
