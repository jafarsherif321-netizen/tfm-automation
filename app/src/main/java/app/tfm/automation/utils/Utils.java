package app.tfm.automation.utils;

import java.util.Date;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import app.tfm.automation.config.ConfigReader;
import app.tfm.automation.reporting.TestLogManager;

@SuppressWarnings({ "null" })
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
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            clickOnEleByJS(locator);

            js.executeScript("arguments[0].value = '';", element);
            actions.pause(Duration.ofMillis(2000)).perform();
            // js.executeScript("arguments[0].value = arguments[1];", element, value);

            js.executeScript(
                    "arguments[0].focus();" +
                            "arguments[0].dispatchEvent(new KeyboardEvent('keydown', { bubbles: true }));" +
                            "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new KeyboardEvent('keyup', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    element,
                    value);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void sendKeys(By locator, String value) throws Exception {
        try {
            waitForPageToBeStable();
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

            element.sendKeys(Keys.ENTER);
            element.clear();
            // actions.pause(Duration.ofMillis(2000)).perform();
            element.sendKeys(value);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void sendKeysUsingActions(By locator, String value) {
        try {
            waitForPageToBeStable();
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            actions.moveToElement(element)
                    .click()
                    .keyDown(Keys.CONTROL)
                    .sendKeys("a")
                    .keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .pause(2000)
                    .sendKeys(value)
                    .perform();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void enterTextByChar(By locator, String text) throws Exception {
        try {
            waitForPageToBeStable();
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.sendKeys(Keys.ENTER);
            element.clear();

            for (char c : text.toCharArray()) {
                element.sendKeys(String.valueOf(c));
                actions.pause(Duration.ofMillis(100));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void enterTextByCharActions(By locator, String text) throws Exception {
        try {
            waitForPageToBeStable();
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            // actions.moveToElement(element)
            //         .click().pause(Duration.ofMillis(200)).perform();
            // actions.keyDown(Keys.CONTROL)
            //         .sendKeys("a")
            //         .keyUp(Keys.CONTROL)
            //         .sendKeys(Keys.DELETE)
            //         .pause(3000)
            //         .perform();
            element.click();
            element.clear();
            actions.pause(Duration.ofMillis(3000)).perform();

            for (char c : text.toCharArray()) {
                actions.sendKeys(String.valueOf(c)).pause(Duration.ofMillis(350));
            }

            actions.perform();

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
            // Wait until the element is present
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            // Scroll the element into view using JavaScript
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);

            return element;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void hoverUsingJS(By locator) {

        try {
            WebElement element = wait.until(
                    ExpectedConditions.presenceOfElementLocated(locator));
            String hoverScript = "var event = new MouseEvent('mouseover', {" +
                    "view: window, bubbles: true, cancelable: true});" +
                    "arguments[0].dispatchEvent(event);";

            js.executeScript(hoverScript, element);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void switchToFrame(By frameLocator) {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    public void sendKeysInsideFrame(By frameLocator, By fieldLocator, String value) throws Exception {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));

        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(fieldLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", field);
        field.sendKeys(value);

        driver.switchTo().defaultContent();
    }

    public static void logStatus(String description, String status) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "|| " + timestamp + " || " + description + " || " + status + " ||";

        if ("Failed".equalsIgnoreCase(status)) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
        // To log in Extent reports
        TestLogManager.log(message);
    }

    public static void clearOldScreenshots(boolean clearOldSS) {
        if (!clearOldSS) {
            return;
        }
        String dir = ConfigReader.get("screeshotFolderPath");
        File folder = new File(dir);
        try {
            if (folder.exists()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            boolean deleted = file.delete();
                            if (!deleted) {
                                System.err.println("Could not delete screenshot file: " + file.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error while clearing old screenshots: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void captureScreenshot(WebDriver driver, String testName) {
        String dir = ConfigReader.get("screeshotFolderPath");
        File folder = new File(dir);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        File dest = new File(dir + testName + "_" + timestamp + ".png");

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String captureScreenshotBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    // custom wait which waits until one of the elements is visible and will return
    // the 1st visible ele
    public WebElement waitForFirstVisibleElement(By first, By second) {
        return wait.until(driver -> {

            if (!driver.findElements(first).isEmpty()) {
                WebElement el = driver.findElement(first);
                if (el.isDisplayed()) {
                    return el;
                }
            }

            if (!driver.findElements(second).isEmpty()) {
                WebElement el = driver.findElement(second);
                if (el.isDisplayed()) {
                    return el;
                }
            }

            return null;
        });
    }

    public boolean isElementVisible(By locator) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            actions.pause(2000).perform();
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();

        } catch (Exception e) {
            return false;
        }

    }

    public void clickOnElement(By locator) {
        final int maxStaleRetries = 3;
        waitForPageToBeStable();
        for (int attempt = 1; attempt <= maxStaleRetries; attempt++) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                return;
            } catch (StaleElementReferenceException stale) {
                logStatus(
                        "Stale element while clicking ON" + locator + " (attempt " + attempt + "/" + maxStaleRetries
                                + ")",
                        "Warning");
                if (attempt == maxStaleRetries) {
                    logStatus("Click failed after stale element retries, trying JS click for: " + locator, "Failed");
                    clickOnEleByJS(locator); // fallback
                    return;
                }
            } catch (Exception e) {
                logStatus("Normal click failed, trying JS click for: " + locator, "Failed");
                clickOnEleByJS(locator); // fallback
                return;
            }
        }
    }

    public void clickOnEleByJS(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollIntoViewJS(locator); //
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JS Click also failed for: " + locator);
        }
    }

    public void waitForPageToBeStable() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait for DOM to fully load
            shortWait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .equals("complete"));

            // Wait for overlays / backdrops / loaders to disappear
            By overlayXpath = By.xpath(
                    "//*[contains(@class,'MuiBackdrop-root') " +
                            "or contains(@class,'MuiDialog-container') " +
                            "or contains(@class,'loading') " +
                            "or contains(@class,'spinner') " +
                            "or contains(@class,'overlay')]");

            shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));

            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(overlayXpath));

        } catch (Exception e) {
            // Do not fail test because of wait — just log
            // System.out.println("Page stability wait skipped: " + e.getMessage());
        }
    }

}
