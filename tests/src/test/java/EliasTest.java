import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

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
    @Before
    public void setUp() throws Exception {
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
        driver.get(base_url);
        assertEquals("I AM ELIAS", driver.getTitle());
    }
}
