import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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

import static org.junit.Assert.*;

/**
 * Created by Elias on 27/05/2016.
 */
@RunWith(Parameterized.class)
public class CommonTests {
    //ORDER
    private static final int ABOUT_NAME_ORDER = 1;
    private static final int ABOUT_BDAY_ORDER = 2;
    private static final int ABOUT_ADDRESS_ORDER = 3;
    private static final int ABOUT_NATIONALITY_ORDER = 4;
    private static final int ABOUT_PHONE_ORDER = 5;
    private static final int ABOUT_EMAIL_ORDER = 6;
    private static final int SECTION_HOME_ORDER = 1;
    private static final int SECTION_ABOUT_ORDER = 2;
    private static final int SECTION_SKILLS_ORDER = 3;
    private static final int SECTION_RESUME_ORDER = 4;
    private static final int SECTION_HIRE_ORDER = 5;
                                                //%20 is to appear correct on browser
    private static final String MAIL_TO_SUBJECT = "?Subject=[FROM%20YOUR%20WEBSITE]";
    private static final String MAIL_TO_HREF_INITIAL = "mailto:";

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
        driver = new HtmlUnitDriver(true) {
            @Override
            protected WebClient newWebClient(BrowserVersion version) {
                WebClient webClient = super.newWebClient(version);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                return webClient;
            }
        };
        driver.manage().timeouts().implicitlyWait((baseUrl.contains("localhost") ? 5 : 30), TimeUnit.SECONDS);
    }

    @Test
    public void testTitle() throws Exception {
        System.out.println("Test testTitle");
        waitToLoad();
        assertEquals("Title of member " + memberName + " is wrong:" + driver.getCurrentUrl(), "I AM " + memberName.toUpperCase(), driver.getTitle());
    }

    @Test
    public void testHeader() throws Exception {
        System.out.println("Test testHeader");
        waitToLoad();
        assertEquals("Error while test headers on " + memberName + " page", "i am " + memberName, driver.findElement(By.className("intro-sub")).getText().toLowerCase());
        assertEquals(memberName + " page title is different than expected", true, driver.findElement(By.xpath("//section[@id='home']/div/h1")).getText().equals(TITLE));
        String text = (String) ((HtmlUnitDriver)driver).executeScript("return arguments[0].innerHTML", driver.findElement(By.id("home-sub-title")));
        text = text.replace("<BR>", "<br>");
        assertEquals("The text above specialization  on the of " + memberName + " page is wrong", getAsStringFromData("home-sub-title"), text);

        //order of elements
        assertEquals("The order fo elements is wrong on " + memberName + " page", true, Util.isElementPresent(By.cssSelector("div.intro > div + h1+ p#home-sub-title"),driver));
    }

    /*@Test
    public void testHeaderCSS() throws Exception {
        waitToLoad();
        //Identification
        WebElement identification = driver.findElement(By.cssSelector("div.intro-sub"));
        assertEquals("The color of specialization text is wrong on " + memberName + " page", "#ffffff", Util.rgbToHex(identification.getCssValue("color")));
        assertEquals("The size of specialization text is wrong on " + memberName + " page", "24px", identification.getCssValue("font-size"));
        assertEquals("The size of specialization text is wrong on " + memberName + " page", "uppercase", identification.getCssValue("text-transform"));

        //Specialization
        WebElement specialization = driver.findElement(By.xpath("//section[@id='home']/div/h1"));
        assertEquals("The color of specialization text is wrong on " + memberName + " page", "#52b3d9", Util.rgbToHex(specialization.getCssValue("color")));
        assertEquals("The color of specialization text is wrong on " + memberName + " page", "#68c3a3", Util.rgbToHex(driver.findElement(By.xpath("//section[@id='home']/div/h1/span")).getCssValue("color")));
        assertEquals("The size of specialization text is wrong on " + memberName + " page", "60px", specialization.getCssValue("font-size"));
    }

    @Test
    public void testIfClickIAMXGoToMainPage() throws Exception {
        System.out.println("Test testIfClickIAMXGoToMainPage");
        waitToLoad();
        WebElement goBack = driver.findElement(By.className("navbar-brand"));
        assertEquals("I AM X link has a wrong href", Util.getBaseUrl() + "/index.html", goBack.getAttribute("href"));

        goBack.click();
        assertEquals("I AM X link does not work on " + memberName + " page", driver.getCurrentUrl(), Util.getBaseUrl()  + "/index.html");
        assertEquals("I AM X link page has wrong title", "We are awesome", driver.getTitle());
    }

    @Test
    public void testIfHaveIntuitiveThingToUserKnowHeNeedsScrollDown() throws Exception {
        System.out.println("Test testIfHaveIntuitiveThingToUserKnowHeNeedsScrollDown");
        waitToLoad();
        assertEquals("Does not have the wheel on " + memberName + " page", true, (driver.findElement(By.className("wheel")) != null));
    }

    @Test
    public void testIfHaveMenu() throws Exception {
        System.out.println("Test testIfHaveMenu");
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
        System.out.println("Test testIfHaveSectionForEachMenu");
        waitToLoad();
        for (String m : menu) {
            assertEquals("Section " + m + " does not exists on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='" + m + "']")) != null));
        }
    }

    @Test
    public void testIfHaveHireSection() throws Exception {
        System.out.println("Test testIfHaveHireSection");
        waitToLoad();
        assertEquals("does not have a hire section on " + memberName + " page", true, (driver.findElement(By.className("hire-section")) != null));
        //TODO:if the user doesn´t have a job, test if exists a option to hire him and test its functionality
        //TODO: if the user have a job, test the company's link
    }

    @Test
    public void testSectionOrder() throws Exception {
        System.out.println("Test testSectionOrder");
        waitToLoad();
        assertEquals("Section home is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HOME_ORDER + "]")).getAttribute("id").equals("home")));
        assertEquals("Section about is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_ABOUT_ORDER + "]")).getAttribute("id").equals("about")));
        assertEquals("Section skills is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_SKILLS_ORDER + "]")).getAttribute("id").equals("skills")));
        assertEquals("Section resume is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_RESUME_ORDER + "]")).getAttribute("id").equals("resume")));
        assertEquals("Section hire is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HIRE_ORDER + "]")).getAttribute("class").contains("hire-section")));
    }

    @Test
    public void testFacebookClick() throws Exception {
        System.out.println("Test testFacebookClick");
        waitToLoad();
        int numTabs = driver.getWindowHandles().size();
        WebElement face = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li/a"));
        assertEquals("Wrong href to Facebook for " + memberName + "page", getAsStringFromData("home-facebook"), face.getAttribute("href"));
        face.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab on " + memberName + "page", numTabs + 1, driver.getWindowHandles().size());

        boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<> (driver.getWindowHandles());
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
        System.out.println("Test testLinkedInClick");
        waitToLoad();
        int numTabs = driver.getWindowHandles().size();
        WebElement linkedin = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
        assertEquals("Wrong href to LinkedIn for " + memberName + " page", getAsStringFromData("home-linkedin"), linkedin.getAttribute("href"));
        linkedin.click();
        assertEquals("Wrong numbers of tabs. Should be opened one more tab on " + memberName + " page", numTabs + 1, driver.getWindowHandles().size());

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
    public void testCurriculumClick() throws Exception {
        waitToLoad();

        String curriculumURL = getAsStringFromData("curriculum");
        if (curriculumURL == null || curriculumURL.isEmpty()) {

        } else {
            int numTabs = driver.getWindowHandles().size();
            WebElement c = driver.findElement(By.id("curriculum"));
            assertEquals("Wrong href to Curriculum for " + memberName + "page", Util.getBaseUrl() + curriculumURL, c.getAttribute("href"));
            c.click();
            assertEquals("Wrong numbers of tabs. Should be opened one more tab on " + memberName + "page", numTabs + 1, driver.getWindowHandles().size());

            boolean foundTab = false;
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            for (String tab : tabs) {
                driver.switchTo().window(tab);
                if (driver.getCurrentUrl().contains(curriculumURL)) {
                    foundTab = true;
                    break;
                }
            }
            assertEquals("No Curriculum tab found on " + memberName + "page", true, foundTab);
        }
    }

    @Test
    public void testMailTo() throws Exception {
        waitToLoad();
        WebElement mailto = driver.findElement(By.id("mail-to"));
        String email = "";
        try {
           email = getAsStringFromData("email");
        } catch (Exception e) {
            fail("User does not have mail data in json");
        }
        assertEquals("mail to does not exists", false, mailto == null);
        assertEquals("mail to href is not equal", MAIL_TO_HREF_INITIAL + email +MAIL_TO_SUBJECT, mailto.getAttribute("href"));
    }

    @Test
    public void testPersonalInfo() throws Exception {
        System.out.println("Test testPersonalInfo");
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
    public void testPersonalInfoLabels() throws Exception {
        waitToLoad();
        List<WebElement> descriptionLabels = driver.findElements(By.xpath("//section[@id='about']/div/div/div/div/ul/li/strong"));
        List<WebElement> informationLabels = driver.findElements(By.xpath("//section[@id='about']/div/div/div/div/ul/li/span"));

        assertEquals("The number description labels are different than information labels on " + memberName + " page", descriptionLabels.size(), informationLabels.size());

        for (int i=0; i< descriptionLabels.size(); i++){
            assertEquals("One description label on " + memberName + " page is not bold", "bold", descriptionLabels.get(i).getCssValue("font-weight")) ;
            assertNotEquals("One information label on " + memberName + " page is  bold", "bold", informationLabels.get(i).getCssValue("font-weight")); ;
        }
    }

    @Test
    public void testPersonalInfoOrder() throws Exception {
        waitToLoad();
        assertEquals("Label name is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_NAME_ORDER + "]/strong")).getText()).equals("Name:"));
        assertEquals("Label date of birth is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_BDAY_ORDER + "]/strong")).getText()).equals("Date of birth:"));
        assertEquals("Label address is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_ADDRESS_ORDER + "]/strong")).getText()).equals("Address:"));
        assertEquals("Label nationality is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_NATIONALITY_ORDER + "]/strong")).getText()).equals("Nationality:"));
        assertEquals("Label phone is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_PHONE_ORDER + "]/strong")).getText()).equals("Phone:"));
        assertEquals("Label email is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_EMAIL_ORDER + "]/strong")).getText()).equals("Email:"));

    }

    //TODO: check if both labels have correct information

    @Test
    public void testAboutMeInformation() throws Exception {
        System.out.println("Test testAboutMeInformation");
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
        System.out.println("Test testPublishedAppsURLs");
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
                    assertEquals("Wrong page was opened", appName, driver.findElement(By.cssSelector("div.id-app-title")).getText());

                    // Close app tab
                    driver.close();
                    driver.switchTo().window(windowHandle);

                    assertEquals("Wrong numbers of tabs. Should be equal to beginning", numTabs, driver.getWindowHandles().size());
                }
            }
        }
    }*/


   /* @Test
    public void testNavbarHover() throws Exception {
        waitToLoad();
        WebElement li = driver.findElement(By.xpath("//nav/div/div[2]/ul/li"));
        System.out.println(li.getCssValue("color"));
        //assertEquals("Menu hover does not work", true, li.getAttribute("class") != null && li.getAttribute("class").equals("active"));
        for(int i = menu.size(); i >0; i--) {
            WebElement a = driver.findElement(By.xpath("//nav/div/div[2]/ul/li[" + i + "]/a"));
            a.click();
            Thread.sleep(1000);
            li = driver.findElement(By.xpath("//nav/div/div[2]/ul/li[" + i + "]"));
            System.out.println("Depois de clicar " + menu.get(i-1) + " "  + li.getAttribute("class") );
            System.out.println(li.getCssValue("color"));
            //assertEquals("Menu hover does not work", true, li.getAttribute("class") != null && li.getAttribute("class").equals("active"));
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
        JsonPrimitive obj = data.getAsJsonPrimitive(thing);
        return obj == null ? null : obj.getAsString();
    }

    private JsonArray getAsArrayFromData(String what) {
        return data.getAsJsonArray(what);
    }
}
