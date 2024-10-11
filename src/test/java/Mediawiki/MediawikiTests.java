package Mediawiki;

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

public class MediawikiTests {
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

    // Test 1: Create new user
    @Test
    public void createNewUserTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // The user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field and "Password001" in the "Password" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        usernameField.sendKeys("admin");
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And clicks the "Special pages" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Special pages"))).click();
        // And clicks the "Create account" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Create account"))).click();

        // And enters "User001" in the "Username" field
        WebElement newUserUsernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName2")));
        newUserUsernameField.sendKeys("User001");

		// And enters "Password001" in the "Password" field
        WebElement newUserPasswordField = driver.findElement(By.id("wpPassword2"));
        newUserPasswordField.sendKeys("Password001");

		// And enters "Password001" in the "Confirm Password" field
		WebElement newUserConfirmPasswordField = driver.findElement(By.id("wpRetype"));
        newUserConfirmPasswordField.sendKeys("Password001");

        // And enters "Real Name 001" in the "Real Name" field
        WebElement newUserRealName = driver.findElement(By.id("wpRealName"));
        newUserRealName.sendKeys("Real Name 001");

        // And clicks the "Create" button
        driver.findElement(By.id("wpCreateaccount")).click();

        // Then "The user account for User001 (talk) has been created." is displayed
        String successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mw-content-text\"]/p[1]"))).getText();
        assertTrue(successMsg.contains("The user account for User001 (talk) has been created."));

        // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }


    // Test 2: Closes the pop-up that is shown the first time the editor is opened (test only works once per user tested)
    @Test
    public void closePopUpOnFirstEditTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");

        // And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "Software testing" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("Software testing");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "Software testing" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Software testing"))).click();

        // Then a pop-up is shown inside the page
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='oo-ui-widget oo-ui-widget-enabled oo-ui-buttonElement oo-ui-labelElement oo-ui-flaggedElement-progressive oo-ui-flaggedElement-primary oo-ui-buttonWidget oo-ui-actionWidget oo-ui-buttonElement-framed']//a[@role='button']")));
        assertNotNull(popup);
        // Then the user clicks the "Start editing" button
        popup.click();

        // And clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }


    // Test 3: Create new page
    @Test
    public void createNewPageTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");

        // And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "Software testing" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("Software testing");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "Software testing" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Software testing"))).click();

        // And closes the notification
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='oo-ui-widget oo-ui-widget-enabled oo-ui-labelElement oo-ui-floatableElement-floatable oo-ui-popupWidget-anchored oo-ui-popupWidget oo-ui-popupTool-popup oo-ui-popupWidget-anchored-top']//span[@class='oo-ui-iconElement-icon oo-ui-icon-close']"))).click();

        // And enters the text of the page in the editor
        WebElement textarea = driver.findElement(By.xpath("//*[@id=\"bodyContent\"]/div[5]/div[1]/div[1]"));
        textarea.click();
        textarea.sendKeys("Software testing is the process of evaluating and verifying that a software product or application does what it's supposed to do.");
        
        // And clicks the "Save" button
        driver.findElement(By.xpath("//div[@class='ve-ui-toolbar-group-save oo-ui-widget oo-ui-toolGroup oo-ui-barToolGroup oo-ui-widget-enabled']//span[3]")).click();
        
        // And enters "Page created" in the summary
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Describe what you changed']"))).sendKeys("Page created");
        
        // And clicks the "Save page" button
        driver.findElement(By.xpath("//span[@class='oo-ui-labelElement-label'][normalize-space()='Save page']")).click();

        // Then the page is displayed with "Software testing" as title and the previously inserted text as body
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("mw-page-title-main")));
        WebElement body = driver.findElement(By.id("content"));

        assertTrue(header.getText().contains("Software testing"));
        assertTrue(body.getText().contains("Software testing is the process of evaluating and verifying that a software product or application does what it's supposed to do."));

        // Given the previous assertion passed
	    // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }


    // Test 4: Create new page containing hyperlink to another page
    @Test
    public void createNewPageWithHyperlinkToAnotherPageTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");

		// And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "E2E Web Testing" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("E2E Web Testing");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "E2E Web Testing" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("E2E Web Testing"))).click();

        // And closes the notification
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='oo-ui-widget oo-ui-widget-enabled oo-ui-labelElement oo-ui-floatableElement-floatable oo-ui-popupWidget-anchored oo-ui-popupWidget oo-ui-popupTool-popup oo-ui-popupWidget-anchored-top']//span[@class='oo-ui-iconElement-icon oo-ui-icon-close']"))).click();

        // And enters the first part of the text in the editor
        WebElement textarea = driver.findElement(By.xpath("//*[@id=\"bodyContent\"]/div[5]/div[1]/div[1]"));
        textarea.click();

        // And enters "[[" in the editor
        textarea.sendKeys("[[");
        
        // And enters "Software testing" in the popup search bar
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Search internal pages']"))).sendKeys("Software testing");
        
        // And clicks the "Software testing" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Software testing"))).click();
        
        // And clicks the editor after the "Software testing" link
        textarea.click();

        // And enters the last part of the text
        textarea.sendKeys(" that checks an entire software application from beginning to end, mimicking real user interactions and data.");

        // And clicks the "Save" button
        driver.findElement(By.xpath("//div[@class='ve-ui-toolbar-group-save oo-ui-widget oo-ui-toolGroup oo-ui-barToolGroup oo-ui-widget-enabled']//span[3]")).click();
        
        // And enters "Page created" in the summary
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Describe what you changed']"))).sendKeys("Page created");
        
        // And clicks the "Save page" button
        driver.findElement(By.xpath("//span[@class='oo-ui-labelElement-label'][normalize-space()='Save page']")).click();

        // Then the page is displayed with "E2E Web Testing" as title
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("mw-page-title-main")));
        assertTrue(header.getText().contains("E2E Web Testing"));

        // Given the previous assertion passed
	    // When the user clicks the "Software testing"
        driver.findElement(By.linkText("Software testing")).click();

        // Then the page created in the previous test case is displayed
        WebElement linkedPageheader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='mw-page-title-main']")));
        WebElement body = driver.findElement(By.xpath("//div[@id='content']"));

        assertTrue(linkedPageheader.getText().contains("Software testing"));
        assertTrue(body.getText().contains("Software testing is the process of evaluating and verifying that a software product or application does what it's supposed to do."));

        // Given the previous assertion passed
        // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }

    // Test 5: Search a page
    @Test
    public void searchPageTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");
        
        // When the user enters "Software testing" in the search bar
        WebElement searchField = driver.findElement(By.xpath("//input[@id='searchInput']"));
        searchField.sendKeys("Software testing");

        // And presses Enter
        searchField.sendKeys(Keys.ENTER);

        // Then the page "Software testing" is displayed
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("mw-page-title-main")));
        assertTrue(header.getText().contains("Software testing"));
    }


    // Test 6: Edit a page
    @Test
    public void editAPageTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");

        // And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "Software testing" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("Software testing");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "Edit" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Edit"))).click();
        
        // And enters the additional text at the end of the editor
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"bodyContent\"]/div[6]/div[1]/div[1]")));
        String endOfLine = Keys.chord(Keys.CONTROL, Keys.ARROW_DOWN);
        textarea.sendKeys(endOfLine);
        textarea.sendKeys(" ADDITION.");

        // And clicks the "Save" button
        driver.findElement(By.xpath("//div[@class='ve-ui-toolbar-group-save oo-ui-widget oo-ui-toolGroup oo-ui-barToolGroup oo-ui-widget-enabled']//span[3]")).click();
        
        // And enters "Page expanded" in the sumamry
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Describe what you changed']"))).sendKeys("Page expanded");
        
        // And clicks on "Save page"
        driver.findElement(By.xpath("//span[@class='oo-ui-widget oo-ui-widget-enabled oo-ui-buttonElement oo-ui-buttonElement-framed oo-ui-flaggedElement-primary oo-ui-flaggedElement-progressive oo-ui-buttonWidget oo-ui-actionWidget oo-ui-labelElement']//a[@role='button']")).click();

        // Then the page is displayed with "Software testing" as title and the full text as body
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("mw-page-title-main")));
        WebElement body = driver.findElement(By.xpath("//div[@id='content']"));

        assertTrue(header.getText().contains("Software testing"));
        assertTrue(body.getText().contains("Software testing is the process of evaluating and verifying that a software product or application does what it's supposed to do. ADDITION."));

        // Given the previous assertion passed
        // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }


    // Test 7: Create a template
    @Test
    public void createATemplateTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");
        
        // And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "Template:Software" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("Template:Software");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "Template:Software" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Template:Software"))).click();
        
        // And enters "Developer: {{{dev}}} Latest version: {{{ver}}}" in the editor
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpTextbox1")));
        textarea.click();
        textarea.sendKeys("Developer: {{{dev}}} Latest version: {{{ver}}}");

        // And clicks the "Save page" button
        driver.findElement(By.id("wpSave")).click();

        // Then the page is displayed with "Template:Software" as title and the previously inserted text as body
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        WebElement body = driver.findElement(By.id("bodyContent"));

        assertTrue(header.getText().contains("Template:Software"));
        assertTrue(body.getText().contains("Developer: {{{dev}}} Latest version: {{{ver}}}"));

        // Given the previous assertion passed
        // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }


    // Test 8: Create a new page using source editor
    @Test
    public void createNewPageUsingSourceEditorTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");

        // And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "Selenium WebDriver" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("Selenium Webdriver");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "Selenium WebDriver" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Selenium Webdriver"))).click();

        // 	And closes the notification
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='oo-ui-widget oo-ui-widget-enabled oo-ui-labelElement oo-ui-floatableElement-floatable oo-ui-popupWidget-anchored oo-ui-popupWidget oo-ui-popupTool-popup oo-ui-popupWidget-anchored-top']//span[@class='oo-ui-iconElement-icon oo-ui-icon-close']"))).click();
        
        // And clicks the "Create source" link
        driver.findElement(By.linkText("Create source")).click();

        // And enters the text of the page in the editor
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpTextbox1")));
        textarea.click();
        textarea.sendKeys("Selenium WebDriver is a programming interface made up of different language bindings that can be used to create and execute test cases across all major programming languages, browsers, and operating systems (OS).");

        // And clicks the "Save page" button
        driver.findElement(By.id("wpSave")).click();

        // Then the page is displayed with "Selenium WebDriver" as title and the previously inserted text as body
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        WebElement body = driver.findElement(By.id("bodyContent"));

        assertTrue(header.getText().contains("Selenium Webdriver"));
        assertTrue(body.getText().contains("Selenium WebDriver is a programming interface made up of different language bindings that can be used to create and execute test cases across all major programming languages, browsers, and operating systems (OS)."));

        // Given the previous assertion passed
        // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }

    // Test 9: Create new page using source editor applying template
    @Test
    public void createNewPageApplyingTemplateTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
		WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");

        // And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");

        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "Selenium WebDriver" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("Selenium Webdriver");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "Edit source" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Edit source"))).click();

        // And enters "{{Software|dev=Selenium|ver=3.141.59}}" at the beginning of the page
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpTextbox1")));
        textarea.click();
        textarea.sendKeys(Keys.CONTROL+"a");
        textarea.sendKeys(Keys.ARROW_LEFT);
        textarea.sendKeys("{{Software|dev=Selenium|ver=3.141.59}} ");

        // And clicks the "Save changes" button
        driver.findElement(By.id("wpSave")).click();

        // Then the page is displayed with "Selenium WebDriver"
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        assertTrue(header.getText().contains("Selenium Webdriver"));

        // And "Developer: Selenium Latest version: 3.141.59" is shown at the beginning of the body
        WebElement body = driver.findElement(By.id("bodyContent"));
        assertTrue(body.getText().contains("Developer: Selenium Latest version: 3.141.59"));

        // Given the previous assertion passed
	    // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }

    // Test 10: Add new category
    @Test
    public void addNewCategoryTest() {
        // Given the user is on the home page
        driver.get("http://localhost:8080/");

        // When the user clicks the "Log in" link
        driver.findElement(By.xpath("//span[normalize-space()='Log in']")).click();
        
        // And enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpName1")));
        usernameField.sendKeys("admin");

		// And enters "Password001" in the "Password" field
        WebElement passwordField = driver.findElement(By.id("wpPassword1"));
        passwordField.sendKeys("Password001");
        
        // And clicks the "Log in" button
        driver.findElement(By.id("wpLoginAttempt")).click();

        // And enters "Selenium WebDriver" in the search bar and presses Enter
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        searchField.sendKeys("Selenium Webdriver");
        searchField.sendKeys(Keys.ENTER);

        // And clicks the "Edit" link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Edit"))).click();
        
        // And clicks the icon with three lines and clicks "Categories"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='oo-ui-iconElement-icon oo-ui-icon-menu']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='oo-ui-widget oo-ui-iconElement oo-ui-tool-with-icon oo-ui-tool oo-ui-tool-name-categories oo-ui-widget-enabled']//a[@role='button']"))).click();

        // And enters "Browser automation tools" in the "Add a category" field and presses Enter
        WebElement categoryField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Add a category']")));
        categoryField.click();
        categoryField.sendKeys("Browser automation tools");
        categoryField.sendKeys(Keys.ENTER);

        // And clicks the "Apply changes" button
        driver.findElement(By.xpath("//span[contains(text(),'Apply changes')]")).click();

        // And clicks the "Save" button
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='ve-ui-toolbar-group-save oo-ui-widget oo-ui-toolGroup oo-ui-barToolGroup oo-ui-widget-enabled']//span[3]"))).click();
        
        // And enters "Added category" in the sumamry
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Describe what you changed']"))).sendKeys("Added category");
        
        // And clicks the "Save page" button
        driver.findElement(By.xpath("//span[@class='oo-ui-widget oo-ui-widget-enabled oo-ui-buttonElement oo-ui-buttonElement-framed oo-ui-flaggedElement-primary oo-ui-flaggedElement-progressive oo-ui-buttonWidget oo-ui-actionWidget oo-ui-labelElement']//a[@role='button']")).click();

        // Then the page has title "Selenium WebDriver" and "Category: Browser automation tools" is displayed at the end of the page
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        WebElement body = driver.findElement(By.id("catlinks"));

        assertTrue(header.getText().contains("Selenium Webdriver"));
        assertTrue(body.getText().contains("Category: Browser automation tools"));

        // Given the previous assertion passed
        // Then the user clicks the "Log out" link
        driver.findElement(By.xpath("//span[normalize-space()='Log out']")).click();
    }


}