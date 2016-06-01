import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.commons.lang3.text.WordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class HomePageTests {
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    private static final int NUMBER_OF_MEMBERS = 3;

    private static final int ELIAS_POSITION = 0;
    private static final int NUNO_POSITION = 1;
    private static final int RAFAEL_POSITION = 2;

    private static final String ELIAS_NAME = "elias";
    private static final String NUNO_NAME = "nuno";
    private static final String RAFAEL_NAME = "rafael";

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
        driver.manage().timeouts().implicitlyWait((Util.getBaseUrl().contains("localhost") ? 5 : 30), TimeUnit.SECONDS);

        Logger.getLogger("").setLevel(Level.OFF);
    }

    //general page tests
    @Test
    public void testTitle() throws Exception {
        driver.get(Util.getBaseUrl());
        assertEquals("We are awesome", driver.getTitle());
    }

    @Test
    public void testPageBanner() throws Exception {
        driver.get(Util.getBaseUrl());
        assertEquals("We are awesome", driver.findElement(By.xpath("//h1[1]")).getText());
        assertEquals("Three programmers guys", driver.findElement(By.cssSelector("div.intro-desc")).getText());
    }

    @Test
    public void testNumberOfTeamMembers() throws Exception {
        driver.get(Util.getBaseUrl());
        assertEquals(NUMBER_OF_MEMBERS, driver.findElements(By.xpath("//div[2]/div/*[contains(@class, 'col-md-4 text-center thumb-wrapper')]")).size());
    }
    //end general page tests

    //nuno testes
    @Test
    public void testIfExistsNunoMember() throws Exception {
        driver.get(Util.getBaseUrl());
        existsMemberButton(NUNO_NAME);
        existsMemberImage(NUNO_NAME, NUNO_POSITION);
    }
    //end nuno tests

    //rafael tests
    @Test
    public void testIfExistsRafaelMember() throws Exception {
        driver.get(Util.getBaseUrl());
        existsMemberButton(RAFAEL_NAME);
        existsMemberImage(RAFAEL_NAME, RAFAEL_POSITION);
    }
    //end rafael tests

    //elias tests
    @Test
    public void testIfExistsEliasMember() throws Exception {
        driver.get(Util.getBaseUrl());
        existsMemberButton(ELIAS_NAME);
        existsMemberImage(ELIAS_NAME, ELIAS_POSITION);
    }

    /*@Test
    public void testIfExistsEliasPage() throws Exception {
        driver.get(Util.getBaseUrl());
        existsMemberPage(ELIAS_NAME);
    }*/
    //end elias tests


    @Test
    public void testCSSImages() throws Exception {
        waitToLoad("testCSSImages");
        for (WebElement element : driver.findElements(By.xpath("//img"))){
            assertEquals("Wrong image border-radius css", "50%", element.getCssValue("border-radius"));
            assertEquals("Wrong image width", "middle", element.getCssValue("vertical-align"));
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    //helper functions
    private void existsMemberButton(String member) {
        member = WordUtils.capitalize(member);
        assertEquals(true, Util.isElementPresent(By.linkText(member), driver));
        assertEquals(member, driver.findElement(By.linkText(member)).getText());
    }

    private void existsMemberImage(String member, int position) {
        assertEquals(true, Util.isElementPresent(By.cssSelector("img[alt='" + WordUtils.capitalize(member) + "']"), driver));
        assertEquals(Util.getBaseUrl() + "/member.html?name=" + member, driver.findElements(By.xpath("//div[@class='demo-thumb']/a")).get(position).getAttribute("href"));
    }

    private void existsMemberPage(String member) {
        driver.get(Util.getBaseUrl() + "/");
        driver.findElement(By.linkText(WordUtils.capitalize(member))).click();
        assertEquals("I AM " + member.toUpperCase(), driver.getTitle());
    }

    //helper funtions
    private void waitToLoad(String method) throws InterruptedException {
        System.out.println("Testing " + method );
        driver.get(Util.getBaseUrl());
        // Wait for JavaScript to load data
        Thread.sleep(200);
    }

}
