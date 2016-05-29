import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.*;

import java.io.IOException;
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

    public static JsonObject getJsonObject(String relativePath) throws Exception {
        JsonParser jp = new JsonParser();
        Path path = Paths.get(EliasTest.class.getResource(".").toURI());
        return(JsonObject) jp.parse(readFile(path.getParent().getParent().getParent()+ relativePath, Charset.defaultCharset()));
    }


    private static String readFile(String path, Charset encoding)throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
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
