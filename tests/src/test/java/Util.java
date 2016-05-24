import org.openqa.selenium.*;

import java.util.ArrayList;

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

    public static void testLinkSocialNetwork(WebDriver driver, String socialNetworkURL, By by) {
        driver.get(Util.getBaseUrl());
        int numTabs = driver.getWindowHandles().size();
        WebElement element = driver.findElement(by);
        assertEquals(socialNetworkURL, element.getAttribute("href"));
        element.click();
        assertEquals(numTabs + 1, driver.getWindowHandles().size());

        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(numTabs)); //Open the last opened tab
        assertEquals(true, driver.getCurrentUrl().contains(socialNetworkURL));
        driver.close();
        driver.switchTo().window(tabs.get(numTabs - 1)); // return to Nuno page
    }


}
