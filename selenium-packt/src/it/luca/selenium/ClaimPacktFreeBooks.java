package it.luca.selenium;

import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.api.services.gmail.Gmail;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

import junit.framework.Assert;

public class ClaimPacktFreeBooks {
	private Selenium selenium;

	private static WebDriver driver;

	// Sender's email
	private static final String from = "automatic@gmail.com";
	
	// Recipient's email
	private static final String to = "email@email.com";
	
	// Packt login
	private static final String packtLogin = "email@";
	private static final String packtPassword = "pawd";
	
	private static final String chromeDriverPath = 
			"C:\\Users\\lucaa\\Documents\\selenium\\selenium-luca\\selenium-packt\\driver\\chromedriver.exe";

	static Properties mailServerProperties;

	private static Logger log = Logger.getLogger(ClaimPacktFreeBooks.class);
	
	public static void main(String[] args) throws Exception {
		JUnitCore.main("it.luca.selenium.ClaimPacktFreeBooks");
	}

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		driver = new ChromeDriver();

		// driver = new FirefoxDriver();

		String baseUrl = "https://www.packtpub.com/packt/offers/free-learning";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}
 
	@SuppressWarnings("deprecation")
	@Test
	public void testClaimPacktFreeBooks() throws Exception {
		try {
			// Open first page
			selenium.open("https://www.packtpub.com/packt/offers/free-learning");

			WebElement title = driver.findElement(By.cssSelector("div.dotd-title h2"));
			String bookTitle = title.getText();
			log.info("book to claim: " + title.getText());

			removeBox();

			if (!isLoggedIn())
				login();

			// click claim button
			selenium.click("//a/div/input");
			selenium.waitForPageToLoad("30000");

			List<WebElement> productList = driver.findElements(By.cssSelector("#product-account-list > div"));

			int i = 0;
			String bookList = "Last 10 Books already claimed:\n";
			for (WebElement p : productList) {
				i++;
				String t = p.getAttribute("title");
				//System.out.println("\t" + t);
				bookList += "\t" + t + "\n";
				if (i > 10)
					break;
			}

			log.info(bookList);
			
			// send email
			String emaiTitle = "Book '" + bookTitle + "' claimed succesfully!";
			String emaiBody = "Dear Master,\n I have claimed the book '"
					+ bookTitle + "' succesfully! \n\n"+bookList+"\n\nSincerely,\nyour Raspberry.";
			
			Gmail service = GmailQuickstart.getGmailService();
			try {
				MimeMessage emailContent = GmailQuickstart.createEmail(to, from, emaiTitle, emaiBody);
				GmailQuickstart.sendMessage(service, "me", emailContent);
			
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			log.error("Cannot claim book!");
			e.printStackTrace();
			Assert.fail("ERRORE");
		}
	}

	private void removeBox() throws InterruptedException {
		log.info("Has box?");

		WebElement box = getWebElement("w-div");
		if (box != null) {
			log.info("YES");

			String boxId = box.getAttribute("id");

			// remove box
			JavascriptExecutor js;
			if (driver instanceof JavascriptExecutor) {
				js = (JavascriptExecutor) driver;

				js.executeScript("return document.getElementById('" + boxId + "').remove();");
			}

		} else {
			WebElement box2 = driver.findElement(By.tagName("w-div"));
			log.info("NO");
		}
	}

	public static WebElement getWebElement(String cssSelector) {
		WebElement myDynamicElement = null;
		try {
			myDynamicElement = (new WebDriverWait(driver, 10))
					.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
			return myDynamicElement;

		} catch (Exception ex) {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public void login() {
		log.info("Login... ");
		selenium.waitForPageToLoad("30000");

		// full page
		if (driver.findElement(By.id("account-bar-login-register")).isDisplayed()) {
			// click login
			selenium.click("//div[@id='account-bar-login-register']/a/div");

			// click emain input
			selenium.click("xpath=(//div[@id='email-wrapper']/input)[2]");

			// write email
			selenium.type("xpath=(//input[@id='email'])[2]", packtLogin);

			// click password
			selenium.click("xpath=(//input[@id='password'])[2]");

			// write password
			selenium.type("xpath=(//input[@id='password'])[2]", packtPassword);

			// click submit
			selenium.click("xpath=(//input[@id='edit-submit-1'])[2]");
			selenium.waitForPageToLoad("30000");

			// small page version
		} else {
			// click menu
			selenium.click("//div[@id='menuIcon']");

			selenium.waitForPageToLoad("30000");
			
			// click login
			selenium.click("//body[@id='ppv4']/div[6]/div/div/div");

			// write email
			selenium.type("id=email", packtLogin);

			// write password
			selenium.type("id=password", packtPassword);

			// click submit
			selenium.click("id=edit-submit-1");
			selenium.waitForPageToLoad("30000");
		}

		if (isLoggedIn())
			log.info("Logged!");
	}

	private boolean isLoggedIn() {
		if (driver.findElement(By.id("account-bar-logged-in")).isDisplayed())
			return true;
		return false;
	}


	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}