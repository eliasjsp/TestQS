import org.apache.commons.lang3.text.WordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class HomePageTests {
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    private static final int ELIAS_POSITION = 0;
    private static final int NUNO_POSITION = 1;
    private static final int RAFAEL_POSITION = 2;

    private static final String ELIAS_NAME = "elias";
    private static final String NUNO_NAME = "nuno";
    private static final String RAFAEL_NAME = "rafael";

    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

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
        assertEquals(3, driver.findElements(By.xpath("//div[2]/div/*[contains(@class, 'col-md-4 text-center thumb-wrapper')]")).size());
    }

    @Test
    public void testIfExistsNunoMember() throws Exception {
        driver.get(Util.getBaseUrl());
        existsMemberButton(NUNO_NAME);
        existsMemberImage(NUNO_NAME, NUNO_POSITION);
    }

    @Test
    public void testIfExistsRafaelMember() throws Exception {
        driver.get(Util.getBaseUrl());
        existsMemberButton(RAFAEL_NAME);
        existsMemberImage(RAFAEL_NAME, RAFAEL_POSITION);
    }

    @Test
    public void testIfExistsEliasMember() throws Exception {
        driver.get(Util.getBaseUrl());
        existsMemberButton(ELIAS_NAME);
        existsMemberImage(ELIAS_NAME, ELIAS_POSITION);
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
    private void existsMemberButton(String member){
        member = WordUtils.capitalize(member);
        assertEquals(true, isElementPresent(By.linkText(member)));
        assertEquals(member, driver.findElement(By.linkText(member)).getText());
    }

    private void existsMemberImage(String member, int position){
        assertEquals(true, isElementPresent(By.cssSelector("img[alt='" + WordUtils.capitalize(member) + "']")));
        assertEquals(Util.getBaseUrl() + "/" + member + ".html", driver.findElements(By.xpath("//div[@class='demo-thumb']/a")).get(position).getAttribute("href"));
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
