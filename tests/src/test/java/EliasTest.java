import org.apache.commons.lang3.text.WordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;
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
    private static final String TITLE = "Software Engineer";
    private static final String SUB_TITLE = "I am a back end developer with 3 years of experience.\n" +
            "Involving with java, javascript, nodejs.\n" +
            "Feel free to contact.";
    private List<String> menu;
    @Before
    public void setUp() throws Exception {
        menu = new ArrayList<String>();
        menu.add("home");
        menu.add("about");
        menu.add("skills");
        menu.add("resume");
        menu.add("contact");
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

    @Test
    public void testProfessionTitle() throws Exception {
        driver.get(base_url);
        assertEquals("Elias page title is different than expected", true, ( driver.findElement(By.xpath("//section[@id='home']/div/h1")).getText().equals(TITLE) ));
    }

    @Test
    public void testSubTitle() throws Exception {
        driver.get(base_url);
        assertEquals("Elias page subtitle is different than expected", true, ( driver.findElement(By.xpath("//section[@id='home']/div/p")).getText().equals(SUB_TITLE) ));
    }

    @Test
    public void testIfHaveMenu() throws Exception {
        driver.get(base_url);
        for(String m : menu) {
            assertEquals("Menu " + m + "does not exists", true, ( driver.findElement(By.linkText(WordUtils.capitalize(m))) != null));
        }
    }

    @Test
    public void testIfHaveSectionForEachMenu() throws Exception {
        driver.get(base_url);
        for(String m : menu) {
            assertEquals("Section " + m + "does not exists", true, ( driver.findElement(By.xpath("//section[@id='" + m + "']")) != null));
        }
    }

    @Test
    public void testIfClickIAMXGoToMainPage() {
        driver.get(base_url);
        driver.findElement(By.xpath("//img")).click();
        assertEquals("I AM X link does not work", true, (driver.getCurrentUrl().contains("index.html") && driver.getTitle().equals("We are awesome") ));
    }

    @Test
    public void testPersonalInfo() throws Exception {
        driver.get(base_url);

        WebElement sectionTitle = driver.findElement(By.cssSelector("#about h2"));
        assertEquals(false, sectionTitle == null);
        assertEquals("about me", sectionTitle.getText().toLowerCase());

        WebElement photo = driver.findElement(By.cssSelector("#about .myphoto img"));
        assertEquals(false, photo == null);
        assertEquals(Util.getBaseUrl() + "/assets/images/elias.jpg", photo.getAttribute("src"));

        List<WebElement> biography = driver.findElements(By.cssSelector("#about .row .biography ul li"));
        assertEquals("Name: Elias Pinheiro", biography.get(0).getText());
        assertEquals("Date of birth: 01 Feb 1994", biography.get(1).getText());
        assertEquals("Address: R. Quinta Parceiros", biography.get(2).getText());
        assertEquals("Nationality: Brazilian", biography.get(3).getText());
        assertEquals("Phone: (+351) 915896602", biography.get(4).getText());
        assertEquals("Email: schmeisk@gmail.com", biography.get(5).getText());
    }
}
