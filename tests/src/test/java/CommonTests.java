import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.text.WordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 27/05/2016.
 */
@RunWith(Parameterized.class)
public class CommonTests {
    //ORDER
    private static final int ABOUT_NAME_ORDER = 0;
    private static final int ABOUT_BDAY_ORDER = 1;
    private static final int ABOUT_ADDRESS_ORDER = 2;
    private static final int ABOUT_NATIONALITY_ORDER = 3;
    private static final int ABOUT_PHONE_ORDER = 4;
    private static final int ABOUT_EMAIL_ORDER = 5;
    private static final int SECTION_HOME_ORDER = 1;
    private static final int SECTION_ABOUT_ORDER = 2;
    private static final int SECTION_SKILLS_ORDER = 3;
    private static final int SECTION_RESUME_ORDER = 4;
    private static final int SECTION_HIRE_ORDER = 5;

    //other variables needed
    private static final String TITLE = "Software Engineer";

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String baseUrl;
    private JsonObject data;

    //lists
    private List<String> menu;

    private String memberName;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection members() {
        return Arrays.asList(
                "elias",
                "nuno",
                "rafael"
        );
    }

    public CommonTests(String memberName) {
        this.memberName = memberName;
        this.baseUrl = Util.getBaseUrl() + "/member.html?name=" + memberName;

        //Logger.getLogger("").setLevel(Level.OFF);
        menuPopulation();

        try {
            data = Util.getJsonObject(memberName);
        } catch (Exception e){
            fail("Could not load JSON for member " + memberName);
        }
    }

