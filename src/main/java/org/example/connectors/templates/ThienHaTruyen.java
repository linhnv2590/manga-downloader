package org.example.connectors.templates;

import org.example.Chapter;
import org.example.Manga;
import org.example.connectors.Connector;
import org.example.utils.ChromeDriverUtils;
import org.example.utils.Constant;
import org.example.utils.MangaUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ThienHaTruyen extends Connector {
    public ThienHaTruyen() {
        setLabel("Thiên Hạ Truyện");
        setDomain("https://thienhatruyen.net/");
    }

    @Override
    public List<Chapter> fetchChapters() {
        List<Chapter> chapters = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", MangaUtils.getDefaultChromeDrivePath());
        try {
            WebDriver driver = new ChromeDriver(ChromeDriverUtils.settingOptions());
            driver.get(this.getUrl());
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.CHROME_WAIT_TIMEOUT));
            WebElement parentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("scrollbar")));
            List<WebElement> anchorElements = parentElement.findElements(By.tagName("a"));
            logger.log(Level.INFO, "Number of chapters: {0}", anchorElements.size());
            for (WebElement anchorElement : anchorElements) {
                String src = anchorElement.getAttribute("href");
                String title = anchorElement.getText().split("\\n")[0];
                Chapter chapter = new Chapter(src, title);
                chapters.add(chapter);
            }
            driver.quit();
        } catch (Exception e) {
            logger.log(Level.INFO, "Error Chrome driver: {0}", e.getMessage());
        }
        return chapters;
    }

    @Override
    public Manga fetchManga() {
        String cssSelector = "div.detail h1";
        return connectorServicesImp.fetchManga(this, cssSelector);
    }
}
