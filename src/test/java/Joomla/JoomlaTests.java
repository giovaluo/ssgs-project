package Joomla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JoomlaTests {
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

    // Test 1: Login as admin
    @Test
    public void loginAsAdminTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("root");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // Then "Super User" is shown as value of the "Name" field
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jform_name")));
        String name = nameField.getAttribute("value");
        assertEquals("Super User", name);

        // Given the previous assertion passed
	    // Then the user clicks the "Log out" link
        WebElement logoutPage = driver.findElement(By.linkText("Log out"));
        logoutPage.click();

	    // And clciks the "Log out" button
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        logoutButton.click();
    }


    // Test 2: Try to login with wrong credentials and fails
    @Test
    public void loginWithWrongCredentialsFailsTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "wrongpassword" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("wrongpassword");

        // And clicks the "Log in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // Then "Username and password do not match or you do not have an account yet." is shown
        WebElement wrongCredentialsAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert")));
        String alertMsg = wrongCredentialsAlert.getText();
        assertTrue(alertMsg.contains("Username and password do not match or you do not have an account yet."));
        // ...we also check the box is yellow
        assertTrue(wrongCredentialsAlert.getCssValue("color").equals("rgba(192, 152, 83, 1)"));
    }


    // Test 3: Tries to login with empty credentials and fails
    @Test
    public void loginWithEmptyCredentialsFailsTest() throws InterruptedException {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

        // Click on "Log in" button without filling fields
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        // only works with 2 clicks for some reason...
        signInButton.click();
        signInButton.click();

        // Then "Please fill out this field." is shown as a HTML 5 validation message
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        assertTrue(usernameField.getAttribute("class").contains("invalid"));
        assertNotNull(usernameField.getAttribute("validationMessage"));
    }


    // Test 4: Adds a new user
    @Test
    public void addUserTest() throws InterruptedException {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("root");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // And clicks the "Site Administrator" link
        WebElement siteAdministrationButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Site Administrator")));
        siteAdministrationButton.click();

        // Site Administration link opens a new tab. We continue the test in the newly opened tab
        // by focusing the webdriver on the new tab.

        // we get every open tab
        String oldTab = driver.getWindowHandle();
        String newTab = driver.getWindowHandle();
        for(String winHandle : driver.getWindowHandles()) {
            newTab = winHandle;
        }
        // switch focus to newest tab opened
        driver.switchTo().window(newTab);

        // We are now in the new tab.
        
        // And enters "administrator" in the "Username" field
        WebElement adminUsernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("mod-login-username")));
        adminUsernameField.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement adminPasswordField = driver.findElement(By.id("mod-login-password"));
        adminPasswordField.sendKeys("root");

        // And clicks the "Log in" button
        driver.findElement(By.xpath("//button[@class='btn btn-primary btn-block btn-large login-button']")).click();

        // And clicks the "Users" link
        WebElement usersPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='container-fluid container-main']//ul[3]//li[1]//a[1]")));
        usersPage.click();
        
        // And clicks the "New" button
        WebElement addUserButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-small button-new btn-success']")));
        addUserButton.click();

        // We fill the fields
        WebElement newUserNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jform_name")));
        WebElement newUserLoginNameField = driver.findElement(By.id("jform_username"));
        WebElement newUserPasswordField = driver.findElement(By.id("jform_password"));
        WebElement newUserConfirmPasswordField = driver.findElement(By.id("jform_password2"));
        WebElement newUserEmailField = driver.findElement(By.id("jform_email"));

        newUserNameField.sendKeys("Test User");
        newUserLoginNameField.sendKeys("tuser01");
        newUserPasswordField.sendKeys("tpassword");
        newUserConfirmPasswordField.sendKeys("tpassword");
        newUserEmailField.sendKeys("testmail@example.com");

        //And clicks the "Save & Close" button
        WebElement saveAndCloseButton = driver.findElement(By.xpath("//button[@class='btn btn-small button-save']"));
        saveAndCloseButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert alert-success']")));

        // Then "Test User", "tuser01" and "testmail@example.com" are shown respectively as name, username and email in the second row of the table
        WebElement secondRow = driver.findElement(By.className("row1"));
        String secondRowText = secondRow.getText();
        assertTrue(secondRowText.contains("Test User"));
        assertTrue(secondRowText.contains("tuser01"));
        assertTrue(secondRowText.contains("testmail@example.com"));

        // Given the assertions passed, the user clicks the down pointing arrow icon in the top-right corner of the page
        driver.findElement(By.xpath("//a[@class='dropdown-toggle']//span[@class='icon-user']")).click();
        
        // And clicks the "Log out" button
        driver.findElement(By.linkText("Logout")).click();
        
        // We close the current tab and focus on the old one again
        driver.close();
        driver.switchTo().window(oldTab);
        
        // And clicks the "Log out" link
        WebElement logoutPage = driver.findElement(By.linkText("Log out"));
        logoutPage.click();

        // And clicks the "Log out" button
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        logoutButton.click();
    }


    // Test 5: Logins as the newly added user
    @Test
    public void loginAsNewlyAddedUserTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "tuser01" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("tuser01");

        // And enters "tpassword" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("tpassword");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // Then "Test User" is shown as value of the "Name" field
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jform_name")));
        String name = nameField.getAttribute("value");
        assertEquals("Test User", name);

        // Given the previous assertion passed
	    // Then the user clicks the "Author Login" link
        driver.findElement(By.linkText("Author Login")).click();

        // And clicks the "Log out" button
        WebElement logoutPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        logoutPage.click();
    }

    // Test 6: Tries to login to the site administration area with wrong credentials and fails
    @Test
    public void loginAdminAreaWithWrongCredentialsFailsTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("root");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // And clicks the "Site Administrator" link
        WebElement siteAdministrationButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Site Administrator")));
        siteAdministrationButton.click();

        // We continue the test in the newly opened tab
        String oldTab = driver.getWindowHandle();
        String newTab = driver.getWindowHandle();
        for(String winHandle : driver.getWindowHandles()) {
            newTab = winHandle;
        }
        // Switch focus to newest tab opened
        driver.switchTo().window(newTab);

        // And enters "administrator" in the "Username" field
        WebElement adminUsernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("mod-login-username")));
        adminUsernameField.sendKeys("administrator");

        // And enters "wrongpassword" in the "Password" field
        WebElement adminPasswordField = driver.findElement(By.id("mod-login-password"));
        adminPasswordField.sendKeys("wrongpassword");

        // And clicks the "Log in" button
        driver.findElement(By.xpath("//button[@class='btn btn-primary btn-block btn-large login-button']")).click();

        // Then "Username and password do not match or you do not have an account yet." is shown in a yellow box
        WebElement wrongCredentialsAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert-message']")));
        String alertMsg = wrongCredentialsAlert.getText();
        assertTrue(alertMsg.contains("Username and password do not match or you do not have an account yet."));
        //... and check its yellow
        assertTrue(wrongCredentialsAlert.getCssValue("color").equals("rgba(138, 109, 59, 1)"));

        // Given the previous assertion passed, then the user closes the current tab
        driver.close();
        driver.switchTo().window(oldTab);

        // And clicks the "Log out" link
        WebElement logoutPage = driver.findElement(By.xpath("//a[normalize-space()='Log out']"));
        logoutPage.click();

        // And clicks the "Log out" button 
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        logoutButton.click();
    }

    // Test 7: Tries to login to the site administration area with empty credentials and fails
    @Test
    public void loginAdminAreaWithEmptyCredentialsFailsTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("root");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // And clicks the "Site Administrator" link
        WebElement siteAdministrationButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Site Administrator")));
        siteAdministrationButton.click();

        // We continue the test in the opened tab
        String oldTab = driver.getWindowHandle();
        String newTab = driver.getWindowHandle();
        for(String winHandle : driver.getWindowHandles()) {
            newTab = winHandle;
        }
        // Switch focus to newest window opened
        driver.switchTo().window(newTab);

        // And clicks the "Log in" button with empty credentials
        wait.until(ExpectedConditions.elementToBeClickable(By.id("mod-login-username")));
        driver.findElement(By.xpath("//button[@class='btn btn-primary btn-block btn-large login-button']")).click();

        // Then "Empty password not allowed." is shown in a yellow box
        WebElement wrongCredentialsAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert")));
        String alertMsg = wrongCredentialsAlert.getText();
        assertTrue(alertMsg.contains("Empty password not allowed."));
        // and check its yellow
        assertTrue(wrongCredentialsAlert.getCssValue("color").equals("rgba(138, 109, 59, 1)"));

        // Given the previous assertion passed, then the user closes the current tab
        driver.close();
        driver.switchTo().window(oldTab);

        // And clicks the "Log out" link
        WebElement logoutPage = driver.findElement(By.xpath("//a[normalize-space()='Log out']"));
        logoutPage.click();

        // And clicks the "Log out" button
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        logoutButton.click();
    }

    // Test 8: Add a new article
    @Test
    public void addNewArticle() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");
        
        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("root");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // And clicks the "Create a Post" link
        WebElement createPostButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create a Post")));
        createPostButton.click();

        // And enters "Test Article 01" in the "Title" field
        WebElement postTitleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jform_title")));
        postTitleField.sendKeys("Test Article 01");
        
        // And enters "This is the body of the first article for testing the platform" in the main text editor
        WebElement postBodyField = driver.findElement(By.id("jform_articletext_ifr"));
        postBodyField.sendKeys(" This is the body of the first article for testing the platform");

        // And clicks the "Save" button
        driver.findElement(By.xpath("//span[@class='icon-ok']")).click();

        // Then "Test Article 01" is shown as title of the first article
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("leading-0")));
        String newPostTitle = driver.findElement(By.tagName("h2")).getText();
        assertEquals("Test Article 01", newPostTitle);

        // And "This is the body of the first article for testing the platform" is shown as text of the first article
        String newPostBody = driver.findElement(By.id("content")).getText();     
        assertTrue(newPostBody.contains("This is the body of the first article for testing the platform"));

        // 	Given the previous assertion passed
	    // Then the user clicks the "Author Login" link
        WebElement logoutPage = driver.findElement(By.xpath("//a[normalize-space()='Log out']"));
        logoutPage.click();

        // And clicks the "Log out" button
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        logoutButton.click();
    }

    // Test 9: Add empty article fails
    @Test
    public void addEmptyArticleFailsTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("root");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // And clicks the "Create a Post" link
        WebElement createPostButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create a Post")));
        createPostButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jform_title")));

        // And clicks the "Save" button
        driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();

        // Then "Invalid field: Title" is shown in a red box
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("system-message-container")));
        // WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"system-message-container\"]/div")));
        String alertMsg = alert.getText();
        assertTrue(alertMsg.contains("Invalid field:  Title "));

        // Given the previous assertion passed
	    // Then the user clicks the "Author Login" link
        driver.findElement(By.linkText("Author Login")).click();

        // And clicks the "Log out" button
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
        logoutButton.click();
    }

    // Test 10: Edits an article
    @Test
    public void editArticleTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");
        
        // When the user clicks the "Author Login" link
        WebElement loginButton = driver.findElement(By.linkText("Author Login"));
        loginButton.click();

        // And enters "administrator" in the "Username" field
        WebElement usernameForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameForm.sendKeys("administrator");

        // And enters "root" in the "Password" field
        WebElement passwordForm = driver.findElement(By.id("password"));
        passwordForm.sendKeys("root");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Test Article 01"))).click();

        // And clicks the gear icon to the bottom right of "Test Article 01"
        WebElement gearIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("icon-cog")));
        gearIcon.click();

        // And clicks the "Edit" option
        WebElement edit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Edit article']")));
        edit.click();

        // And enters "EDITED" in the main text editor (the new text must be appended to the existing one)
        WebElement editBodyForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jform_articletext_ifr")));
        editBodyForm.sendKeys(Keys.CONTROL + "a");
        editBodyForm.sendKeys(Keys.ARROW_RIGHT);
        editBodyForm.sendKeys("EDITED");
        
        // And clicks the "Save" button
        driver.findElement(By.className("icon-ok")).click();

        // Then "This is the body of the first article for testing the platformEDITED" is shown as text of the first article
        WebElement editedPost = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));
        String editedBody = editedPost.getText();
        assertTrue(editedBody.contains("This is the body of the first article for testing the platformEDITED"));

        // Given the previous assertion passed
	    // Then the user clicks the "Author Login" link
        WebElement authorLoginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Author Login")));
        authorLoginButton.click();

        // And clicks the "Log out" button
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
        logoutButton.click();
    }
}