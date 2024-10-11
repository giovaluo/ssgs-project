package Kanboard;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

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

public class KanboardTets {
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

    // Test 1: Add a new project
    @Test
    public void testAddNewProject() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // And clicks the "New project" link
        WebElement newProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("New project")));
        newProjectLink.click();

        // And enter "Test 2" in the "Name" field
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-name")));
        nameField.sendKeys("Test 2");

        // And clicks the "Save" button
        WebElement saveButton = driver.findElement(By.xpath("//button[@type='submit']"));
        saveButton.click();

        // Then "Test 2" is shown to the right of the "KB" logo
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Summary")));
        WebElement projectTitle = driver.findElement(By.xpath("/html/body/header"));
        assertTrue(projectTitle.getText().contains("Test 2"));

        // And "This project is open" is shown below "Summary"
        WebElement projectStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/section/div[2]/ul")));
        assertTrue(projectStatus.getText().contains("This project is open"));

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 2: Tries to add a project without name and fails
    @Test
    public void testAddProjectWithoutNameFails() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();

        // And clicks the "New project" link
        WebElement newProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("New project")));
        newProjectLink.click();

        // And clicks the "Save" button without entering a name
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-name")));
        WebElement saveButton = driver.findElement(By.xpath("//button[@type='submit']"));
        saveButton.click();

        // Then "The project name is required" is shown below the "Name" field
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("form-errors"))); // Adjust the locator as needed
        assertTrue(errorMessage.getText().contains("The project name is required"));

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        driver.findElement(By.linkText("cancel")).click();
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 3: Add new task
    @Test
    public void testAddNewTaskToProject() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "Test 2" link
        WebElement projectLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Test 2")));
        projectLink.click();

        // And clicks the gear icon to the left of the screen
        WebElement gearIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//i[@class='fa fa-cog']"))); // Adjust the locator as needed
        gearIcon.click();

        // And clicks the "Add a new task" link
        WebElement addNewTaskLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add a new task")));
        addNewTaskLink.click();

        // And enters "task 3" in the "Title" field
        WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-title")));
        titleField.sendKeys("task 3");

        // And clicks the "Save" button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // Then "task 3" is shown on a yellow box
        WebElement task = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"board\"]/tbody/tr[2]/td[1]/div[1]/div/div[2]/div[3]")));
        assertTrue(task.getText().contains("task 3")); // Adjust the class name as needed

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }
    

    // Test 4: Tries to  add a task without title and fails
    @Test
    public void testAddTaskWithoutTitleFails() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "Test 2" link
        WebElement projectLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Test 2")));
        projectLink.click();

        // And clicks the gear icon to the left of the screen
        WebElement gearIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//i[@class='fa fa-cog']"))); // Adjust the locator as needed
        gearIcon.click();

        // And clicks the "Add a new task" link
        WebElement addNewTaskLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add a new task")));
        addNewTaskLink.click();

        // Wait for popup
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-title")));
        
        // And clicks the "Save" button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // Then "The title is required" is shown below the "Title" field
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("form-errors"))); // Adjust the locator as needed
        assertTrue(errorMessage.getText().contains("The title is required"));

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        driver.findElement(By.linkText("cancel")).click();
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 5: Change description of a project
    @Test
    public void testChangeProjectDescription() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "#1" icon to the left of "Test 2"
        WebElement projectIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//strong[normalize-space()='#1']"))); // Adjust the locator as needed
        projectIcon.click();

        // And clicks the "Configure this project" link
        WebElement configureProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[contains(text(),'Configure this project')]")));
        configureProjectLink.click();

        // And clicks the "Edit project" link
        WebElement editProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Edit project']")));
        editProjectLink.click();

        // And enters "This is the new description" in the "Description" field
        WebElement descriptionField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Write your text in Markdown']")));
        descriptionField.clear();
        descriptionField.click();
        descriptionField.sendKeys("This is the new description");

        // And clicks the "Save" button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // And clicks the "Summary" link
        WebElement summaryLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Summary")));
        summaryLink.click();

        // Then "This is the new description" is shown below "Description"
        WebElement description = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("markdown"))); // Adjust the locator as needed
        assertTrue(description.getText().contains("This is the new description"));

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 6: Add a new column
    @Test
    public void testAddNewColumn() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "#1" icon to the left of "Test 2"
        WebElement projectIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//strong[normalize-space()='#1']"))); // Adjust the locator as needed
        projectIcon.click();

        // And clicks the "Configure this project" link
        WebElement configureProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[contains(text(),'Configure this project')]")));
        configureProjectLink.click();

        // And clicks the "Columns" link
        WebElement columnsLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Columns']")));
        columnsLink.click();

        // And clicks the "Add a new column" link
        WebElement addNewColumnLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Add a new column']")));
        addNewColumnLink.click();

        // And enters "New Column 3" in the "Column" field
        WebElement columnField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-title")));
        columnField.sendKeys("New Column 3");

        // And clicks the "Save" button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // Then "New Column 3" is shown in the last row of the table
        WebElement lastColumn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr[5]/td[1]")));
        assertTrue(lastColumn.isDisplayed());
        assertTrue(lastColumn.getText().contains("New Column 3"));

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 7: Add a new column with no title fails
    @Test
    public void testAddColumnWithoutNameFails() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "#1" icon to the left of "Test 2"
        WebElement projectIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//strong[normalize-space()='#1']"))); // Adjust the locator as needed
        projectIcon.click();

        // And clicks the "Configure this project" link
        WebElement configureProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[contains(text(),'Configure this project')]")));
        configureProjectLink.click();

        // And clicks the "Columns" link
        WebElement columnsLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Columns']")));
        columnsLink.click();

        // And clicks the "Add a new column" link
        WebElement addNewColumnLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Add a new column']")));
        addNewColumnLink.click();

        // And clicks the "Save" button without entering a title
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // Then "The title is required" is shown below the "Title" field
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("form-errors"))); // Adjust the locator as needed
        assertTrue(errorMessage.getText().contains("The title is required"));
        driver.findElement(By.linkText("cancel")).click();

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 8: Add new swimlane
    @Test
    public void testAddNewSwimlane() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "#1" icon to the left of "Test 2"
        WebElement projectIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//strong[normalize-space()='#1']"))); // Adjust the locator as needed
        projectIcon.click();

        // And clicks the "Configure this project" link
        WebElement configureProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[contains(text(),'Configure this project')]")));
        configureProjectLink.click();

        // And clicks the "Swimlanes" link
        WebElement swimlanesLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Swimlanes']")));
        swimlanesLink.click();

        // And clicks the "Add a new swimlane" link
        WebElement addNewSwimlaneLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add a new swimlane")));
        addNewSwimlaneLink.click();

        // And enters "New Swimlane 3" in the "Name" field
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-name")));
        nameField.sendKeys("New Swimlane 3");

        // And clicks the "Save" button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // Then "New Swimlane 3" is shown in the last row of the table
        WebElement lastSwimlane = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/section/div[2]/table/tbody/tr[2]")));
        assertTrue(lastSwimlane.isDisplayed());
        assertTrue(lastSwimlane.getText().contains("New Swimlane 3"));

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 9: Add new swimlane with no title fails
    @Test
    public void testAddSwimlaneWithoutNameFails() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "#1" icon to the left of "Test 2"
        WebElement projectIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//strong[normalize-space()='#1']"))); // Adjust the locator as needed
        projectIcon.click();

        // And clicks the "Configure this project" link
        WebElement configureProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[contains(text(),'Configure this project')]")));
        configureProjectLink.click();

        // And clicks the "Swimlanes" link
        WebElement swimlanesLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Swimlanes']")));
        swimlanesLink.click();

        // And clicks the "Add a new swimlane" link
        WebElement addNewSwimlaneLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add a new swimlane")));
        addNewSwimlaneLink.click();

        // And enters "New Swimlane 3" in the "Name" field
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-name")));

        // And clicks the "Save" button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // Then "The name is required" is shown below the "Name" field
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("form-errors"))); // Adjust the locator as needed
        assertTrue(errorMessage.getText().contains("The name is required"));
        driver.findElement(By.linkText("cancel")).click();

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }


    // Test 10: 
    @Test
    public void testAddNewCategory() {
        // Given the user is on the login page (/login)
        driver.get("http://localhost:8080/login"); 

        // When the user enters "admin" in the "Username" field
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys("admin");

        // And enters "admin" in the "Password" field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin");

        // And clicks the "Sign in" button
        WebElement signInButton = driver.findElement(By.xpath("//button[@type='submit']"));
        signInButton.click(); 

        // And clicks the "#1" icon to the left of "Test 2"
        WebElement projectIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//strong[normalize-space()='#1']"))); // Adjust the locator as needed
        projectIcon.click();

        // And clicks the "Configure this project" link
        WebElement configureProjectLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[contains(text(),'Configure this project')]")));
        configureProjectLink.click();

        // And clicks the "Categories" link
        WebElement categoriesLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Categories']")));
        categoriesLink.click();

        // And clicks the "Add a new category" link
        WebElement addNewCategoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add a new category")));
        addNewCategoryLink.click();

        // And enters "New Category 2" in the "Category Name" field
        WebElement categoryNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-name")));
        categoryNameField.sendKeys("New Category 2");

        // And clicks the "Save" button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        saveButton.click();

        // Then "New Category 2" is shown in the table
        WebElement categoryInTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody//tr//td[1]")));
        assertTrue(categoryInTable.isDisplayed());
        assertTrue(categoryInTable.getText().contains("New Category 2"));

        // Given the previous assertion passed
        // Then the user clicks on the "A" icon in the top-right corner of the screen
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@title='admin']")));
        profileIcon.click();

        // And clicks the "Logout" link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='dropdown-submenu-open']//a[normalize-space()='Logout']")));
        logoutLink.click();
    }

}