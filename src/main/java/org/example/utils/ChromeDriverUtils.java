package org.example.utils;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChromeDriverUtils {
    static Logger logger = Logger.getLogger(MangaUtils.class.getName());

    private ChromeDriverUtils() {
    }
    public static ChromeOptions settingOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        return options;
    }

    public static List<Chapter> fetchChapters(Connector connector, String cssSelector) {
        List<Chapter> chapters = new ArrayList<>();
        int retries = Constant.NUMBER_OF_TIMES_RETRY;
        try {
            System.setProperty("webdriver.chrome.driver", MangaUtils.getDefaultChromeDrivePath());
            WebDriver driver = new ChromeDriver(settingOptions());
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.CHROME_WAIT_TIMEOUT));
            WebElement parentElement;
            List<WebElement> anchorElements = new ArrayList<>();

            driver.get(connector.getUrl());
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            while (retries > 0) {
                parentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
                anchorElements = parentElement.findElements(By.tagName("a"));
                if (anchorElements.isEmpty()) {
                    retries--;
                    Thread.sleep(Constant.FIND_ELEMENT_TIMEOUT);
                } else {
                    break;
                }
            }
            logger.log(Level.INFO, "Number of chapters: {0}", anchorElements.size());
            for (WebElement anchorElement : anchorElements) {
                String src = anchorElement.getAttribute("href");
                String title = anchorElement.getText().split("\\n")[0];
                Chapter chapter = new Chapter(src, title);
                chapters.add(chapter);
            }
            driver.quit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error Chrome driver: {0}", e.getMessage());
            Thread.currentThread().interrupt();
        }
        return chapters;
    }

    public static Manga fetchManga(Connector connector, String cssSelector) {

        return null;
    }
}
