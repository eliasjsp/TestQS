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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Rafael on 23/05/2016.
 */
public class RafaelTests {

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String baseUrl = Util.getBaseUrl() + "/" + "rafael.html";

    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testPage() throws Exception {
        driver.get(Util.getBaseUrl());
        driver.findElement(By.linkText("Rafael")).click();
        assertEquals("I AM RAFAEL", driver.getTitle());
    }

    @Test
    public void testHeader() throws Exception {
        driver.get(baseUrl);
        assertEquals("i am rafael", driver.findElement(By.className("intro-sub")).getText().toLowerCase());
        assertEquals("software engineer", driver.findElement(By.cssSelector(".intro h1")).getText().toLowerCase());
    }

    @Test
    public void testFacebookClick() throws Exception {
        driver.get(baseUrl);
        int numTabs = driver.getWindowHandles().size();
        WebElement face = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li/a"));
        assertEquals(false, face == null);
        assertEquals("https://www.facebook.com/rafael.caetano.50", face.getAttribute("href"));
        face.click();
        assertEquals(numTabs + 1,  driver.getWindowHandles().size());

        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains("www.facebook.com/rafael.caetano.50")) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No Facebook tab found", true, foundTab);
    }

    @Test
    public void testLinkedInClick() throws Exception {
        driver.get(baseUrl);
        int numTabs = driver.getWindowHandles().size();
        WebElement linkedin = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
        assertEquals(false, linkedin == null);
        assertEquals("https://linkedin.com/in/rafael-caetano-376636b4", linkedin.getAttribute("href"));
        linkedin.click();
        assertEquals(numTabs + 1,  driver.getWindowHandles().size());

        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains("linkedin.com/in/rafael-caetano-376636b4")) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No LinkedIn tab found", true, foundTab);
    }

    @Test
    public void testMenu() throws Exception {
        driver.get(baseUrl);
        // AT1
        assertEquals(false, (driver.findElement(By.linkText("About")) == null));
        assertEquals(false, (driver.findElement(By.linkText("Home")) == null));
        assertEquals(false, (driver.findElement(By.linkText("Skills")) == null));
        assertEquals(false, (driver.findElement(By.linkText("Resume")) == null));
        assertEquals(false, (driver.findElement(By.linkText("Contact")) == null));
        assertEquals(false, driver.findElement(By.className("navbar-brand")) == null);
        assertEquals(Util.getBaseUrl() + "/index.html", driver.findElement(By.className("navbar-brand")).getAttribute("href"));

        // AT2
        ArrayList<String> sections = new ArrayList<String>();
        sections.add(driver.findElement(By.linkText("About")).getAttribute("href").split("#")[1]);
        sections.add(driver.findElement(By.linkText("Home")).getAttribute("href").split("#")[1]);
        sections.add(driver.findElement(By.linkText("Skills")).getAttribute("href").split("#")[1]);
        sections.add(driver.findElement(By.linkText("Resume")).getAttribute("href").split("#")[1]);
        sections.add(driver.findElement(By.linkText("Contact")).getAttribute("href").split("#")[1]);

        for (String section: sections) {
            if(!Util.isElementPresent(By.id(section), driver)){
                fail("Section '" + section + "' is not present in the document");
            }

            // AT3
            List<WebElement> sectionList = driver.findElements(By.id(section));
            if (sectionList.size() > 1)
                fail("Multiple sections found for '" + section + "'");
        }
        driver.findElement(By.className("navbar-brand")).click();
        assertEquals("We are awesome", driver.getTitle());
    }

    @Test
    public void testPersonalInfo() throws Exception {
        driver.get(baseUrl);

        WebElement sectionTitle = driver.findElement(By.cssSelector("#about h2"));
        assertEquals(false, sectionTitle == null);
        assertEquals("about me", sectionTitle.getText().toLowerCase());

        WebElement photo = driver.findElement(By.cssSelector("#about .myphoto img"));
        assertEquals(false, photo == null);
        assertEquals(Util.getBaseUrl() + "/assets/images/rafael.jpg", photo.getAttribute("src"));

        List<WebElement> biography = driver.findElements(By.cssSelector("#about .row .biography ul li"));
        assertEquals("Name: Rafael Caetano", biography.get(0).getText());
        assertEquals("Date of birth: 01 Mar 1994", biography.get(1).getText());
        assertEquals("Address: Batalha, Leiria", biography.get(2).getText());
        assertEquals("Nationality: Portuguese", biography.get(3).getText());
        assertEquals("Phone: (+351) 916754699", biography.get(4).getText());
        assertEquals("Email: rafaelvcaetano@hotmail.com", biography.get(5).getText());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}

