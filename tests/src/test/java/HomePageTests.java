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
        assertEquals(true, isElementPresent(By.linkText("Nuno")));
        assertEquals("Nuno", driver.findElement(By.linkText("Nuno")).getText());
    }



   /* @Test
    public void test() throws Exception {
        driver.get("http://qs-ner.westeurope.cloudapp.azure.com:8080");
        assertEquals("Hello World!", driver.getTitle());
        // Warning: assertTextPresent may require manual changes
        assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Sup mates![\\s\\S]*$"));
    }*/

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
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
