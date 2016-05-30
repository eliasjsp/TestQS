import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.SystemColor.window;
import static javax.swing.text.html.CSS.getAttribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 23/05/2016.
 */
public class NunoTest {

    private WebDriver driver;

    private StringBuffer verificationErrors = new StringBuffer();
    private String baseUrl = Util.getBaseUrl() + "/member.html?name=nuno";

    @Before
    public void setUp() throws Exception {
        //driver = new FirefoxDriver();
        driver = new HtmlUnitDriver(true){
            @Override
            protected WebClient newWebClient(BrowserVersion version) {
                WebClient webClient = super.newWebClient(version);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                return webClient;
            }
        };
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Logger.getLogger("").setLevel(Level.OFF);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    /*@Test
    public void testPublishedAppsURLs() throws Exception {
        driver.get(baseUrl);


        WebElement alwaysOnDisplay = driver.findElement(By.id("always_on_display"));
        WebElement colorPoint = driver.findElement(By.id("color_point"));

        assertEquals("Always on display link not found", true, Util.isElementPresent(By.id("always_on_display"), driver));
        assertEquals("Color point link not found", true, Util.isElementPresent(By.id("color_point"), driver));

        assertEquals("The link for color point is wrong", COLOR_POINT_URL, colorPoint.getAttribute("href"));
        assertEquals("The link for always on display is wrong", ALWAYS_ON_DISPLAY, alwaysOnDisplay.getAttribute("href"));

        int numTabs = driver.getWindowHandles().size();
        String windowHandle = driver.getWindowHandle();

        //Color Point
        colorPoint.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());


        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(COLOR_POINT_URL)) {
                foundTab = true;
                break;
            }
        }
        assertEquals("No Color point tab found", true, foundTab);
        assertEquals("Wrong page was opened", "Color Point", driver.findElement(By.cssSelector("div.id-app-title")).getText());

        //Switch again to our page
        driver.switchTo().window(windowHandle);

        //Always on Display
        alwaysOnDisplay.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());

        foundTab = false;
        tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(ALWAYS_ON_DISPLAY)) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No always on display tab found", true, foundTab);
        assertEquals("Wrong page was opened", "Always on Display", driver.findElement(By.cssSelector("div.id-app-title")).getText());
    }*/
}
