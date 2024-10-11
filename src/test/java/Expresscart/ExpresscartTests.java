package Expresscart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExpresscartTests {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-search-engine-choice-screen", "--no-sandbox", "--disable-gpu", "--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Test 1: Add a new user to the system
    @Test
    public void addUserTest() {
        // Navigate to the administrative home page
        driver.get("http://localhost:3000/admin");

        // Log in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("owner@test.com");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("test");
        
        WebElement signInButton = driver.findElement(By.id("loginForm"));
        signInButton.click();

        // Wait for Add User button ("+") to be visible and then click it
        WebElement addUserButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/admin/user/new']")));
        addUserButton.click();

        // Wait for the user creation form to be visible and create user
        WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usersName")));
        userNameField.sendKeys("TestUser000");
        
        WebElement userEmailField = driver.findElement(By.id("userEmail"));
        userEmailField.sendKeys("test000@test.com");
        
        WebElement userPasswordField = driver.findElement(By.id("userPassword"));
        userPasswordField.sendKeys("password");
        
        WebElement passwordConfirmField = driver.findElement(By.xpath("//input[@data-match='#userPassword']"));
        passwordConfirmField.sendKeys("password");
        
        WebElement createButton = driver.findElement(By.id("btnUserAdd"));
        createButton.click();

        // Clicks the "Users" link
        driver.findElement(By.linkText("Users")).click();

        // // Wait for the user table to be updated (find the third row)
        WebElement thirdRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//main[@role='main']//li[3]")));
        String rowText = thirdRow.getText();
        assertTrue(rowText.contains("User: TestUser000 - (test000@test.com)\nRole: User"));

        // Wait for the Logout link to be clickable and then click it
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        logoutLink.click();
    }


    // Test 2: Add Empty User Fails
    @Test
    public void tryToAddEmptyUserTest() throws InterruptedException {
        // Navigate to the administrative home page
        driver.get("http://localhost:3000/admin");

        // Log in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("owner@test.com");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("test");
        
        WebElement signInButton = driver.findElement(By.id("loginForm"));
        signInButton.click();

        // Wait for Add User button ("+") to be visible and then click it
        WebElement addUserButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/admin/user/new']")));
        addUserButton.click();

        // Click the Create button without filling in the user details
        WebElement createButton = driver.findElement(By.id("btnUserAdd"));
        createButton.click();
        
        // When we press "Create" with empty fields, the first one gets auto-focused.
        // We press anywhere else to unfocus the autoselected text area.
        driver.findElement(By.id("container")).click();
        Thread.sleep(2000);

        // Verify all fields are highlighted in red (indicating validation errors)
        WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usersName")));
        WebElement userEmailField = driver.findElement(By.id("userEmail"));
        WebElement userPasswordField = driver.findElement(By.id("userPassword"));
        WebElement passwordConfirmField = driver.findElement(By.xpath("//input[@data-match='#userPassword']"));

        // Assuming the fields turn red on invalid input, check their styles
        assertTrue(userNameField.getCssValue("border-color").equals("rgb(204, 65, 53)"), "User name field is not highlighted in red");
        assertTrue(userEmailField.getCssValue("border-color").equals("rgb(204, 65, 53)"), "User email field is not highlighted in red");
        assertTrue(userPasswordField.getCssValue("border-color").equals("rgb(204, 65, 53)"), "User password field is not highlighted in red");
        assertTrue(passwordConfirmField.getCssValue("border-color").equals("rgb(204, 65, 53)"), "Password confirm field is not highlighted in red");

        // Wait for the Logout link to be clickable and then click it
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        logoutLink.click();
    }


    // Test 3: Login as newly created user
    @Test
    public void loginAsNewUserTest() {
        // Navigate to the administrative home page
        driver.get("http://localhost:3000/admin");

        // Enter the user credentials and sign in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("test000@test.com");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("password");
        
        WebElement signInButton = driver.findElement(By.id("loginForm"));
        signInButton.click();

        // Verify that "Dashboard" is shown on the page
        WebElement dashboardText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Dashboard')]")));
        assertTrue(dashboardText.isDisplayed());

        // Click the Logout link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        logoutLink.click();
    }


    // Test 4: Adding already existing user fails
    @Test
    public void tryToAddUserWithExistingDataTest() {
        // Navigate to the administrative home page
        driver.get("http://localhost:3000/admin");

        // Log in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("owner@test.com");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("test");
        
        WebElement signInButton = driver.findElement(By.id("loginForm"));
        signInButton.click();

        // Wait for and click the Add User button
        WebElement addUserButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/admin/user/new']")));
        addUserButton.click();

        // Enter user details
        WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usersName")));
        userNameField.sendKeys("TestUser000");
        
        WebElement userEmailField = driver.findElement(By.id("userEmail"));
        userEmailField.sendKeys("test000@test.com");
        
        WebElement userPasswordField = driver.findElement(By.id("userPassword"));
        userPasswordField.sendKeys("password");
        
        WebElement passwordConfirmField = driver.findElement(By.xpath("//input[@data-match='#userPassword']"));
        passwordConfirmField.sendKeys("password");
        
        WebElement createButton = driver.findElement(By.id("btnUserAdd"));
        createButton.click();

        // Wait for and verify the error message
        WebElement alertMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'alert') and contains(text(), 'A user with that email address already exists')]")));
        assertTrue(alertMessage.isDisplayed(), "The alert message for existing user is not displayed");

        // Wait for the alert to disappear
        wait.until(ExpectedConditions.invisibilityOf(alertMessage));

        // Click the Logout link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        logoutLink.click();
    }


    // Test 5: Add Product
    @Test
    public void addProductTest() {
        // Navigate to the administrative home page
        driver.get("http://localhost:3000/admin");

        // Log in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("owner@test.com");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("test");
        
        WebElement signInButton = driver.findElement(By.id("loginForm"));
        signInButton.click();

        // Navigate to the Add Products section
        WebElement productsLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/admin/product/new']")));
        productsLink.click();

        // Enter product details
        WebElement productTitleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
        productTitleField.sendKeys("NewProduct000");
        
        WebElement productPriceField = driver.findElement(By.id("productPrice"));
        productPriceField.sendKeys("15.95");
        
        WebElement productDescriptionField = driver.findElement(By.xpath("//div[@class='note-editable panel-body']//p"));
        productDescriptionField.sendKeys("Description for product 000");
        
        // Click the Add Product button
        WebElement addProductSubmitButton = driver.findElement(By.id("frm_edit_product_save"));
        addProductSubmitButton.click();

        // Navigate back to the Products list
        WebElement productsListLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='nav flex-column']//li[3]//a[1]")));
        productsListLink.click();

        // Verify that the new product is shown in the first row of the table
        WebElement firstProductRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[2]//div[1]")));
        assertTrue(firstProductRow.getText().contains("NewProduct000"), "The new product is not shown in the first row of the table");

        // Click the Logout link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        logoutLink.click();
    }


    // Test 6: Add Empty Product Fails
    @Test
    public void tryToAddEmptyProductTest() throws InterruptedException {
        // Navigate to the administrative home page
        driver.get("http://localhost:3000/admin");

        // Log in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("owner@test.com");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("test");
        
        WebElement signInButton = driver.findElement(By.id("loginForm"));
        signInButton.click();

        // Navigate to the Add Products section
        WebElement productsLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/admin/product/new']")));
        productsLink.click();

        // Attempt to add an empty product
        WebElement addProductSubmitButton = driver.findElement(By.id("frm_edit_product_save"));
        addProductSubmitButton.click();

        // When we press "Create" with empty fields, the first one gets autoselected. 
        // We press anywhere else to unfocus the autoselected text area.
        driver.findElement(By.tagName("body")).click();
        Thread.sleep(2000);

        // Verify that the fields "Product title" and "Product price" are highlighted in red
        WebElement productTitleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
        WebElement productPriceField = driver.findElement(By.id("productPrice"));

        String titleFieldBorderColor = productTitleField.getCssValue("border-color");
        String priceFieldBorderColor = productPriceField.getCssValue("border-color");

        // Assuming the border color turns red when validation fails
        assertTrue(titleFieldBorderColor.contains("rgb(204, 65, 53)"));
        assertTrue(priceFieldBorderColor.contains("rgb(204, 65, 53)"));

        // Click the Logout link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        logoutLink.click();
    }


    // Test 7: Add to cart
    @Test
    public void addProductToCartTest() {
        // Navigate to the home page
        driver.get("http://localhost:3000");

        // Click the "NewProduct000" link
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("NewProduct000")));
        productLink.click();

        // Click the "Add to cart" button
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add to cart']")));
        addToCartButton.click();

        // Click the "Home" link
        WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='navbar-brand']")));
        homeLink.click();

        // Verify "1" is shown in the red square to the right of the "Cart" link
        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='btn menu-btn']")));
        String badgeText = cartBadge.getText();
        assertTrue(badgeText.contains("1"));

        // Click on the "Cart" link
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn menu-btn']")));
        cartLink.click();

        // Verify "NewProduct000" is shown in the "Cart contents"
        WebElement cartContents = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-8 col-md-9']//div[@class='row']")));
        assertTrue(cartContents.getText().contains("NewProduct000"));
    }


    // Test 8: Search
    @Test
    public void searchProductTest() {
        // Navigate to the home page
        driver.get("http://localhost:3000");

        // Enter "NewProduct000" in the "Search shop" field
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("frm_search")));
        searchField.sendKeys("NewProduct000");

        // Click the "Search" button
        WebElement searchButton = driver.findElement(By.id("btn_search"));
        searchButton.click();

        // Verify "NewProduct000" is shown to the right of "Search results:"
        WebElement searchResultsText = driver.findElement(By.xpath("//div[@class='row product-layout']"));
        assertTrue(searchResultsText.getText().contains("NewProduct000"));

        // Locate all items with the class "product-item"
        WebElement searchResults = driver.findElement(By.className("product-layout"));
        List<WebElement> items = searchResults.findElements(By.xpath("//body/div[@id='container']/div[@class='row']/div[@class='productsWrapper col-sm-12 col-md-8 offset-md-2']/div[@class='row product-layout']/div[*]/div[1]"));

        // Verify there is only one product item
        assertEquals(1, items.size());
    }


    // Test 9: Add product review
    @Test
    public void addProductReviewTest() throws InterruptedException {
        // Navigate to the home page
        driver.get("http://localhost:3000");

        // Click on the account icon
        WebElement accountIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn']")));
        accountIcon.click();

        // Enter email and password and sign in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement signInButton = driver.findElement(By.id("customerloginForm"));

        emailField.sendKeys("test@test.com");
        passwordField.sendKeys("test");
        signInButton.click();

        // Go to the home page of the site again
        WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Home']")));
        homeButton.click();

        // Click the "NewProduct000" link
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[normalize-space()='NewProduct000']")));
        productLink.click();

        // Click the "Add review" button
        WebElement addReviewButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-review")));
        addReviewButton.click();

        // Enter review details
        WebElement titleField = wait.until(ExpectedConditions.elementToBeClickable(By.id("review-title")));
        WebElement descriptionField = driver.findElement(By.id("review-description"));
        WebElement ratingField = driver.findElement(By.id("review-rating"));
        WebElement submitReviewButton = driver.findElement(By.id("addReview"));
        WebElement closeReview = driver.findElement(By.xpath("//*[@id=\"reviewModal\"]/div/div/div[3]/button[1]"));

        titleField.sendKeys("Review000");
        descriptionField.sendKeys("Description000");
        ratingField.sendKeys("5");
        submitReviewButton.click();
        Thread.sleep(3000);
        closeReview.click();

        // Click on "Recent reviews"
        WebElement recentReviewsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Recent reviews")));
        recentReviewsLink.click();
        Thread.sleep(3000);

        // Wait for reviews to be visible
        WebElement reviewRating  = driver.findElement(By.xpath("//*[@id=\"collapseReviews\"]/li/p[2]"));
        WebElement reviewTitle  = driver.findElement(By.xpath("//*[@id=\"collapseReviews\"]/li/p[3]"));
        WebElement reviewDescription  = driver.findElement(By.xpath("//*[@id=\"collapseReviews\"]/li/p[4]"));

        assertTrue(reviewTitle .getText().contains("Title: Review000"), "Review title 'Review000' not found");
        assertTrue(reviewDescription .getText().contains("Description: Description000"), "Review description 'Description000' not found");
        assertTrue(reviewRating .getText().contains("Rating: 5"), "Review rating '5' not found");

        // Log out
        WebElement dropdown = driver.findElement(By.xpath("//nav[@class='navbar navbar-expand-lg justify-content-between mainNavBar']//div[2]//button[1]"));
        dropdown.click();
        
        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Logout")));    
        logout.click();
    }


    // Test 10: Add empty review
    @Test
    public void addEmptyReviewTest() {
        // Navigate to the home page
        driver.get("http://localhost:3000");

        // Click on the account icon
        WebElement accountIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn']")));
        accountIcon.click();

        // Enter email and password and sign in
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement signInButton = driver.findElement(By.id("customerloginForm"));

        emailField.sendKeys("test@test.com");
        passwordField.sendKeys("test");
        signInButton.click();

        // Go to the home page of the site again (could be redundant)
        WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Home']")));
        homeButton.click();

        // Click the "NewProduct000" link
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[normalize-space()='NewProduct000']")));
        productLink.click();

        // Click the "Add review" button
        WebElement addReviewButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-review")));
        addReviewButton.click();

        // Click the "Add review" button in the modal without filling in the fields
        WebElement submitReviewButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addReview")));
        submitReviewButton.click();

        // Wait for the error message to be visible
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notify_message")));

        // Verify the error message
        String errorText = errorMessage.getText();
        assertTrue(errorText.contains("Please supply a review title"), "Expected error message not found");

        WebElement closeReview = driver.findElement(By.xpath("//*[@id=\"reviewModal\"]/div/div/div[3]/button[1]"));
        closeReview.click();

        // Log out
        WebElement dropdown = driver.findElement(By.xpath("//nav[@class='navbar navbar-expand-lg justify-content-between mainNavBar']//div[2]//button[1]"));
        dropdown.click();
        
        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Logout")));    
        logout.click();
    }

}