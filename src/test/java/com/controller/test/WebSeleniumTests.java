package com.controller.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebSeleniumTests {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static Actions actions;
    private static final String BASE_URL = "http://localhost:8080";

    private static final String SEEKER_EMAIL = "test.11111111s@example.com";
    private static final String EMPLOYER_EMAIL = "test.11111111e@example.com";
    private static final String PASSWORD = "password123";
    private static final String EXISTING_SEEKER_EMAIL = "v@gmail.com";
    private static final String EXISTING_PASSWORD = "123";
    private static final String EXISTING_EMPLOYER_EMAIL = "gay@website.com";

    @BeforeAll
    static void setupDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();
        actions = new Actions(driver);
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void navigateToHome() {
        driver.get(BASE_URL);
    }

    @AfterEach
    void quitLogin() {
        driver.get(BASE_URL + "/logout");
    }

    public static WebElement findElementWithScroll(WebDriver driver, By locator, int timeoutSeconds) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        js.executeScript("window.scrollTo(0, 0);");

        int scrollStep = 300;
        long currentScrollPosition = 0;
        long pageHeight = (Long) js.executeScript("return document.body.scrollHeight");

        while (System.currentTimeMillis() - startTime < timeout) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    WebElement element = elements.get(0);
                    if (element.isDisplayed()) {
                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                        Thread.sleep(300);
                        return element;
                    }
                }

                currentScrollPosition += scrollStep;
                js.executeScript("window.scrollTo(0, " + currentScrollPosition + ");");
                Thread.sleep(200);

                long currentPosition = (Long) js.executeScript("return window.pageYOffset + window.innerHeight");
                if (currentPosition >= pageHeight) {
                    break;
                }

            } catch (Exception e) {
            }
        }

        return null;
    }


    private static void safeClick(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", element);
            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (ElementClickInterceptedException | InterruptedException e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception jsException) {
                actions.moveToElement(element).click().perform();
            }
        }
    }

    private static void scrollClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        safeClick(element);
    }

    @Test
    @Order(1)
    void testSuccessfulSeekerRegistration() {
        driver.get(BASE_URL + "/register");

        fillRegistrationForm(
                SEEKER_EMAIL,
                PASSWORD,
                "John Seeker",
                "01-01-2003",
                "Moscow",
                "+7-123-456-7890",
                true
        );

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getCurrentUrl().contains("/profile"));

        assertTrue(driver.getPageSource().contains("Personal Information"));
        assertTrue(driver.getPageSource().contains("Add Resume"));
    }

    @Test
    @Order(2)
    void testSuccessfulEmployerRegistration() {
        driver.get(BASE_URL + "/register");

        fillRegistrationForm(
                EMPLOYER_EMAIL,
                PASSWORD,
                "Test Company",
                "01-01-2003",
                "St. Petersburg",
                "+7-987-654-3210",
                false
        );

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getCurrentUrl().contains("/profile"));

        assertTrue(driver.getPageSource().contains("Company Profile"));
        assertTrue(driver.getPageSource().contains("Add Vacancy"));
    }

    @Test
    @Order(3)
    void testRegistrationWithExistingEmail() {
        driver.get(BASE_URL + "/register");

        fillRegistrationForm(
                EXISTING_SEEKER_EMAIL,
                PASSWORD,
                "Duplicate User",
                "1990-01-01",
                "Moscow",
                "+7-111-111-1111",
                true
        );

        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/register"));
        WebElement errorElement = findElementWithScroll(driver,
                By.xpath("//*[contains(text(), 'Email already registered')]"), 10);

        Assertions.assertNotNull(errorElement, "Error message not found");
        Assertions.assertTrue(errorElement.getText().contains("Email already registered"));
        loginAsSeeker();
    }

    @Test
    @Order(5)
    void testSuccessfulLogin() {
        driver.get(BASE_URL + "/login");

        fillLoginForm(EXISTING_SEEKER_EMAIL, EXISTING_PASSWORD);

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getCurrentUrl().contains("/profile"));
    }

    @Test
    @Order(6)
    void testLoginWithWrongPassword() {
        driver.get(BASE_URL + "/login");

        fillLoginForm(SEEKER_EMAIL, "wrongpassword");

        WebElement errorElement = findElementWithScroll(driver,
                By.className("alert-danger"), 10);

        Assertions.assertNotNull(errorElement, "Error message not found");
        Assertions.assertTrue(errorElement.getText().contains("Invalid email or password"));
        loginAsSeeker();
    }

    @Test
    @Order(7)
    void testLoginWithNonexistentEmail() {
        driver.get(BASE_URL + "/login");

        fillLoginForm("nonexistent@example.com", PASSWORD);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("alert-danger")));
        assertTrue(driver.getPageSource()
                .contains("Invalid email or password"));
        loginAsSeeker();
    }

    @Test
    @Order(8)
    void testSuccessfulVacancyCreation() {
        loginAsEmployer();

        driver.get(BASE_URL + "/vacancies/add");

        driver.findElement(By.id("position")).sendKeys("Software Developer");
        driver.findElement(By.id("salary")).sendKeys("100000");
        driver.findElement(By.id("requirements"))
                .sendKeys("Java, Spring, Hibernate experience required");
        scrollClick(By.cssSelector("button[type='submit']"));

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getPageSource().contains("Software Developer"));
        assertTrue(driver.getPageSource().contains("100000"));
    }

    @Test
    @Order(9)
    void testVacancyCreationAsSeeker() {
        loginAsSeeker();

        driver.get(BASE_URL + "/vacancies/add");

        assertFalse(driver.getPageSource().contains("Add Vacancy"));
    }

    @Test
    @Order(10)
    void testVacancyCreationUnauthenticated() {
        driver.get(BASE_URL + "/vacancies/add");
        assertTrue(driver.getCurrentUrl().contains("/login") ||
                !driver.getPageSource().contains("Add Vacancy"));
        loginAsSeeker();
    }

    @Test
    @Order(11)
    void testVacancyEditing() {
        loginAsEmployer();
        driver.get(BASE_URL + "/profile");
        scrollClick(By.linkText("Edit"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("position")));

        WebElement positionField = driver.findElement(By.id("position"));
        positionField.clear();
        positionField.sendKeys("Senior Software Developer");

        scrollClick(By.cssSelector("button[type='submit']"));

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getPageSource()
                .contains("Senior Software Developer"));
    }

    @Test
    @Order(12)
    void testVacancyDeletion() {
        loginAsEmployer();

        driver.get(BASE_URL + "/profile");

        int vacanciesBefore = driver.findElements(
                By.xpath("//table//tr[td]")).size();

        scrollClick(By.linkText("Delete"));

        wait.until(ExpectedConditions.urlContains("/profile"));

        int vacanciesAfter = driver.findElements(
                By.xpath("//table//tr[td]")).size();

        Assertions.assertEquals(vacanciesBefore - 1, vacanciesAfter);
    }

    @Test
    @Order(13)
    void testSuccessfulResumeCreation() {
        loginAsSeeker();

        driver.get(BASE_URL + "/resumes/add");

        driver.findElement(By.id("wantedPosition")).sendKeys("Java Developer");
        driver.findElement(By.id("wantedSalary")).sendKeys("95000");
        driver.findElement(By.id("skills"))
                .sendKeys("Java, Spring Boot, PostgreSQL, Git");
        scrollClick(By.cssSelector("button[type='submit']"));

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getPageSource().contains("Java Developer"));
        assertTrue(driver.getPageSource().contains("95000"));
    }

    @Test
    @Order(14)
    void testResumeCreationAsEmployer() {
        loginAsEmployer();
        driver.get(BASE_URL + "/resumes/add");
        assertFalse(driver.getPageSource().contains("Add Resume"));
    }

    @Test
    @Order(15)
    void testResumeEditing() {
        loginAsSeeker();

        driver.get(BASE_URL + "/profile");
        scrollClick(By.linkText("Edit"));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("wantedPosition")));

        WebElement salaryField = driver.findElement(By.id("wantedSalary"));
        salaryField.clear();
        salaryField.sendKeys("105000");

        scrollClick(By.cssSelector("button[type='submit']"));

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getPageSource().contains("105000"));
    }

    @Test
    @Order(16)
    void testAddEducation() {
        loginAsSeeker();

        driver.get(BASE_URL + "/profile");

        driver.findElement(By.id("institution")).sendKeys("Moscow State University");
        driver.findElement(By.id("specialization")).sendKeys("Computer Science");
        driver.findElement(By.id("endYear")).sendKeys("2020");
        scrollClick(By.xpath("//button[text()='Add Education']"));

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getPageSource()
                .contains("Moscow State University"));
        assertTrue(driver.getPageSource().contains("Computer Science"));
        assertTrue(driver.getPageSource().contains("2020"));
    }

    @Test
    @Order(17)
    void testAddExperience() {
        loginAsSeeker();

        driver.get(BASE_URL + "/profile");

        driver.findElement(By.id("company")).sendKeys("Tech Corp");
        driver.findElement(By.id("position")).sendKeys("Junior Developer");
        driver.findElement(By.id("salary")).sendKeys("60000");
        driver.findElement(By.id("startDate")).sendKeys("01-01-2020");
        driver.findElement(By.id("endDate")).sendKeys("31-12-2022");
        scrollClick(By.xpath("//button[text()='Add Experience']"));

        wait.until(ExpectedConditions.urlContains("/profile"));
        assertTrue(driver.getPageSource().contains("Tech Corp"));
        assertTrue(driver.getPageSource().contains("Junior Developer"));
    }

    @Test
    @Order(18)
    void testApplyToVacancy() {
        loginAsSeeker();

        driver.get(BASE_URL + "/vacancies/search");
        scrollClick(By.cssSelector("button[type='submit']"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Software Engineer")));
        scrollClick(By.linkText("Software Engineer"));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.linkText("Apply")));
        scrollClick(By.linkText("Apply"));

        wait.until(ExpectedConditions.urlContains("/vacancies/view/"));
        assertTrue(driver.getCurrentUrl().contains("/vacancies/view/"));
    }

    @Test
    @Order(19)
    void testUpdateResponseStatus() {
        loginAsEmployer();

        driver.get(BASE_URL + "/profile");

        if (driver.getPageSource().contains("Pending")) {
            Select statusSelect = new Select(driver.findElement(
                    By.name("status")));
            statusSelect.selectByValue("Reviewed");

            scrollClick(By.xpath("//button[text()='Update']"));

            wait.until(ExpectedConditions.urlContains("/profile"));
            assertTrue(driver.getPageSource().contains("Reviewed"));
        }
    }

    @Test
    @Order(20)
    void testVacancySearch() {
        loginAsSeeker();
        driver.get(BASE_URL + "/vacancies/search");

        Select positionSelect = new Select(driver.findElement(By.id("position")));
        if (positionSelect.getOptions().size() > 1) {
            positionSelect.selectByIndex(1);
        }

        scrollClick(By.cssSelector("button[type='submit']"));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h3[contains(text(), 'Results')]")));
        assertTrue(driver.getPageSource().contains("Results"));
    }

    @Test
    @Order(21)
    void testResumeSearch() {
        loginAsSeeker();
        driver.get(BASE_URL + "/resumes/search");

        driver.findElement(By.id("minSalary")).sendKeys("50000");
        driver.findElement(By.id("maxSalary")).sendKeys("150000");

        scrollClick(By.cssSelector("button[type='submit']"));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h3[contains(text(), 'Results')]")));
        assertTrue(driver.getPageSource().contains("Results"));
    }

    @Test
    @Order(22)
    void testLogout() {
        loginAsSeeker();

        scrollClick(By.linkText("Logout"));

        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
        assertTrue(driver.getPageSource().contains("Login"));
        assertFalse(driver.getPageSource().contains("Logout"));

        loginAsSeeker();
    }

    @Test
    @Order(23)
    void testProfileAccessWithoutLogin() {
        driver.get(BASE_URL + "/profile");

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
        loginAsSeeker();
    }

    private void fillRegistrationForm(String email, String password, String name,
                                      String birthDate, String city, String phone,
                                      boolean isSeeker) {
        driver.findElement(By.id("regEmail")).sendKeys(email);
        driver.findElement(By.id("regPassword")).sendKeys(password);
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("birthDate")).sendKeys(birthDate);
        driver.findElement(By.id("city")).sendKeys(city);
        driver.findElement(By.id("phone")).sendKeys(phone);

        Select roleSelect = new Select(driver.findElement(By.id("role")));
        roleSelect.selectByValue(String.valueOf(isSeeker));

        scrollClick(By.cssSelector("button[type='submit']"));
    }

    private void fillLoginForm(String email, String password) {
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        scrollClick(By.cssSelector("button[type='submit']"));
    }

    private void loginAsSeeker() {
        driver.get(BASE_URL + "/login");
        fillLoginForm(EXISTING_SEEKER_EMAIL, EXISTING_PASSWORD);
        wait.until(ExpectedConditions.urlContains("/profile"));
    }

    private void loginAsEmployer() {
        driver.get(BASE_URL + "/login");
        fillLoginForm(EXISTING_EMPLOYER_EMAIL, EXISTING_PASSWORD);
        wait.until(ExpectedConditions.urlContains("/profile"));
    }
}