import org.openqa.selenium.*;

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
