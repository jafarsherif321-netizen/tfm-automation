package app.tfm.automation.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@SuppressWarnings("unused")
public class Utils {
    private WebDriver driver;
    WebDriverWait wait;
    private Actions actions;

    public Utils(WebDriver driver) {
        try{
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            this.actions = new Actions(driver);
        } catch (Exception e) {
            System.out.println("Error: while initilizing the actions class "+e.getMessage());
        }
        
    }

    public void enterTextByChar(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
        }
    }

    public void enterTextByCharActions(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        actions.moveToElement(element).click();

        for (char c : text.toCharArray()) {
            actions.sendKeys(String.valueOf(c));
        }

        actions.build().perform();
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

    public static void logStatus(String description, String status) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "|| "+timestamp + " || " + description + " || " + status+" ||";

        if ("Failed".equalsIgnoreCase(status)) {
            System.err.println(message); 
        } else {
            System.out.println(message); 
        }
    }

}
