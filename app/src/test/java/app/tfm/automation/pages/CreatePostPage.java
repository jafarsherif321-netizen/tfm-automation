package app.tfm.automation.pages;

import java.time.Duration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import app.tfm.automation.utils.Utils;

@SuppressWarnings({ "unused", "null" })
public class CreatePostPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;

    // Constructor
    public CreatePostPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By social = By.xpath("//span[text()='Social']");
    private By plusBtn = By.xpath("//span[@data-testid='create-post-button']");
    private By mediaLocator = By.xpath("//input[@type='file' and contains(@accept,'image') and @multiple]");
    private By continueBtn= By.xpath("//div[contains(@class, 'desktop-continue')]//button[text()='Continue']");
    private By plsWaitUpload = By.xpath("//p[@class='upload-loader-subtext']");
    private By caption = By.xpath("//textarea[@name='description']");
    private By createPostBtn = By.xpath("//button[text()='Create Post']");
    private By snackBar = By.xpath("//span[@id='client-snackbar' and contains(.,'Post created successfully')]");
    private By postTab = By.xpath("//button[@data-testid='posts-tab']");




    // Logics

    private final String[] STARTERS = {
            "Today felt like a", "Living for this", "Caught in a",
            "Just another", "Smiling through this", "Soaking in this",
            "This moment is pure", "Nothing beats a", "Falling in love with this",
            "Taking in the"
    };

    private final String[] MOODS = {
            "peaceful moment", "beautiful vibe", "happy memory",
            "slow day", "golden hour", "late night mood",
            "fresh start", "simple joy", "calm energy", "weekend feeling"
    };

    private final String[] ENDINGS = {
            "and I'm here for it.",
            "that I didn't want to end.",
            "that made my day better.",
            "worth remembering forever.",
            "that feels just right.",
            "that hits differently.",
            "and it feels amazing.",
            "and I'm loving every second.",
            "and I needed this.",
            "that brought a smile to my face."
    };

    private final String[] HASHTAGS = {
            "#Life", "#Moments", "#GoodVibes", "#DailyLife",
            "#WeekendMood", "#StayPositive", "#SimpleThings",
            "#Memories", "#HappyTimes", "#JustLiving",
            "#Peaceful", "#Grateful", "#ChillVibes", "#Smiles",
            "#PhotoOfTheDay"
    };

    private final String[] EMOJIS = {

            "★", "☆", "✦", "✧", "✩", "✪", "✫", "✬", "✭", "✮", "✯", "✰",
            "⋆", "⍟", "⭑", "⭒", "❋", "✱", "✲", "✳", "✴", "✵", "✶", "✷", "✸", "✹", "✺", "✻",
            "☺", "☻", "☹", "♡", "♥", "❥", "❣", "❦", "❧",
            "☀", "☁", "☂", "☃", "☄", "⚘", "❀", "✿", "❁",
            "⚡", "♫", "♪", "♬", "✈", "⌛", "⏰", "∞",
            "☾", "☽", "❂", "※"
    };

    private final Random random = new Random();
    private final Set<String> usedCaptions = new HashSet<>();

    public String generateCaption() {
        String caption;
        int attempts = 0;
        int maxAttempts = 6000; // safety limit to avoid infinite loop 

        do {
            String starter = STARTERS[random.nextInt(STARTERS.length)];
            String mood = MOODS[random.nextInt(MOODS.length)];
            String ending = ENDINGS[random.nextInt(ENDINGS.length)];
            String emoji = EMOJIS[random.nextInt(EMOJIS.length)];

            // Add 1–3 random hashtags
            int tagCount = 1 + random.nextInt(3);
            Set<String> tags = new HashSet<>();
            while (tags.size() < tagCount) {
                tags.add(HASHTAGS[random.nextInt(HASHTAGS.length)]);
            }
            caption = String.join(" ", tags) + "\n" + starter + " " + mood + " " + ending + " " + emoji;

            attempts++;

            if (attempts > maxAttempts) {
                throw new RuntimeException("Caption pool exhausted!: The max no of possible combinations for caption has been reached, stopping loop to avoid infinite loop");
            }

        } while (usedCaptions.contains(caption)); // Runs next loop if the caption is not unique

        usedCaptions.add(caption);
        return caption;
    }


    public boolean createPost(){
        try {
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(social)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(plusBtn)).click();
        WebElement mediaInput = wait.until(ExpectedConditions.presenceOfElementLocated(mediaLocator));
        js.executeScript("arguments[0].style.display='block';", mediaInput); //unloack dom ele 
    
        String filePath0 = "C:\\Users\\3embed\\Videos\\merkaru\\app1.jpg";
        String filePath1 = "C:\\Users\\3embed\\Videos\\merkaru\\app2.jpg";
        String filePath2 = "C:\\Users\\3embed\\Videos\\merkaru\\app5.mp4";

        mediaInput.sendKeys(filePath0+"\n"+filePath1+"\n"+filePath2);

        wait.until(ExpectedConditions.visibilityOfElementLocated(continueBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(plsWaitUpload));
        wait = new WebDriverWait(driver, Duration.ofSeconds(150));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(plsWaitUpload));
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        String captionText = generateCaption();
        wait.until(ExpectedConditions.visibilityOfElementLocated(caption)).sendKeys(captionText);
         
        Utils utils = new Utils(driver);
        WebElement ele = utils.scrollIntoViewJS(createPostBtn);
        js.executeScript("arguments[0].click();", ele);
        
        status = wait.until(ExpectedConditions.visibilityOfElementLocated(snackBar)).isDisplayed();
        wait.until(ExpectedConditions.visibilityOfElementLocated(postTab)).click();

        Utils.logStatus("User successfully created a post", (status ? "Passed" : "Failed"));
        return status;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
