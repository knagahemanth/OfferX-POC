package Automation.SmokeTestExecution;

import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class SmokeTestExecution {
	// Locators used across different environments
	By microsoftemailidbutton = By.xpath("//input[@name='loginfmt']");
	By microsoftnextbutton = By.xpath("//input[@type='submit']");
	By microsoftpassword = By.xpath("//input[@name='passwd']");
	By microsoftnextbuttonpasswordpage = By.xpath("//button[@type='submit']");
	By microsoftsigninbutton = By.xpath(
			"//div[contains(@class, 'ext-button-item') and contains(@class, '___frx9oy0') and contains(@class, 'f14t3ns0')]/button[contains(@class, 'ext-primary') and contains(@class, 'ext-button')]");
	By microsoftstaysignedin = By.xpath("//button[@data-testid='secondaryButton']");
	By signinbutton = By.xpath("//a[@title='Sign In']");
	By signinwithMicrosoft = By.xpath("//span[text()='Sign in with Microsoft']");
	By releaseoffer = By.xpath("//a[normalize-space()='Release an offer']");
	By accepted = By.xpath("//button[text()='Accepted']");
	By Onboarded = By.xpath("//button[text()='Onboarded']");
	By Ghosted = By.xpath("//button[text()='Ghosted']");
	By Declined = By.xpath("(//button[text()='Declined'])[2]");
	By Retracted = By.xpath("(//button[text()='Retracted'])[2]");
	By expired = By.xpath("(//button[text()='Expired'])[2]");
	By profile = By.xpath("//img[@title='My profile']");
	By Signout = By.xpath("//button[text()='Sign out']");

	private static WebDriver driver = null;
	ExtentSparkReporter htmlReporter;
	ExtentReports extent;
	ExtentTest testProd, testUat, testTest;

	@BeforeSuite
	public void setup() throws IOException {
		// Define the location of the ExtentReport configuration file
		final File CONF = new File(
				"C:\\Users\\hemanth.konduru\\eclipse-workspace\\SmokeTest\\test-output\\extentconfig.xml");
		htmlReporter = new ExtentSparkReporter("extentReport.html");
		extent = new ExtentReports();
		// Load the Extent Report configuration
		htmlReporter.loadXMLConfig(CONF);
		extent.attachReporter(htmlReporter);
	}

	// Initiating Chrome browser
	@BeforeMethod
	public void ChromeInstant() {
		try {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Smoke test for PROD environment
	@Test(priority = 1, enabled = true)
	public void testProdEnvironment() throws InterruptedException {
		testProd = extent.createTest("TC001_PROD_Environment",
				"Smoke Test Execution Steps for the Production Environment.");
		testProd.log(Status.INFO, "Starting Test Case.");
		// String username = KeyVaultHelper.getSecret("your-key-vault-name",
		// "your-username-secret");
		// String password = KeyVaultHelper.getSecret("OfferxScript-keyvault",
		// "Email-Password");
		try {
			navigateToUrl("https://www.offerx.in/", signinbutton);
			testProd.log(Status.PASS, "The Browser has launched.");
			testProd.log(Status.PASS, "Navigated to the OfferX homepage.");
			clickElement(driver, signinbutton, 30);
			testProd.log(Status.PASS, "Clicked on the Sign-In button on the homepage.");
			clickElement(driver, signinwithMicrosoft, 30);
			testProd.log(Status.PASS, "Selected 'Sign in with Microsoft' option.");
			switchToNewWindow();
			waitForElementAndSendKeys(microsoftemailidbutton, "rohan.mandal2024@outlook.com", Duration.ofSeconds(30));
			clickElement(driver, microsoftnextbutton, 30);
			waitForElementAndSendKeys(microsoftpassword, "Test@456", Duration.ofSeconds(30));
			testProd.log(Status.PASS, "Entered Microsoft Account Credentials.");
			clickElement(driver,microsoftnextbuttonpasswordpage, 30);
			testProd.log(Status.PASS, "Clicked on the Sign-In button.");
			clickElement(driver, microsoftstaysignedin, 30);
			driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
			testProd.log(Status.PASS, "The user logged in to the application.");
			performClick(accepted, "Accepted Tab");
			performClick(Onboarded, "Onboarded Tab");
			performClick(Ghosted, "Ghosted Tab");
			performClick(Declined, "Declined Tab");
			performClick(Retracted, "Retracted Tab");
			performClick(expired, "Expired Tab");
			performClick(releaseoffer, "Releaseoffer Option");
			clickElement(driver, profile, 30);
			testProd.log(Status.PASS, "Profile Tab is Clicked.");
			clickElement(driver, Signout, 30);
			testProd.log(Status.PASS, "Sign-out option is Clicked.");
			testProd.log(Status.PASS, "Logged out of the application.");
		} catch (Exception e) {
			ExceptionStorage.getInstance().storeException("PROD", "Production failed", "FAILED");
			testProd.log(Status.FAIL,
					MarkupHelper.createLabel("The Test Has Failed for the Production Environment.", ExtentColor.RED));
		}

	}

	// Smoke test for UAT environment
	@Test(priority = 2, enabled = true)
	public void testUatEnvironment() throws InterruptedException {
		testUat = extent.createTest("TC002_UAT_Environment", "Smoke Test Execution Steps for the  UAT Environment.");
		testUat.log(Status.INFO, "Starting test case.");
		try {
			navigateToUrl("https://uat.offerx.in/", signinbutton);
			testUat.log(Status.PASS, "The Browser has launched.");
			testUat.log(Status.PASS, "Navigated to the OfferX homepage.");
			clickElement(driver, signinbutton, 30);
			testUat.log(Status.PASS, "Clicked on the Sign-In button on the homepage.");
			clickElement(driver, signinwithMicrosoft, 30);
			testUat.log(Status.PASS, "Selected 'Sign in with Microsoft' option.");
			switchToNewWindow();
			waitForElementAndSendKeys(microsoftemailidbutton, "saiavinash9596@outlook.com", Duration.ofSeconds(30));
			clickElement(driver, microsoftnextbutton, 30);
			waitForElementAndSendKeys(microsoftpassword, "Avi@1997", Duration.ofSeconds(60));
			testUat.log(Status.PASS, "Entered Microsoft Account Credentials.");
			clickElement(driver,microsoftnextbuttonpasswordpage, 30);
			testUat.log(Status.PASS, "Clicked on the Sign-In button.");
			clickElement(driver, microsoftstaysignedin, 30);
			driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
			testUat.log(Status.PASS, "The user logged in to the application.");
			clickElement(driver, profile, 30);
			testUat.log(Status.PASS, "Profile Tab is Clicked.");
			clickElement(driver, Signout, 30);
			testUat.log(Status.PASS, "Sign-out option is Clicked.");
			testUat.log(Status.PASS, "Logged out of the application.");
		} catch (Exception e) {
			ExceptionStorage.getInstance().storeException("UAT", "UAT failed", "FAILED");
			testUat.log(Status.FAIL,
					MarkupHelper.createLabel("The Test Has Failed for the UAT Environment.", ExtentColor.RED));
		}
	}

	// Smoke test for TEST environment
	@Test(priority = 3, enabled = true)
	public void testTestEnvironment() throws InterruptedException {
		testTest = extent.createTest("TC003_TEST_Environment", "Smoke Test Execution Steps for the  TEST Environment");
		testTest.log(Status.INFO, "Starting test case.");

		try {
			navigateToUrl("https://test.offerx.in/", signinbutton);
			testTest.log(Status.PASS, "The Browser has launched.");
			testTest.log(Status.PASS, "Navigated to the OfferX homepage.");
			clickElement(driver, signinbutton, 30);
			testTest.log(Status.PASS, "Clicked on the Sign-In button on the homepage.");
			clickElement(driver, signinwithMicrosoft, 30);
			testTest.log(Status.PASS, "Selected 'Sign in with Microsoft' option.");
			switchToNewWindow();
			waitForElementAndSendKeys(microsoftemailidbutton, "saiavinash9596@outlook.com", Duration.ofSeconds(30));
			clickElement(driver, microsoftnextbutton, 30);
			waitForElementAndSendKeys(microsoftpassword, "Avi@1997", Duration.ofSeconds(30));
			testTest.log(Status.PASS, "Entered Microsoft Account Credentials.");
			clickElement(driver,microsoftnextbuttonpasswordpage, 30);
			testTest.log(Status.PASS, "Clicked on the Sign-In button.");
			clickElement(driver, microsoftstaysignedin, 30);
			driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
			testTest.log(Status.PASS, "The user logged in to the application.");
			clickElement(driver, profile, 30);
			testTest.log(Status.PASS, "Profile Tab is Clicked.");
			clickElement(driver, Signout, 30);
			testTest.log(Status.PASS, "Sign-out option is Clicked.");
			testTest.log(Status.PASS, "Logged out of the application.");
		} catch (Exception e) {
			ExceptionStorage.getInstance().storeException("TEST", "TEST failed", "FAILED");
			testTest.log(Status.FAIL,
					MarkupHelper.createLabel("The Test Has Failed for the TEST Environment.", ExtentColor.RED));
		}

	}

	// Click with retry mechanism
	private void performClick(By locator, String elementName) {
		int attempts = 0;
		// Loop until the element is clicked or maximum attempts=3
		while (attempts < 3) {
			try {
				// Create a WebDriverWait with a timeout of 30 seconds
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				// Wait until the element is clickable
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
				element.click();
				// Log the successful click action
				testProd.log(Status.PASS, elementName + " is Clicked.");
				break;// Exit the loop if the click is successful
			} catch (Exception e) {
				// Increment the attempt counter if an exception occurs
				attempts++;
				if (attempts == 3) {
					try {
						WebElement element = driver.findElement(locator);
						// Use JavaScript to click the element as a fallback
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
					} catch (Exception jsException) {
						// Log the failure in clicking the element
						if (!hasEnvironmentFailureLogged("PROD")) {
							// Store the exception.
							ExceptionStorage.getInstance().storeException("PROD", "Production failed", "FAILED");
						}
						// Log the failure in the test report
						testProd.log(Status.FAIL,
								MarkupHelper.createLabel(elementName + " failed to Click", ExtentColor.RED));
					}
				}
			}
		}
	}

	// Navigation to the URL with the explicit wait condition.
	public void navigateToUrl(String url, By waitForElement) {
		driver.get(url);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOfElementLocated(waitForElement));
	}

	// Click element with wait condition
	public void clickElement(WebDriver driver, By locator, int timeoutInSeconds) {
		// Creating a FluentWait with the specified timeout and polling every 1 second
		FluentWait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeoutInSeconds))
				.pollingEvery(Duration.ofSeconds(1)) // Polling every 1 second
				.ignoring(NoSuchElementException.class) // Ignoring NoSuchElementException
				.ignoring(StaleElementReferenceException.class); // Ignoring StaleElementReferenceException

		// Wait until the element is clickable
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		// Click on the element once it is clickable
		element.click();
	}

	// Check if failure is already logged for a specific environment
	private boolean hasEnvironmentFailureLogged(String environment) {
		JSONArray exceptions = ExceptionStorage.getInstance().getExceptions();
		for (int i = 0; i < exceptions.length(); i++) {
			JSONObject exception = exceptions.getJSONObject(i);
			if (exception.getString("environment").equals(environment)) {
				return true;
			}
		}
		return false;
	}

	// Switch to the newly opened window
	private void switchToNewWindow() {
		String parentWindowHandle = driver.getWindowHandle();
		for (String windowHandle : driver.getWindowHandles()) {
			if (!windowHandle.equals(parentWindowHandle)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}
	}

	// Wait for the element and then send keys
	public void waitForElementAndSendKeys(By locator, String text, Duration timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		element.sendKeys(text);
	}

	// Tear down the driver after each test method
	@AfterMethod()
	public void teardown() {
		driver.quit();
	}

	// Flush the extent report after completing the test suite
	@AfterSuite
	public void flush() {
		extent.flush();
	}
}