    private void menuPopulation() {
        menu = new ArrayList<String>();
        menu.add("home");
        menu.add("about");
        menu.add("skills");
        menu.add("resume");
    }

    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait((baseUrl.contains("localhost") ? 5 : 30), TimeUnit.SECONDS);
    }

    @Test
    public void testTitle() throws Exception {
        waitToLoad();
        assertEquals("Title of member " + memberName + " is wrong:" + driver.getCurrentUrl(), "I AM " + memberName.toUpperCase(), driver.getTitle());
    }

    @Test
    public void testHeader() throws Exception {
        waitToLoad();
        assertEquals("Error while test headers on " + memberName + " page", "i am " + memberName, driver.findElement(By.className("intro-sub")).getText().toLowerCase());
        assertEquals(memberName + " page title is different than expected", true, driver.findElement(By.xpath("//section[@id='home']/div/h1")).getText().equals(TITLE));
    }

    @Test
    public void testIfClickIAMXGoToMainPage() throws Exception {
        waitToLoad();
        driver.findElement(By.className("navbar-brand")).click();
        assertEquals("I AM X link does not work on " + memberName + " page", true, driver.getCurrentUrl().contains("index.html"));
        assertEquals("I AM X link page has wrong title", "We are awesome", driver.getTitle());
    }

    @Test
    public void testIfHaveIntuitiveThingToUserKnowHeNeedsScrollDown() throws Exception {
        waitToLoad();
        assertEquals("Does not have the wheel on " + memberName + " page", true, (driver.findElement(By.className("wheel")) != null));
    }

    @Test
    public void testIfHaveMenu() throws Exception {
        waitToLoad();
        for (String section : menu) {
            String sectionTitle = WordUtils.capitalize(section);
            assertEquals("Missed " + section + " menu on " + memberName + " page", true, Util.isElementPresent(By.linkText(sectionTitle), driver));

            if(!Util.isElementPresent(By.id(section), driver)){
                fail("Section '" + section + "' is not present in the document on " + memberName + " page");
            }
            List<WebElement> sectionList = driver.findElements(By.id(section));
            if (sectionList.size() > 1)
                fail("Multiple sections found for '" + section + "' on " + memberName + " page");
        }
    }

    @Test
    public void testIfHaveSectionForEachMenu() throws Exception {
        waitToLoad();
        for (String m : menu) {
            assertEquals("Section " + m + " does not exists on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='" + m + "']")) != null));
        }
    }

    @Test
    public void testIfHaveHireSection() throws Exception {
        waitToLoad();
        assertEquals("does not have a hire section on " + memberName + " page", true, (driver.findElement(By.className("hire-section")) != null));
    }

    @Test
    public void testSectionOrder() throws Exception {
        waitToLoad();
        assertEquals("Section home is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HOME_ORDER + "]")).getAttribute("id").equals("home")));
        assertEquals("Section about is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_ABOUT_ORDER + "]")).getAttribute("id").equals("about")));
        assertEquals("Section skills is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_SKILLS_ORDER + "]")).getAttribute("id").equals("skills")));
        assertEquals("Section resume is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_RESUME_ORDER + "]")).getAttribute("id").equals("resume")));
        assertEquals("Section hire is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HIRE_ORDER + "]")).getAttribute("class").contains("hire-section")));
    }

    @Test
    public void testFacebookClick() throws Exception {
        waitToLoad();
        int numTabs = driver.getWindowHandles().size();
        WebElement face = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li/a"));
        assertEquals("Wrong href to Facebook for " + memberName + "page", getAsStringFromData("home-facebook"), face.getAttribute("href"));
        face.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab on " + memberName + "page", numTabs + 1, driver.getWindowHandles().size());

        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(getAsStringFromData("home-facebook"))) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No Facebook tab found on " + memberName + "page", true, foundTab);
    }

    @Test
    public void testLinkedInClick() throws Exception {
        waitToLoad();
        int numTabs = driver.getWindowHandles().size();
        WebElement linkedin = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
        assertEquals("Wrong href to LinkedIn for " + memberName + "page", getAsStringFromData("home-linkedin"), linkedin.getAttribute("href"));
        linkedin.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab on " + memberName + "page", numTabs + 1, driver.getWindowHandles().size());

        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            if (driver.getCurrentUrl().contains(getAsStringFromData("home-linkedin"))) {
                foundTab = true;
                break;
            }
        }

        assertEquals("No LinkedIn tab found on " + memberName + "page", true, foundTab);

        //testLinkSocialNetwork(driver, LINKEDIN_URL, By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
    }

    @Test
    public void testPersonalInfo() throws Exception {
        waitToLoad();

        WebElement sectionTitle = driver.findElement(By.id("about"));
        assertEquals(false, sectionTitle == null);

        WebElement photo = driver.findElement(By.cssSelector("#about .myphoto img"));
        assertEquals(false, photo == null);
        assertEquals(Util.getBaseUrl() + "/assets/images/" + memberName +".jpg", photo.getAttribute("src"));

        assertEquals(getAsStringFromData("name"), driver.findElement(By.id("name")).getText());
        assertEquals(getAsStringFromData("bday"), driver.findElement(By.id("bday")).getText());
        assertEquals(getAsStringFromData("address"), driver.findElement(By.id("address")).getText());
        assertEquals(getAsStringFromData("nationality"), driver.findElement(By.id("nationality")).getText());
        assertEquals(getAsStringFromData("phone"), driver.findElement(By.id("phone")).getText());
        assertEquals(getAsStringFromData("email"), driver.findElement(By.id("email")).getText());
    }

    @Test
    public void testAboutMeInformation() throws Exception {
        waitToLoad();
        //System.out.println((String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", driver.findElement(By.id("what-i-do"))));
        assertEquals("No title 'Objective' found for " + memberName, "Objective", driver.findElement(By.id("objective-title")).getAttribute("textContent"));
        assertEquals("Objectives text not found for " + memberName, true, Util.isElementPresent(By.id("objective"), driver));
        assertEquals("Objectives text is wrong for " + memberName, getAsStringFromData("objective"), driver.findElement(By.id("objective")).getAttribute("textContent"));

        assertEquals("No title 'What i do?' found for " + memberName, "What I Do ?", driver.findElement(By.id("what-i-do-title")).getAttribute("textContent"));
        assertEquals("What I do? text not found for " + memberName, true, Util.isElementPresent(By.id("what-i-do"), driver));
        assertEquals("What I do? text is wrong for " + memberName, getAsStringFromData("what-i-do"), driver.findElement(By.id("what-i-do")).getAttribute("textContent"));

        List<WebElement> whatIDo = driver.findElements(By.xpath("//section[@id='about']/div/div/div[2]/div[2]/ul/li"));
        if (whatIDo == null)
            fail("What I Do? list not found for " + memberName);

        JsonArray whatIDoArray = getAsArrayFromData("what-i-do-list");
        assertEquals("The number of items in the list of what i do are wrong for " + memberName, whatIDoArray.size(), whatIDo.size());

        for (int i = 0; i < whatIDoArray.size(); i++) {
            WebElement element = whatIDo.get(i);
            assertEquals("Text at index " + i + " of What I Do? is wrong for " + memberName, whatIDoArray.get(i).getAsString(), element.getAttribute("textContent"));
        }
    }

    @Test
    public void testPublishedAppsURLs() throws Exception {
        waitToLoad();

        JsonArray publishedApps = getAsArrayFromData("published-apps");
        if (publishedApps == null || publishedApps.size() == 0) {
            assertEquals("Published apps group was found", false, Util.isElementPresent(By.xpath("//div[@id='published-apps']/div"), driver));
            assertEquals("Published apps group should have display:none", "display:none", driver.findElement(By.id("published-apps-container")).getAttribute("style"));
            assertEquals("Published apps group should be hidden", false, driver.findElement(By.id("published-apps-container")).isDisplayed());
        } else {
            assertEquals("Incorrect number of published apps", driver.findElements(By.xpath("//div[@id='published-apps']/div/a")).size(), publishedApps.size());

            int numTabs = driver.getWindowHandles().size();
            String windowHandle = driver.getWindowHandle();
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

                ArrayList<WebElement> clickElements = new ArrayList<WebElement>();
                clickElements.add(appElement);
                clickElements.add(driver.findElement(By.xpath("//div[@id='published-apps']/div[" + divIndex + "]/a")));

                for (WebElement clickable : clickElements) {
                    clickable.click();
                    assertEquals("Wrong numbers of tabs. Should be opened one more tab", numTabs + 1, driver.getWindowHandles().size());

                    boolean foundTab = false;
                    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                    for (String tab : tabs) {
                        driver.switchTo().window(tab);
                        if (driver.getCurrentUrl().contains(appURL)) {
                            foundTab = true;
                            break;
                        }
                    }
                    assertEquals("No " + appName + " tab found", true, foundTab);
                    assertEquals("Wrong page was opened", true, driver.getTitle().contains(appName));

                    // Close app tab
                    driver.close();
                    driver.switchTo().window(windowHandle);

                    assertEquals("Wrong numbers of tabs. Should be equal to beginning", numTabs, driver.getWindowHandles().size());
                }
            }
        }
    }


   /* @Test
    public void testNavbarHover() throws Exception {
        for(String member : members) {
            driver.get(baseUrl + member + ".html");
            for(int i = 1; i <= menu.size(); i++) {
                if( i > 0) {
                    driver.findElement(By.xpath("//nav/div/div[2]/ul/li/a")).click();
                } else {
                    driver.findElement(By.xpath("//nav/div/div[2]/ul/li[2]/a")).click();
                }
                WebElement li = driver.findElement(By.xpath("//nav/div/div[2]/ul/li[" + i + "]"));
                WebElement a = driver.findElement(By.xpath("//nav/div/div[2]/ul/li[" + i + "]/a"));
                System.out.println(li.getAttribute("class"));
                a.click();
                li = driver.findElement(By.xpath("//nav/div/div[2]/ul/li[" + i + "]"));
                System.out.println(li.getClass());
            }
        }
    }*/

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    //helper funtions
    private void waitToLoad() throws InterruptedException {
        driver.get(baseUrl);
        // Wait for JavaScript to load data
        Thread.sleep(100);
    }

    private String getAsStringFromData(String thing) {
        return data.getAsJsonPrimitive(thing).getAsString();
    }

    private JsonArray getAsArrayFromData(String what) {
        return data.getAsJsonArray(what);
    }
}
