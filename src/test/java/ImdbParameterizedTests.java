import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ImdbParameterizedTests {
    private static WebDriver driver;
    private static String baseUrl;

    private String searchTerm;

    @Parameterized.Parameters(name = "Movie {index}: {0}")
    public static Collection movieTitles() {
        ArrayList<String> res = new ArrayList();
        res.add("Blade Runner");
        res.add("One from the Heart");
        res.add("The Big Lebowski");
        res.add("Groundhog Day");
        res.add("Before Sunrise");

        return res;
    }
//ghjgjhf
    public ImdbParameterizedTests(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Logger.getLogger("").setLevel(Level.OFF);
        driver = new HtmlUnitDriver();
        baseUrl = "http://www.imdb.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testImdbSearch() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("navbar-query")).click();
        driver.findElement(By.id("navbar-query")).clear();
        driver.findElement(By.id("navbar-query")).sendKeys(searchTerm);
        driver.findElement(By.id("navbar-submit-button")).click();
        driver.findElement(By.xpath("//td[2]/a")).click();
        assertTrue(driver.getTitle().matches(".*" + searchTerm + ".*"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }
}