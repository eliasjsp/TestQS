import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 23/05/2016.
 */
public class NunoTest {

    private WebDriver driver;

    private StringBuffer verificationErrors = new StringBuffer();
    private String baseUrl = Util.getBaseUrl() + "/member.html?name=nuno";
    private JsonObject data;

    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver(true) {
            @Override
            protected WebClient newWebClient(BrowserVersion version) {
                WebClient webClient = super.newWebClient(version);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                return webClient;
            }
        };
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Logger.getLogger("").setLevel(Level.OFF);

        try {
            data = Util.getJsonObject("nuno");
        } catch (Exception e) {
            fail("Could not load JSON for member nuno");
        }
    }

    @Test
    public void testPublishedAppsURLs() throws Exception {
        driver.get(baseUrl);

        JsonArray publishedApps = getAsArrayFromData("published-apps");
        assertEquals("Incorrect number of published apps", driver.findElements(By.xpath("//div[@id='published-apps']/div/a")).size(), publishedApps.size());

        int numTabs = driver.getWindowHandles().size();
        String windowHandle = driver.getWindowHandle();
        //TODO: Verificar outros <a>
        //TODO: Verificar imagens
        //TODO: NEste momento os aray tem de de estar vazios ou este teste deve falhar

        for (int i = 0; i < publishedApps.size(); i++) {
            JsonObject appObject = publishedApps.get(i).getAsJsonObject();

            int divIndex = i + 1;
            String appName = appObject.get("name").getAsString();
            String appURL = appObject.get("url").getAsString();
            String iconURL = appObject.get("image-url").getAsString();

            assertEquals(appName + " link not found", true, Util.isElementPresent(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]/div/a"), driver));
            WebElement appElement = driver.findElement(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]/div/a"));
            assertEquals("The link for " + appName + " is wrong", appURL, appElement.getAttribute("href"));

            assertEquals(appName + " icon not found", true, Util.isElementPresent(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]//a/img"), driver));
            assertEquals(appName + " icon is incorrect", iconURL, driver.findElement(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]//a/img")).getAttribute("src"));
            assertEquals(appName + " icon is incorrect", appName, driver.findElement(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]//a/img")).getAttribute("alt"));

            assertEquals(appElement + " button not found", true, Util.isElementPresent(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]/a"), driver));
            assertEquals(appElement + " button has wrong URL", appURL, driver.findElement(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]/a")).getAttribute("href"));
            assertEquals(appElement + " button has wront text", appName, driver.findElement(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]/a")).getText());

            appElement.click();
            assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());

            boolean foundTab = false;
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            for (String tab : tabs) {
                driver.switchTo().window(tab);
                if (driver.getCurrentUrl().contains(appObject.get("url").getAsString())) {
                    foundTab = true;
                    break;
                }
            }
            assertEquals("No " + appName + " tab found", true, foundTab);
            assertEquals("Wrong page was opened", appName, driver.findElement(By.cssSelector("div.id-app-title")).getText());

            //Switch again to our page
            driver.switchTo().window(windowHandle);
        }

/*
        //Color Point
        colorPoint.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());


        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(publishedApps.get(0).getAsJsonObject().get("url").getAsString())) {
                foundTab = true;
                break;
            }
        }
        assertEquals("No Color point tab found", true, foundTab);
        assertEquals("Wrong page was opened", publishedApps.get(0).getAsJsonObject().get("name").getAsString(), driver.findElement(By.cssSelector("div.id-app-title")).getText());

        //Switch again to our page
        driver.switchTo().window(windowHandle);

        //Always on Display
        alwaysOnDisplay.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());

        foundTab = false;
        tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(publishedApps.get(1).getAsJsonObject().get("url").getAsString())) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No always on display tab found", true, foundTab);
        assertEquals("Wrong page was opened", publishedApps.get(1).getAsJsonObject().get("name").getAsString(), driver.findElement(By.cssSelector("div.id-app-title")).getText());
*/
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private String getAsStringFromData(String thing) {
        return data.getAsJsonPrimitive(thing).getAsString();
    }

    private JsonArray getAsArrayFromData(String what) {
        return data.getAsJsonArray(what);
    }
}
