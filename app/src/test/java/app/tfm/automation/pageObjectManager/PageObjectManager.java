package app.tfm.automation.pageObjectManager;

import org.openqa.selenium.WebDriver;

import app.tfm.automation.pages.CartPage;
import app.tfm.automation.pages.CheckoutPage;
import app.tfm.automation.pages.CreatePostPage;
import app.tfm.automation.pages.HomePage;
import app.tfm.automation.pages.LoginPage;
import app.tfm.automation.pages.OrdersPage;
import app.tfm.automation.pages.ProductDetailsPage;
import app.tfm.automation.pages.SignUpPage;

public class PageObjectManager {
    private WebDriver driver;
    private LoginPage loginPage;
    private SignUpPage signUpPage;
    private HomePage homePage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private OrdersPage ordersPage;
    private CreatePostPage createPostPage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage loginPage() {
        if (loginPage == null)
            loginPage = new LoginPage(driver);
        return loginPage;
    }

    public SignUpPage signUpPage() {
        if (signUpPage == null)
            signUpPage = new SignUpPage(driver);
        return signUpPage;
    }

    public HomePage homePage() {
        if (homePage == null)
            homePage = new HomePage(driver);
        return homePage;
    }

    public ProductDetailsPage productDetailsPage() {
        if (productDetailsPage == null)
            productDetailsPage = new ProductDetailsPage(driver);
        return productDetailsPage;
    }

    public CartPage cartPage() {
        if (cartPage == null)
            cartPage = new CartPage(driver);
        return cartPage;
    }

    public CheckoutPage checkoutPage() {
        if (checkoutPage == null)
            checkoutPage = new CheckoutPage(driver);
        return checkoutPage;
    }

    public OrdersPage ordersPage() {
        if (ordersPage == null)
            ordersPage = new OrdersPage(driver);
        return ordersPage;
    }

    public CreatePostPage createPostPage() {
        if (createPostPage == null)
            createPostPage = new CreatePostPage(driver);
        return createPostPage;
    }

}
