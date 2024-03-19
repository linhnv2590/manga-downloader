package org.example.utils;

import ij.IJ;
import ij.ImagePlus;
import org.jsoup.nodes.Element;

import java.io.File;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;

public class MangaUtils {
    static Logger logger = Logger.getLogger(MangaUtils.class.getName());
    private static final List<String> IMAGE_EXTENSION_SUPPORT = Arrays.asList("jpg", "jpeg", "png", "webp");
    private static final Random random = new Random();

    private MangaUtils() {
    }

    public static String formatFolderName(String originalString, int maxLength) {
        String folderName = originalString.replace(":", " ").replaceAll("\\s+", " ").trim();
        folderName = folderName.substring(0, Math.min(folderName.length(), maxLength));
        return folderName;
    }

    public static String formatChapterTitle(String title) {
        String chapterNo = title.replaceAll("[^0-9.]", "");
        if (chapterNo.isEmpty()) {
            int randomNumber = random.nextInt();
            return Constant.CHAP + " " + String.format("%03d", randomNumber);
        } else if (chapterNo.matches("\\d+")) {
            return Constant.CHAP + " " + String.format("%03d", Integer.parseInt(chapterNo));
        }
        return Constant.CHAP + " " + chapterNo;
    }

    public static boolean isValidSrcImg(String url) {
        String lowercaseUrl = url.toLowerCase();
        for (String format : IMAGE_EXTENSION_SUPPORT) {
            if (lowercaseUrl.contains("." + format)) {
                return true;
            }
        }
        return false;
    }

    public static String getImageExtension(String imageSrc) {
        String fileExtension = "";
        int lastDotIndex = imageSrc.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < imageSrc.length() - 1) {
            fileExtension = imageSrc.substring(lastDotIndex + 1).toLowerCase();
        }
        int queryIndex = fileExtension.indexOf('?');
        if (queryIndex != -1) {
            fileExtension = fileExtension.substring(0, queryIndex);
        }
        return fileExtension;
    }

    public static boolean canCreateFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists()) {
            return true;
        }
        boolean success = folder.mkdirs();
        if (success) {
            return true;
        }
        logger.log(Level.WARNING, "Cannot create folder");
        return false;
    }

    public static String getDefaultDownloadDirectory() {
        String osName = System.getProperty("os.name").toLowerCase();
        String defaultDirectory = System.getProperty("user.home") + File.separator + "Downloads";
        if (osName.contains("win") || osName.contains("mac") || osName.contains("nix")
                || osName.contains("nux") || osName.contains("aix")) {
            return defaultDirectory;
        } else {
            return System.getProperty("user.dir");
        }
    }

    public static boolean isValidImageDimension(String path) {
        ImagePlus image = IJ.openImage(path);
        if (image == null) {
            deleteFile(path);
            return false;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        if (width > 500 && height > 500) {
            return true;
        }
        deleteFile(path);
        return false;
    }

    public static void deleteFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.delete(path);
            logger.log(Level.INFO, "Delete file success");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Can not delete file");
        }
    }

    public static String getDefaultChromeDrivePath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String currentPath = System.getProperty("user.dir");
        if (osName.contains("mac")) {
            return currentPath + File.separator + "chromedriver-mac-arm64/chromedriver";
        } else if (osName.contains("win")) {
            return currentPath + File.separator + "chromedriver-win64/chromedriver.exe";
        }
        return "";
    }

    public static String getImageSrc(Element element) {
        if (element == null) {
            return "";
        }
        String src = element.attr("src");
        String dataSrc = element.attr("data-src");
        return !src.isEmpty() ? src : dataSrc;
    }

    public static String getFileExt(String imageSrc) {
        boolean isValidFileToDownload = !imageSrc.isEmpty() && isValidSrcImg(imageSrc);
        String fileExt = getImageExtension(imageSrc);
        if (fileExt == null || fileExt.isEmpty()) {
            isValidFileToDownload = false;
        }
        if (!isValidFileToDownload) {
            return null;
        }
        return fileExt;
    }
}
