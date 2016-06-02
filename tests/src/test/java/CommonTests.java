import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.lang3.text.WordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

import static java.awt.SystemColor.text;
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

        Logger.getLogger("").setLevel(Level.OFF);
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
        waitToLoad("testTitle");
        assertEquals("Title of member " + memberName + " is wrong:" + driver.getCurrentUrl(), "I AM " + memberName.toUpperCase(), driver.getTitle());
    }

    @Test
    public void testHeader() throws Exception {
        waitToLoad("testHeader");
        assertEquals("Error while test headers on " + memberName + " page", "i am " + memberName, driver.findElement(By.className("intro-sub")).getText().toLowerCase());
        assertEquals(memberName + " page title is different than expected", true, driver.findElement(By.xpath("//section[@id='home']/div/h1")).getText().equals(TITLE));
        String text = (String) ((HtmlUnitDriver)driver).executeScript("return arguments[0].innerHTML", driver.findElement(By.id("home-sub-title")));
        text = text.replace("<BR>", "<br>");
        assertEquals("The text above specialization  on the of " + memberName + " page is wrong", getAsStringFromData("home-sub-title"), text);

        //order of elements
        assertEquals("The order fo elements is wrong on " + memberName + " page", true, Util.isElementPresent(By.cssSelector("div.intro > div + h1+ p#home-sub-title"),driver));
    }

    @Test
    public void testHeaderCSS() throws Exception {
        waitToLoad("testHeaderCSS");
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
        waitToLoad("testIfClickIAMXGoToMainPage");
        WebElement goBack = driver.findElement(By.className("navbar-brand"));
        assertEquals("I AM X link has a wrong href", Util.getBaseUrl() + "/index.html", goBack.getAttribute("href"));

        goBack.click();
        assertEquals("I AM X link does not work on " + memberName + " page", driver.getCurrentUrl(), Util.getBaseUrl()  + "/index.html");
        assertEquals("I AM X link page has wrong title", "We are awesome", driver.getTitle());
    }

    @Test
    public void testIfHaveIntuitiveThingToUserKnowHeNeedsScrollDown() throws Exception {
        waitToLoad("testIfHaveIntuitiveThingToUserKnowHeNeedsScrollDown");
        assertEquals("Does not have the wheel on " + memberName + " page", true, (driver.findElement(By.className("wheel")) != null));
    }

    @Test
    public void testIfHaveMenu() throws Exception {
        waitToLoad("testIfHaveMenu");
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
        waitToLoad("testIfHaveSectionForEachMenu");
        for (String m : menu) {
            assertEquals("Section " + m + " does not exists on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='" + m + "']")) != null));
        }
    }

    @Test
    public void testIfHaveHireSection() throws Exception {
        waitToLoad("testIfHaveHireSection");
        assertEquals("does not have a hire section on " + memberName + " page", true, (driver.findElement(By.className("hire-section")) != null));
        //TODO:if the user doesn�t have a job, test if exists a option to hire him and test its functionality
        //TODO: if the user have a job, test the company's link
    }

    @Test
    public void testSectionOrder() throws Exception {
        waitToLoad("testSectionOrder");
        assertEquals("Section home is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HOME_ORDER + "]")).getAttribute("id").equals("home")));
        assertEquals("Section about is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_ABOUT_ORDER + "]")).getAttribute("id").equals("about")));
        assertEquals("Section skills is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_SKILLS_ORDER + "]")).getAttribute("id").equals("skills")));
        assertEquals("Section resume is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_RESUME_ORDER + "]")).getAttribute("id").equals("resume")));
        assertEquals("Section hire is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HIRE_ORDER + "]")).getAttribute("class").contains("hire-section")));
    }

    @Test
    public void testFacebookClick() throws Exception {
        waitToLoad("testFacebookClick");
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
        waitToLoad("testLinkedInClick");
        int numTabs = driver.getWindowHandles().size();
        WebElement linkedin = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
        assertEquals("Wrong href to LinkedIn for " + memberName + " page", getAsStringFromData("home-linkedin"), linkedin.getAttribute("href"));
        linkedin.click();
        Thread.sleep(200);
        assertEquals("Wrong numbers of tabs. Should be opened one more tab on " + memberName + " page", numTabs + 1, driver.getWindowHandles().size());

        /*boolean foundTab = false;
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        for (String tab : tabs) {
            driver.switchTo().window(tab);
            Thread.sleep(200);
            if (driver.getCurrentUrl().contains(getAsStringFromData("home-linkedin"))) {
                foundTab = true;
                break;
            }
        }
        assertEquals("No LinkedIn tab found on " + memberName + "page", true, foundTab);*/
    }

    @Test
    public void testCurriculumClick() throws Exception {
        waitToLoad("testCurriculumClick");

        String curriculumURL = getAsStringFromData("curriculum");
        if (curriculumURL == null || curriculumURL.length() == 0) {
            assertEquals("CV button was found", true, Util.isElementPresent(By.id("curriculum"), driver));
            assertEquals("CV button should have display: none", "none", driver.findElement(By.id("curriculum")).getCssValue("display"));
            assertEquals("CV should be hidden", false, driver.findElement(By.id("curriculum")).isDisplayed());
        } else {
            int numTabs = driver.getWindowHandles().size();
            WebElement c = driver.findElement(By.id("curriculum"));
            assertEquals("Wrong href to Curriculum for " + memberName + "page", Util.getBaseUrl() + curriculumURL, c.getAttribute("href"));
            c.click();
            assertEquals("Wrong numbers of tabs. Should be opened one more tab on " + memberName + "page", numTabs + 1, driver.getWindowHandles().size());

            boolean foundTab = false;
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
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
        waitToLoad("testMailTo");
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
        waitToLoad("testPersonalInfo");

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
        waitToLoad("testPersonalInfoLabels");
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
        waitToLoad("testPersonalInfoOrder");
        assertEquals("Information name label name is not on the right order on " + memberName + " page", "Name:", (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_NAME_ORDER + "]/strong")).getText()));
        assertEquals("Content label name is not on the right order on " + memberName + " page", getAsStringFromData("name"), (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_NAME_ORDER + "]/span")).getText()));

        assertEquals("Information label date of birth is not on the right order on " + memberName + " page", "Date of birth:", (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_BDAY_ORDER + "]/strong")).getText()));
        assertEquals("Content label date of birth is not on the right order on " + memberName + " page", getAsStringFromData("bday"), (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_BDAY_ORDER + "]/span")).getText()));

        assertEquals("Information label address is not on the right order on " + memberName + " page", "Address:", (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_ADDRESS_ORDER + "]/strong")).getText()));
        assertEquals("Content label address of birth is not on the right order on " + memberName + " page", getAsStringFromData("address"), (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_ADDRESS_ORDER + "]/span")).getText()));

        assertEquals("Label nationality is not on the right order on " + memberName + " page", "Nationality:", (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_NATIONALITY_ORDER + "]/strong")).getText()));
        assertEquals("Content label nationality of birth is not on the right order on " + memberName + " page", getAsStringFromData("nationality"), (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_NATIONALITY_ORDER + "]/span")).getText()));

        assertEquals("Label phone is not on the right order on " + memberName + " page", "Phone:", (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_PHONE_ORDER + "]/strong")).getText()));
        assertEquals("Content label phone of birth is not on the right order on " + memberName + " page", getAsStringFromData("phone"), (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_PHONE_ORDER + "]/span")).getText()));

        assertEquals("Label email is not on the right order on " + memberName + " page", "Email:", (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_EMAIL_ORDER + "]/strong")).getText()));
        assertEquals("Content label phone of birth is not on the right order on " + memberName + " page", getAsStringFromData("email"), (driver.findElement(By.xpath("//section[@id='about']/div/div/div/div/ul/li[" + ABOUT_EMAIL_ORDER + "]/span")).getText()));

    }


    @Test
    public void testAboutMeInformation() throws Exception {
        waitToLoad("testAboutMeInformation");

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
        waitToLoad("testPublishedAppsURLs");

        JsonArray publishedApps = getAsArrayFromData("published-apps");
        if (publishedApps == null || publishedApps.size() == 0) {
            assertEquals("Published apps group was found", false, Util.isElementPresent(By.xpath("//div[@id='published-apps']/div"), driver));
            assertEquals("Published apps group should have display:none", "none", driver.findElement(By.id("published-apps-container")).getCssValue("display"));
            assertEquals("Published apps group should be hidden", false, driver.findElement(By.id("published-apps-container")).isDisplayed());
        } else {
            assertEquals("Incorrect number of published apps", driver.findElements(By.xpath("//div[@id='published-apps']/div/a")).size(), publishedApps.size());

            int numTabs = driver.getWindowHandles().size();
            String windowHandle = driver.getWindowHandle();

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
    }

    @Test
    public void testProgrammingSkills() throws Exception {
        waitToLoad("testProgrammingSkills");

		JsonArray programmingSkills = getAsArrayFromData("programming-skills");
		if (programmingSkills == null || programmingSkills.size() == 0) {
			assertEquals("Skills should be empty", false, Util.isElementPresent(By.cssSelector("#programming-skills div.col-md-6"), driver));
		} else {
			assertEquals("Skills section not found", true, Util.isElementPresent(By.id("skills"), driver));
			assertEquals("Skills title not found", true, Util.isElementPresent(By.xpath("//section[@id='skills']/div/h2"), driver));
			WebElement titleElement = driver.findElement(By.xpath("//section[@id='skills']/div/h2"));
			assertEquals("Skills title is wrong", "Skills", titleElement.getAttribute("textContent"));
			assertEquals("Main skill div not found", true, Util.isElementPresent(By.id("programming-skills"), driver));

            assertEquals("Wrong text style on title", "uppercase", titleElement.getCssValue("text-transform"));
            assertEquals("Wrong text size on title", "50px", titleElement.getCssValue("font-size"));


			List<WebElement> skills = driver.findElements(By.cssSelector("#skills div.skill-progress"));
			assertEquals("Wrong number of skills", programmingSkills.size(), skills.size());
			if (skills.size() > 1) {
				assertEquals("Should have 2 columns", 2, driver.findElements(By.cssSelector("#skills div.col-md-6")).size());

				int leftCount = (int) Math.ceil(skills.size() / 2f);
				int rightCount = (int) Math.floor(skills.size() / 2f);

				assertEquals("Wrong number of skills on the left", leftCount, driver.findElements(By.xpath("//div[@id='programming-skills']/div[1]/div")).size());
				assertEquals("Wrong number of skills on the left", rightCount, driver.findElements(By.xpath("//div[@id='programming-skills']/div[2]/div")).size());
			} else if (skills.size() == 1) {
				assertEquals("Should have a single column", 1, driver.findElements(By.cssSelector("#skills div.col-md-6")).size());
				assertEquals("Wrong number of elements in left div", 1, driver.findElements(By.cssSelector("#skills div.col-md-6 div.skill-progress")).size());
			} else {
				assertEquals("Columns should not be present", false, Util.isElementPresent(By.xpath("//div[@id='programming-skills']/div"), driver));
				return;
			}

			for (JsonElement skill : programmingSkills) {
				JsonObject skillObj = skill.getAsJsonObject();
				String skillName = skillObj.get("name").getAsString();
				int skillProgress = skillObj.get("progress").getAsInt();

				assertEquals("Skill div not found", true, Util.isElementPresent(By.xpath("//div[@id='programming-skills']//div[@class='skill-progress' and ./div[@class='skill-title']/h3/text()='" + skillName + "']"), driver));
				WebElement skillDiv = driver.findElement(By.xpath("//div[@id='programming-skills']//div[@class='skill-progress' and ./div[@class='skill-title']/h3/text()='" + skillName + "']"));
				assertEquals("Skill name not found", true, Util.isElementPresent(skillDiv, By.cssSelector("div.skill-title")));
				assertEquals("Wrong skill name", skillName, skillDiv.findElement(By.cssSelector("div.skill-title")).getText());

				assertEquals("Progress div not found", true, Util.isElementPresent(skillDiv, By.cssSelector("div.progress div.progress-bar")));
				assertEquals("Progress div has wrong role", "progressbar", skillDiv.findElement(By.cssSelector("div.progress div.progress-bar")).getAttribute("role"));

				assertEquals("Progress not found", true, Util.isElementPresent(skillDiv, By.cssSelector(".progress-bar span")));
				assertEquals("Progress is not correct", skillProgress + "%", skillDiv.findElement(By.cssSelector(".progress-bar span")).getText());
			}
		}
    }

	@Test
	public void testOtherSkills() throws Exception {
		waitToLoad("testOtherSkills");

		JsonArray otherSkills = getAsArrayFromData("other-skills");
		if (otherSkills == null || otherSkills.size() == 0) {
			assertEquals("Skills should be empty", false, Util.isElementPresent(By.cssSelector("#other-skills div"), driver));
		} else {
			assertEquals("Other Skills title not found", true, Util.isElementPresent(By.xpath("//section[@id='skills']/div/div[@class='skill-chart text-center']/h3"), driver));
			WebElement titleElement = driver.findElement(By.xpath("//section[@id='skills']/div/div[@class='skill-chart text-center']/h3"));
			assertEquals("Other Skills title is wrong", "More skills", titleElement.getAttribute("textContent"));
			assertEquals("Wrong text style on title", "uppercase", titleElement.getCssValue("text-transform"));
            assertEquals("Wrong text size on title", "24px", titleElement.getCssValue("font-size"));


			assertEquals("Other skills div not found", true, Util.isElementPresent(By.id("other-skills"), driver));

			List<WebElement> skills = driver.findElements(By.cssSelector("#skills div.chart"));
			assertEquals("Wrong number of other skills", otherSkills.size(), skills.size());

			int index = 1;
			for (JsonElement skill : otherSkills) {
				JsonObject skillObj = skill.getAsJsonObject();
				String skillName = skillObj.get("name").getAsString();
				int skillProgress = skillObj.get("progress").getAsInt();

                assertEquals(skillName + " skill div doesn't have a crucial class", true, driver.findElement(By.xpath("//div[@id='other-skills']/div[" + index +"]/div")).getAttribute("class").contains("chart"));

				assertEquals(skillName + " skill div not found", true, Util.isElementPresent(By.xpath("(//div[@id='other-skills']//div[@class='chart'])[" + index + "]"), driver));
				WebElement skillDiv = driver.findElement(By.xpath("(//div[@id='other-skills']//div[@class='chart'])[" + index + "]"));
				assertEquals("Skill name not found", true, Util.isElementPresent(skillDiv, By.cssSelector("div.chart-text span")));
				assertEquals("Wrong skill name", skillName, skillDiv.findElement(By.cssSelector("div.chart-text span")).getText());

				assertEquals("Progress span not found", true, Util.isElementPresent(skillDiv, By.cssSelector("span.percent")));

				assertEquals("Progress is not correct", "" + skillProgress, skillDiv.getAttribute("data-percent"));
				index++;
			}
		}
	}

    @Test
    public void testEducation() throws Exception{
        waitToLoad("testEducation");
        JsonArray timelineEducation = getAsArrayFromData("education-timeline");
        int i = 1;
        int lastYear = 0;
        if (timelineEducation != null) {
            for(JsonElement ed : timelineEducation) {
                // finding the elements
                WebElement time = driver.findElement(By.xpath("//ul[@id='education-timeline']/li["+i+"]/div/span"));
                WebElement div = driver.findElement(By.xpath("//ul[@id='education-timeline']/li["+i+"]/div[2]/div"));
                WebElement title = driver.findElement(By.xpath("//ul[@id='education-timeline']/li["+i+"]/div[2]/div/div/h3"));
                WebElement subTitle = driver.findElement(By.xpath("//ul[@id='education-timeline']/li["+i+"]/div[2]/div/div/span"));
                WebElement desc = driver.findElement(By.xpath("//ul[@id='education-timeline']/li["+i+"]/div[2]/div/div[2]/p"));
                //testing if elements exists
                assertEquals("does not have time", false, time == null);
                assertEquals("does not have the main div", false, div == null);
                assertEquals("does not have title", false, title == null);
                assertEquals("does not have subTitle", false, subTitle == null);
                assertEquals("does not have desc", false, desc == null);
                //testing if is the correct data
                assertEquals("the time on html is different from json", true, time.getText().equals(ed.getAsJsonObject().get("when").getAsString()));
                assertEquals("the title on html is different from json", true, title.getText().equals(ed.getAsJsonObject().get("what").getAsString()));
                assertEquals("the subTitle on html is different from json", true, subTitle.getText().equals(ed.getAsJsonObject().get("where").getAsString()));
                assertEquals("the desc on html is different from json", true, desc.getText().equals(ed.getAsJsonObject().get("desc").getAsString().replaceAll("\\<[^>]*>","")));
                //tests if the time is in the correct order
                try {
                    if (lastYear == 0) {
                        lastYear = Integer.parseInt(time.getText().split("-")[0]);
                    } else {
                        int now = Integer.parseInt(time.getText().split("-")[1]);
                        assertEquals("Dates are not in the correct order", true, lastYear >= now);
                    }
                } catch (Exception e) {
                    fail("Time are not in right form, need to be yyyy-yyyy");
                }
                i++;
            }
        } else {
            assertEquals("Does not have education data but exists that section on html", true, ( driver.findElement(By.id("education-title")) == null && driver.findElement(By.id("education-list")) == null));
        }
    }

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
    private void waitToLoad(String method) throws InterruptedException {
        System.out.println("Testing " + method + " [" + memberName + "]");
        driver.get(baseUrl);
        // Wait for JavaScript to load data
        Thread.sleep(200);
    }

    private String getAsStringFromData(String thing) {
        JsonPrimitive obj = data.getAsJsonPrimitive(thing);
        return obj == null ? null : obj.getAsString();
    }

    private JsonArray getAsArrayFromData(String what) {
        return data.getAsJsonArray(what);
    }
}
