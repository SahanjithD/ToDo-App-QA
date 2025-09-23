package com.example.backend.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class SeleniumTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = System.getProperty("frontend.url", "http://localhost:5173");

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void addTask_fromUI_success() {
        driver.get(baseUrl);
        driver.findElement(By.cssSelector("#title")).sendKeys("UI Task");
        driver.findElement(By.cssSelector("#description")).sendKeys("Created via Selenium");
        new Select(driver.findElement(By.cssSelector("#priority"))).selectByVisibleText("HIGH");
        driver.findElement(By.cssSelector("[data-testid='submit-task']")).click();

        WebElement created = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='task-list']//li[contains(.,'UI Task')]")));
        Assertions.assertNotNull(created);
    }

    @Test
    void addTask_fromUI_validationError() {
        driver.get(baseUrl);
        driver.findElement(By.cssSelector("#title")).clear();
        driver.findElement(By.cssSelector("[data-testid='submit-task']")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-title")));
        Assertions.assertTrue(error.getText().length() > 0);
    }
}