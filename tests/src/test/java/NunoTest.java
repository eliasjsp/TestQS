import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static javax.swing.text.html.CSS.getAttribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 23/05/2016.
 */
public class NunoTest {

    private WebDriver driver;

    private StringBuffer verificationErrors = new StringBuffer();
    private String baseUrl = Util.getBaseUrl() + "/" + "nuno.html";
    private static final String FACEBOOK_URL = "https://www.facebook.com/nuno.laudo";
    private static final String LINKEDIN_URL = "https://pt.linkedin.com/in/nunolaudo";

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
        driver.get(baseUrl);
        assertEquals("Wrong page title", "I AM NUNO", driver.getTitle());
    }

    @Test
    public void testHeader() throws Exception {
        driver.get(baseUrl);
        assertEquals("Error while test headers", "i am nuno", driver.findElement(By.className("intro-sub")).getText().toLowerCase());
        assertEquals("Error while test headers","software engineer", driver.findElement(By.cssSelector(".intro h1")).getText().toLowerCase());
    }

    @Test
    public void testFacebookClick() throws Exception {
        driver.get(baseUrl);
        int numTabs = driver.getWindowHandles().size();
        WebElement face = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li/a"));
        assertEquals("Wrong href to Facebook", FACEBOOK_URL, face.getAttribute("href"));
        face.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());


        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(FACEBOOK_URL)) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No Facebook tab found", true, foundTab);
        //testLinkSocialNetwork(driver, FACEBOOK_URL,  By.xpath("//section[@id='home']/div/div[2]/ul/li/a"));
    }

    @Test
    public void testLinkedInClick() throws Exception {
        driver.get(baseUrl);
        int numTabs = driver.getWindowHandles().size();
        WebElement linkedin = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
        assertEquals("Wrong href to LinkedIn", LINKEDIN_URL, linkedin.getAttribute("href"));
        linkedin.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());

        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(LINKEDIN_URL)) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No LinkedIn tab found", true, foundTab);

        //testLinkSocialNetwork(driver, LINKEDIN_URL, By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
    }

    @Test
    public void testIfHaveMenu() throws Exception {
        driver.get(baseUrl);
        assertEquals("Missed about menu", true, Util.isElementPresent(By.linkText("About"), driver));
        assertEquals("Missed home menu",true, Util.isElementPresent(By.linkText("Home"), driver));
        assertEquals("Missed skills menu",true, Util.isElementPresent(By.linkText("Skills"), driver));
        assertEquals("Missed resume menu",true, Util.isElementPresent(By.linkText("Resume"), driver));
        assertEquals("Missed contact menu",true, Util.isElementPresent(By.linkText("Contact"), driver));

        //TODO: FALTA TESTAR AS LIGAÇÕES! Se vai parar ao sitio certo!
        //TODO: Falta user story para isto!
    }

    @Test
    public void testPersonalInfo() throws Exception {
        driver.get(baseUrl);

        assertEquals("The elements about is not present",true, Util.isElementPresent(By.cssSelector("#about h2"), driver));
        assertEquals("Wrong test about me", "about me", driver.findElement(By.cssSelector("#about h2")).getText().toLowerCase());

        assertEquals("Cant found the image", true, Util.isElementPresent(By.cssSelector("#about .myphoto img"), driver));
        assertEquals("Attribute src from image is wrong", Util.getBaseUrl() + "/assets/images/nuno.jpg", driver.findElement(By.cssSelector("#about .myphoto img")).getAttribute("src"));

        List<WebElement> biography = driver.findElements(By.cssSelector("#about .row .biography ul li"));
        assertEquals("Wrong name", "Name: Nuno Laúdo", biography.get(0).getText());
        assertEquals("Wrong date of birth", "Date of birth: 24 Nov 1994", biography.get(1).getText());
        assertEquals("Wrong address", "Address: Chã da Laranjeira, Leiria", biography.get(2).getText());
        assertEquals("Wrong nationality", "Nationality: Portuguese", biography.get(3).getText());
        assertEquals("Wrong phone", "Phone: (+351) 918335233", biography.get(4).getText());
        assertEquals("Wrong email", "Email: nunolaudo@hotmail.com", biography.get(5).getText());
    }
}
