package org.example.utils;

import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverUtils {

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
}
