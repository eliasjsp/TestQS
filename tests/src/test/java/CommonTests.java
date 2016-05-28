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

import static javax.swing.text.html.CSS.getAttribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 27/05/2016.
 */
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
    private static final int SECTION_CONTACT_ORDER = 6;

    //other variables needed
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String base_url;

    //lists
    private List<String> menu;
    private List<String> members;

    @Before
    public void setUp() throws Exception {
        menuPopulation();
        memberPopulation();
        base_url = Util.getBaseUrl() + "/";
        //driver = new FirefoxDriver();
        driver = new HtmlUnitDriver();
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

    @Test
    public void testIfHaveHireSection() {
        for(String member: members){
            driver.get(base_url + member + ".html");
            assertEquals("does not have a hire section on " + member + " page", true, (driver.findElement(By.className("hire-section")) != null));
        }
    }

    @Test
    public void testSectionOrder() throws Exception {
        for(String member : members) {
            driver.get(base_url + member + ".html");
            assertEquals("Section home is not on the right order on " + member + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HOME_ORDER + "]")).getAttribute("id").equals("home")));
            assertEquals("Section about is not on the right order on " + member + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_ABOUT_ORDER + "]")).getAttribute("id").equals("about")));
            assertEquals("Section skills is not on the right order on " + member + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_SKILLS_ORDER + "]")).getAttribute("id").equals("skills")));
            assertEquals("Section resume is not on the right order on " + member + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_RESUME_ORDER + "]")).getAttribute("id").equals("resume")));
            assertEquals("Section hire is not on the right order on " + member + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_HIRE_ORDER + "]")).getAttribute("class").contains("hire-section")));
            assertEquals("Section contact is not on the right order on " + member + " page", true, (driver.findElement(By.xpath("//section[" + SECTION_CONTACT_ORDER + "]")).getAttribute("id").equals("contact")));
        }
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
        menu.add("contact");
    }

    private void memberPopulation() {
        members = new ArrayList<String>();
        members.add(NUNO_NAME);
        members.add(ELIAS_NAME);
        members.add(RAFAEL_NAME);
    }
}
