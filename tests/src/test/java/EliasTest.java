import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 23/05/2016.
 */
public class EliasTest {

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String base_url = Util.getBaseUrl() + "/" + "elias.html";
    private static final String FACEBOOK_URL = "https://www.facebook.com/eliasjsp";
    private static final String LINKEDIN_URL = "https://pt.linkedin.com/in/eliasjsp";

    @Before
    public void setUp() throws Exception {
        //driver = new FirefoxDriver();
        driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    @Test
    public void testTitle() throws Exception {
        driver.get(base_url);
        assertEquals("I AM ELIAS", driver.getTitle());
    }

    @Test
    public void testFacebookClick() throws Exception {
        driver.get(base_url);
        WebElement face = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li/a"));
        assertEquals(FACEBOOK_URL, face.getAttribute("href"));
        face.click();
        assertEquals(2, (new ArrayList<String> (driver.getWindowHandles())).size());
    }

    @Test
    public void testLikedInClick() throws Exception {
        driver.get(base_url);
        WebElement linkedin = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
        assertEquals(LINKEDIN_URL, linkedin.getAttribute("href"));
        linkedin.click();
        assertEquals(2, (new ArrayList<String> (driver.getWindowHandles())).size());
    }
}
