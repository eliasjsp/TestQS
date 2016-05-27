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
    private static final int NUMBER_OF_SECTIONS = 5;

    private static final String OBEJECTIVES = "An opportunity to work and upgrade oneself, as well as being involved in an organization that believes in gaining a competitive edge and giving back to the community. I'm presently become a master in mobile computing. I hope develop skills and knowledge of mobile computing area and become an honest asset to the business. As an individual, I'm self-confident you’ll find me creative, funny and naturally passionate. I’m a forward thinker, which others may find inspiring when working as a team.";
    private static final String WHAT_I_DO = "I am a student of master in mobile computing in IPLeiria, Portugal. Before, I graduated also in IPLeiria. Besides the normal school project, I like develop by myself.This way I can improve my skills. Normally those project are project for android platform. I already publish two apps: Color Point ( with Rafael Caetano) - Get the app here! Always on Display - Get the app here!";
    private static final int NUMBER_OF_PUBLISHED_APPS_BY_ME = 2;
    private static final String COLOR_POINT_URL = "https://play.google.com/store/apps/details?id=me.fayax.colorpoint";
    private static final String ALWAYS_ON_DISPLAY = "https://play.google.com/store/apps/details?id=pt.me.fayax.alwaysondisplay";
    private static final int NUMBER_OF_WHAT_I_DO_LIST = 6;

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


    @Test
    public void testAboutMeInformation() throws Exception {
        driver.get(baseUrl);
        assertEquals("No title about me found", "about me", driver.findElement(By.xpath("//section[@id='about']/div/h2")).getText().toLowerCase());

        assertEquals("No title objective found", "objective", driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div/h3")).getText().toLowerCase());

        assertEquals("The text on objectives is wrong", OBEJECTIVES.trim(), driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div/p")).getText().trim());
        assertEquals("The text on what i do is wrong", WHAT_I_DO.trim(), driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/p")).getText().trim());

        assertEquals("The number of published apps are wrong!", NUMBER_OF_PUBLISHED_APPS_BY_ME, driver.findElements(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/p/a")).size());


        assertEquals("The number of item in the list of what i do are wrong!", NUMBER_OF_WHAT_I_DO_LIST, driver.findElements(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li")).size());

        assertEquals("Text at index 1 at the list on about me section is wrong!", "Software development", driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li[1]")).getText());
        assertEquals("Text at index 2 at the list on about me section is wrong!", "Android apps development", driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li[2]")).getText());
        assertEquals("Text at index 3 at the list on about me section is wrong!", "IOS apps development", driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li[3]")).getText());
        assertEquals("Text at index 4 at the list on about me section is wrong!", "Control version repositories", driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li[4]")).getText());
        assertEquals("Text at index 5 at the list on about me section is wrong!", "Basics on project managing", driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li[5]")).getText());
        assertEquals("Text at index 6 at the list on about me section is wrong!", "Games Development", driver.findElement(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li[6]")).getText());

        //TODO: test if the links for each app are open correctly

    }

    @Test
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
        String title = "";
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(COLOR_POINT_URL)) {
                foundTab = true;
                title = driver.getTitle();
                break;
            }
        }
        title = title.split("–")[0];

        assertEquals("No Color point tab found", true, foundTab);
        assertEquals("Wrong page was opened", "Color Point".trim(), title.trim());

        //Switch again to our page
        driver.switchTo().window(windowHandle);
        System.out.println(driver.getWindowHandles().size());

        //Always on Display
        alwaysOnDisplay.click();
        System.out.println(driver.getWindowHandles().size());
        assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());

        foundTab = false;
        title = "";
        tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(ALWAYS_ON_DISPLAY)) {
                foundTab = true;
                title = driver.getTitle();
                System.out.println(title);
                break;
            }
        }
        title = title.split("–")[0];

        assertEquals("No always on display tab found", true, foundTab);
        assertEquals("Wrong page was opened", "Always on Display".trim(), title.trim());
    }
}
