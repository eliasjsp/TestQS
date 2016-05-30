import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static javafx.scene.input.KeyCode.J;
import static org.junit.Assert.assertEquals;

public final class Util {
    private static String baseUrl;
    private static boolean acceptNextAlert = true;

    public static String getBaseUrl() {
        if (baseUrl == null) {
            String url = System.getProperty("baseUrl");
            if (url == null)
                url = "http://localhost:8080";
            baseUrl = url;
            System.out.println("Base URL is: " + baseUrl);
        }
        return baseUrl;
    }

    public static boolean isElementPresent(By by, WebDriver driver) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static JsonObject getJsonObject(String member) throws Exception {
        JsonParser jp = new JsonParser();
        URL url = new URL(getBaseUrl() + "/json/" + member + ".json");
        ReadableByteChannel channel = Channels.newChannel(url.openStream());
        Reader reader = Channels.newReader(channel, "utf-8");
        String data = "";
        int character;
        while ((character = reader.read()) >= 0f) {
            data += (char) character;
        }

        return (JsonObject) jp.parse(data);
    }


    public static void waitUntilClickable(WebDriver driver, By locator){
        WebDriverWait waiting = new WebDriverWait(driver, 10);
        waiting.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
    }

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public static String closeAlertAndGetItsText(WebDriver driver) {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }


}
