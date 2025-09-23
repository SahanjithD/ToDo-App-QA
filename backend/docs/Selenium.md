## Selenium UI Tests: Two Scenarios

We will automate two frontend scenarios using Selenium WebDriver:
1) Add a new task from UI
2) Validate error when submitting empty title

Assumptions:
- Frontend served locally (e.g., Vite dev server) at `http://localhost:5173` or configured host.
- Elements: title input `#title`, description `#description`, priority select `#priority`, submit button `[data-testid="submit-task"]`, list `#task-list`.

### Setup

Add Maven dependencies to `pom.xml`:
```xml
<dependencies>
  <dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.24.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

Screenshot prompts:
- Screenshot 1: `pom.xml` with Selenium deps.

### Test Class

Create `src/test/java/com/example/backend/ui/SeleniumTests.java`:
```java
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
```

Screenshot prompts:
- Screenshot 2: Test class open in IDE.
- Screenshot 3: Running tests in terminal/IDE.

### Run Tests

Start frontend in another terminal, then run:
```bash
.\\mvnw.cmd --% -Dtest=... test
```

Screenshot prompts:
- Screenshot 4: Browser automation in action (optional) and green results.

### Notes
- Adjust selectors to match your actual frontend.
- You can pass `-Dfrontend.url=http://localhost:5174` to point to a different port.


