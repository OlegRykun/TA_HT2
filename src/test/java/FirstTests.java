import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.Keys.ENTER;
import static org.testng.Assert.assertTrue;

public class FirstTests {
    private WebDriver driver;

    @BeforeTest
    public void profileSetUp(){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
    }

    @BeforeMethod
    public void testSetUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatUrlContainsSearchWord(){
        driver.findElement(By.xpath("//input[@id='input_search']"))
                .sendKeys("iPhone 11", ENTER);
        assertTrue(driver.getCurrentUrl().contains("query=iPhone"));
    }

    @Test(priority = 2)
    public void checkThatAfterWrongSearchDataGivesNoResult(){
        driver.findElement(By.xpath("//input[@id='input_search']"))
                .sendKeys("asdfgh", ENTER);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        assertTrue(driver.findElement(By.xpath("//div[@class='row js_more_content js_height-block']")).getText().contains("Ничего не найдено"));
    }

    @Test(priority = 3)
    public void checkThatYouCantPlaceAnOrderWithEmptyCart(){
        driver.findElement(By.xpath("//div[@class='header-bottom__right-icon']/i[@class='icon icon-cart-new']")).click();
        driver.findElement(By.xpath("//a[text()='Оформить заказ']']")).click();
        assertTrue(driver.findElement(By.xpath("//div[@id='modalAlert']")).getText().contains("Чтобы сделать покупку, нужно добавить товары в корзину."));
    }

    @Test(priority = 4)
    public void checkThatProductsPriceInsideLimits(){
        driver.findElement(By.xpath("//input[@id='input_search']"))
                .sendKeys("iPhone", ENTER);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//input[@class='form-control form-control-min']")).clear();
        driver.findElement(By.xpath("//input[@class='form-control form-control-min']")).sendKeys("5000");
        driver.findElement(By.xpath("//input[@class='form-control form-control-max']")).clear();
        driver.findElement(By.xpath("//input[@class='form-control form-control-max']")).sendKeys("10000");
        driver.findElement(By.xpath("//div[@class='form-group filter-group js_filter_parent open-filter-tooltip']//span[@class='filter-tooltip-inner']"))
                .click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        List<WebElement> phonesPrice = driver.findElements(By.xpath("//div[@class='row js_more_content js_height-block']//div[@class='prod-cart__prise-new']"));
        for(WebElement phonePrice:phonesPrice){
            String temp = phonePrice.getText().substring(0, phonePrice.getText().indexOf(' '));
            assertTrue(5000 <= Integer.parseInt(temp) && Integer.parseInt(temp) <= 10000);
        }
    }

    @Test(priority = 5)
    public void checkErrorOnLoginToNonexistentAccount(){
        driver.findElement(By.xpath("//div[@class='header-bottom search_mobile_display']//i[@class='icon icon-user-big']")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//div[@class='sign-holder clearfix']//input[@name='login']"))
                .sendKeys("1234567890");
        driver.findElement(By.xpath("//input[@class='validate show-password']"))
                .sendKeys("qwer1234567890");
        driver.findElement(By.xpath("//button[@class='button-reset main-btn submit main-btn--green']")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        assertTrue(driver.findElement(By.xpath("//div[@id='modalAlert']")).getText().contains("Ошибка"));
    }

    @AfterMethod
    public void tearDown(){
        driver.close();
    }
}
