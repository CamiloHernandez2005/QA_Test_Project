package com.example.AutomatizationQA.Playwright;

import com.example.AutomatizationQA.Models.Region;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PlaywrightUtils {

    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    public static final Duration SHORT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration LONG_TIMEOUT = Duration.ofSeconds(20);

    public String buildUrl(Region region) {
        return String.format("http://%s:%s%s",
                region.getIp(),
                region.getPort(),
                region.getPath());
    }

    public Browser createBrowser(Playwright playwright) {
        return playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    public Browser.NewContextOptions configureContext() {
        return new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setIgnoreHTTPSErrors(true)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
    }

    public void configurePageDefaults(Page page) {
        page.setDefaultTimeout(DEFAULT_TIMEOUT.toMillis());
        page.setDefaultNavigationTimeout(LONG_TIMEOUT.toMillis());
    }
}
