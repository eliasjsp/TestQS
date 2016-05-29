import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.xml.internal.bind.api.impl.NameConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import sun.nio.cs.StandardCharsets;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Elias on 23/05/2016.
 */
public class EliasTest {

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String base_url = Util.getBaseUrl() + "/" + "elias.html";
    private static final String TITLE = "Software Engineer";
    private JsonObject data;

    public EliasTest() throws Exception {
        JsonParser jp = new JsonParser();
        Path path = Paths.get(EliasTest.class.getResource(".").toURI());
        data = (JsonObject) jp.parse(readFile(path.getParent().getParent().getParent()+"/site/json/elias.json", Charset.defaultCharset()));
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    private String readFile(String path, Charset encoding)throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
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
    public void testFacebookClick() throws Exception {
        waitToLoad();
        WebElement face = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li/a"));
        System.out.println(face);
        assertEquals(getAsStringFromData("home-facebook"), face.getAttribute("href"));
        face.click();
        assertEquals(2, (new ArrayList<String> (driver.getWindowHandles())).size());
    }
    @Test
    public void testLikedInClick() throws Exception {
        waitToLoad();
        WebElement linkedin = driver.findElement(By.xpath("//section[@id='home']/div/div[2]/ul/li[2]/a"));
        assertEquals(getAsStringFromData("home-linkedin"), linkedin.getAttribute("href"));
        linkedin.click();
        assertEquals(2, (new ArrayList<String> (driver.getWindowHandles())).size());
    }

    @Test
    public void testProfessionTitle() throws Exception {
        waitToLoad();
        assertEquals("Elias page title is different than expected", true, ( driver.findElement(By.xpath("//section[@id='home']/div/h1")).getText().equals(TITLE) ));
    }

    /*@Test
    public void testIfHaveButtonToUserScrollUp() {
        driver.get(base_url);
        WebElement scrollUp = null;
        try {
            scrollUp = driver.findElement(By.xpath("//body/div[3]/a"));
        } catch (Exception e) {}
        //System.out.println(scrollUp.getCssValue("display"));
        System.out.println(scrollUp.getAttribute("style"));
        System.out.println("Text" + scrollUp.getText());
        if(true) {
            for(String m : menu) {
                driver.findElement(By.linkText(WordUtils.capitalize(m))).click();
                if(m.equals("home"))
                    assertEquals("Scroll up is displayed on " + m + " section", true, scrollUp == null );
                else {
                    try {
                        scrollUp = driver.findElement(By.xpath("//body/div[3]/a"));
                    } catch (Exception e) {}
                    assertEquals("Scroll up not displayed on " + m + " section", true, scrollUp.isDisplayed());
                    scrollUp.click();
                    assertEquals("Scroll up is displayed on " + m + " section", false, scrollUp.isDisplayed() );
                }
            }
        } else {
            assertEquals("Does not have the scroll up", true, false);
        }

    }*/

    @Test
    public void testPersonalInfo() throws Exception {
        waitToLoad();
        WebElement sectionTitle = driver.findElement(By.id("about-title"));
        assertEquals(false, sectionTitle == null);

        WebElement photo = driver.findElement(By.cssSelector("#about .myphoto img"));
        assertEquals(false, photo == null);
        assertEquals(Util.getBaseUrl() + "/assets/images/elias.jpg", photo.getAttribute("src"));

        assertEquals(getAsStringFromData("name"), driver.findElement(By.id("name")).getText());
        assertEquals(getAsStringFromData("bday"), driver.findElement(By.id("bday")).getText());
        assertEquals(getAsStringFromData("address"), driver.findElement(By.id("address")).getText());
        assertEquals(getAsStringFromData("nationality"), driver.findElement(By.id("nationality")).getText());
        assertEquals(getAsStringFromData("phone"), driver.findElement(By.id("phone")).getText());
        assertEquals(getAsStringFromData("email"), driver.findElement(By.id("email")).getText());
    }



    //helper funtions
    private void waitToLoad() throws InterruptedException {
        driver.get(base_url);
        Thread.sleep(1000);
    }

    private String getAsStringFromData(String thing) {
        return data.getAsJsonPrimitive(thing).getAsString();
    }
}
