package app.tfm.automation.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@SuppressWarnings({ "unused", "null" })
public class Utils {
    private WebDriver driver;
    WebDriverWait wait;
    private Actions actions;
    JavascriptExecutor js;

    public Utils(WebDriver driver) {
        try {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            this.actions = new Actions(driver);
            this.js = (JavascriptExecutor) driver;

        } catch (Exception e) {
            System.out.println("Error: while initilizing the actions class " + e.getMessage());
        }

    }

    public void sendKeysUsingJS(By locator, String value) throws Exception {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            js.executeScript("arguments[0].value = '';", element);
            js.executeScript("arguments[0].value = arguments[1];", element, value);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void sendKeys(By locator, String value) throws Exception {
    try {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

        element.click();     
        element.clear(); 
        Thread.sleep(2000);    
        element.sendKeys(value);

    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
}


    public void sendKeysUsingActions(By locator, String value) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            actions.moveToElement(element)
                    .click()
                    .keyDown(Keys.CONTROL)
                    .sendKeys("a")
                    .keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .sendKeys(value)
                    .perform();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void enterTextByChar(By locator, String text) throws Exception {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.click();
            element.clear();
            Thread.sleep(800);
            for (char c : text.toCharArray()) {
                Thread.sleep(100);
                element.sendKeys(String.valueOf(c));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void enterTextByCharActions(By locator, String text) throws Exception {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            actions.moveToElement(element).click();
            Thread.sleep(1000);

            for (char c : text.toCharArray()) {
                Thread.sleep(800);
                actions.sendKeys(String.valueOf(c));
            }

            actions.build().perform();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public WebElement scrollIntoView(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            actions.moveToElement(element).perform();
            return element;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public WebElement scrollIntoViewJS(By locator) {
        try {
            // Wait until the element is present and visible
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // Scroll the element into view using JavaScript
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);

            return element;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void logStatus(String description, String status) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "|| " + timestamp + " || " + description + " || " + status + " ||";

        if ("Failed".equalsIgnoreCase(status)) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }

    //write method to click on an element - takes locator
}
