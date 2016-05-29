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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static javax.swing.text.html.CSS.getAttribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 27/05/2016.
 */
@RunWith(Parameterized.class)
public class CommonTests {
    //members
    private static final String ELIAS_NAME = "elias";
    private static final String NUNO_NAME = "nuno";
    private static final String RAFAEL_NAME = "rafael";

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
    private String base_url;
    private JsonObject data;

    //lists
    private List<String> menu;

    private String memberName;

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new String[] {
                NUNO_NAME ,
                ELIAS_NAME /*,
                RAFAEL_NAME*/
        });
    }

    public CommonTests(String memberName) {
        menuPopulation();
        base_url = Util.getBaseUrl() + "/member.html?name=";
        this.memberName = memberName;
        try {
            data = Util.getJsonObject("/site/json/" + memberName + ".json");
        } catch (Exception e){

        }
    }

    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait((base_url.contains("localhost") ? 5 : 30), TimeUnit.SECONDS);
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
        driver.get(base_url + memberName);
        assertEquals("Title of member " + memberName + " is wrong:" + driver.getCurrentUrl(), "I AM " + memberName.toUpperCase(), driver.getTitle());
    }

    @Test
    public void testHeader() throws Exception {
        driver.get(base_url + memberName);
        assertEquals("Title of member " + memberName + " is wrong:" + driver.getCurrentUrl(), "I AM " + memberName.toUpperCase(), driver.getTitle());
        assertEquals("Error while test headers on " + memberName + " page", "i am " + memberName, driver.findElement(By.className("intro-sub")).getText().toLowerCase());
    }

    @Test
    public void testProfessionTitle() throws Exception {
        waitToLoad();
        assertEquals(memberName + " page title is different than expected", true, ( driver.findElement(By.xpath("//section[@id='home']/div/h1")).getText().equals(TITLE) ));
    }

    @Test
    public void testIfClickIAMXGoToMainPage() {
        driver.get(base_url + memberName);
        driver.findElement(By.xpath("//img")).click();
        assertEquals("I AM X link does not work on " + memberName + " page", true, (driver.getCurrentUrl().contains("index.html") && driver.getTitle().equals("We are awesome")));
    }

    @Test
    public void testIfHaveIntuitiveThingToUserKnowHeNeedsScrollDown() {
        driver.get(base_url + memberName);
        assertEquals("Does not have the wheel on " + memberName + " page", true, (driver.findElement(By.className("wheel")) != null));

    }

    /*@Test
    public void testIfHaveMenu() throws Exception {
        driver.get(base_url + memberName);
        for (String m : menu) {
            assertEquals("Menu " + m + "does not exists on "  + memberName + " page", true, (driver.findElement(By.linkText(WordUtils.capitalize(m))) != null));
        }
    }*/


    @Test
    public void testIfHaveMenu() throws Exception {
        driver.get(base_url + memberName);
        assertEquals("Missed about menu on " + memberName + " page", true, Util.isElementPresent(By.linkText("About"), driver));
        assertEquals("Missed home menu on " + memberName + " page",true, Util.isElementPresent(By.linkText("Home"), driver));
        assertEquals("Missed skills menu on " + memberName + " page",true, Util.isElementPresent(By.linkText("Skills"), driver));
        assertEquals("Missed resume menu on " + memberName + " page",true, Util.isElementPresent(By.linkText("Resume"), driver));

        ArrayList<String> sections = new ArrayList<String>();
        sections.add(driver.findElement(By.linkText("About")).getAttribute("href").split("#")[1]);
        sections.add(driver.findElement(By.linkText("Home")).getAttribute("href").split("#")[1]);
        sections.add(driver.findElement(By.linkText("Skills")).getAttribute("href").split("#")[1]);
        sections.add(driver.findElement(By.linkText("Resume")).getAttribute("href").split("#")[1]);

        for (String section: sections) {
            if(!Util.isElementPresent(By.id(section), driver)){
                fail("Section '" + section + "' is not present in the document on " + memberName + " page");
            }

            List<WebElement> sectionList = driver.findElements(By.id(section));
            if (sectionList.size() > 1)
                fail("Multiple sections found for '" + section + "' on " + memberName + " page");
        }
        driver.findElement(By.className("navbar-brand")).click();
        assertEquals("We are awesome", driver.getTitle());

    }


    @Test
    public void testIfHaveSectionForEachMenu() throws Exception {
        driver.get(base_url + memberName);
        for (String m : menu) {
            assertEquals("Section " + m + "does not exists on " + memberName + " page", true, (driver.findElement(By.xpath("//section[@id='" + m + "']")) != null));
        }
    }

    @Test
    public void testIfHaveHireSection() {
        driver.get(base_url + memberName);
        assertEquals("does not have a hire section on " + memberName + " page", true, (driver.findElement(By.className("hire-section")) != null));
    }

    @Test
    public void testSectionOrder() throws Exception {
        driver.get(base_url + memberName);
        assertEquals("Section home is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HOME_ORDER + "]")).getAttribute("id").equals("home")));
        assertEquals("Section about is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_ABOUT_ORDER + "]")).getAttribute("id").equals("about")));
        assertEquals("Section skills is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_SKILLS_ORDER + "]")).getAttribute("id").equals("skills")));
        assertEquals("Section resume is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_RESUME_ORDER + "]")).getAttribute("id").equals("resume")));
        assertEquals("Section hire is not on the right order on " + memberName + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HIRE_ORDER + "]")).getAttribute("class").contains("hire-section")));

    }

    @Test
    public void testFacebookClick() throws Exception {
        driver.get(base_url + memberName);
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
        driver.get(base_url + memberName);
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


   /* @Test
    public void testNavbarHover() throws Exception {
        for(String member : members) {
            driver.get(base_url + member + ".html");
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

    //helper functions
    private void menuPopulation() {
        menu = new ArrayList<String>();
        menu.add("home");
        menu.add("about");
        menu.add("skills");
        menu.add("resume");
    }


    //TODO: ESTA FUNCAO E MESMO NECESSRARIO ? NAO HA OUTRA FORMA? PQ A NECESISIDADE DISTO ?
    //helper funtions
    private void waitToLoad() throws InterruptedException {
        driver.get(base_url);
        Thread.sleep(1000);
    }

    private String getAsStringFromData(String thing) {
        return data.getAsJsonPrimitive(thing).getAsString();
    }

}
