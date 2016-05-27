import org.apache.commons.lang3.text.WordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 27/05/2016.
 */
public class CommonTests {
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
    private static final int SECTION_HOME_ORDER = 0;
    private static final int SECTION_ABOUT_ORDER = 1;
    private static final int SECTION_SKILLS_ORDER = 2;
    private static final int SECTION_RESUME_ORDER = 3;
    private static final int SECTION_CONTACT_ORDER = 4;

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String base_url;
    private List<String> menu;
    private List<String> members;

    @Before
    public void setUp() throws Exception {
        menuPopulation();
        memberPopulation();
        base_url = Util.getBaseUrl() + "/";
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
        for(String m : members) {
            driver.get(base_url + m + ".html");
            assertEquals("Title of member " + m + "is wrong", "I AM " + m.toUpperCase(), driver.getTitle());
        }
    }

    @Test
    public void testIfClickIAMXGoToMainPage() {
        for(String m : members) {
            driver.get(base_url + m + ".html");
            driver.findElement(By.xpath("//img")).click();
            assertEquals("I AM X link does not work on " + m + " page", true, (driver.getCurrentUrl().contains("index.html") && driver.getTitle().equals("We are awesome")));
        }
    }

    @Test
    public void testIfHaveIntuitiveThingToUserKnowHeNeedsScrollDown() {
        for(String m : members) {
            driver.get(base_url + m + ".html");
            assertEquals("Does not have the wheel on " + m + " page", true, (driver.findElement(By.className("wheel")) != null));
        }
    }

    @Test
    public void testIfHaveMenu() throws Exception {
        for(String member : members) {
            driver.get(base_url + member + ".html");
            for (String m : menu) {
                assertEquals("Menu " + m + "does not exists on "  + member + " page", true, (driver.findElement(By.linkText(WordUtils.capitalize(m))) != null));
            }
        }
    }

    @Test
    public void testIfHaveSectionForEachMenu() throws Exception {
        for(String member : members) {
            driver.get(base_url + member + ".html");
            for (String m : menu) {
                assertEquals("Section " + m + "does not exists on " + member + " page", true, (driver.findElement(By.xpath("//section[@id='" + m + "']")) != null));
            }
        }
    }

    //helper functions
    private void menuPopulation() {
        menu = new ArrayList<String>();
        menu.add("home");
        menu.add("about");
        menu.add("skills");
        menu.add("resume");
        menu.add("contact");
    }

    private void memberPopulation() {
        members = new ArrayList<String>();
        members.add(NUNO_NAME);
        members.add(ELIAS_NAME);
        members.add(RAFAEL_NAME);
    }
}
