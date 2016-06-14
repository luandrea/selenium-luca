package it.luca.selenium;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

public class ClaimPacktFreeBooks {
	private Selenium selenium;

	WebDriver driver;
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\lucaa\\Documents\\selenium\\selenium-luca\\selenium-packt\\driver\\chromedriver.exe");
		driver = new ChromeDriver();
		
		//driver = new FirefoxDriver();
		
		String baseUrl = "https://www.packtpub.com/packt/offers/free-learning";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testClaimPacktFreeBooks2() throws Exception {

		// Open first page
		selenium.open("https://www.packtpub.com/packt/offers/free-learning");
		
		WebElement title = driver.findElement(By.cssSelector("div.dotd-title h2"));
		System.out.println("book to claim: "+title.getText());
		
		if (!isLoggedIn())
			login();
		
		// click claim button
		selenium.click("//div[@id='deal-of-the-day']/div/div/div[2]/div[4]/div[2]/a/div/input");
		selenium.waitForPageToLoad("30000");
		
		List<WebElement> productList = driver.findElements(By.cssSelector("#product-account-list div"));
		
		for (WebElement p : productList) {
			String t = p.getAttribute("title");
			System.out.println("\t"+t);
		}
	}
	
	public void login() {
		System.out.println("Login... ");
		selenium.waitForPageToLoad("30000");
		
		// click login
		selenium.click("//div[@id='account-bar-login-register']/a/div");
		
//		// click emain input
//		selenium.click("//div[@id='menu-icon']");
//		
//		div.respoLogin
//		
		// click emain input
		selenium.click("xpath=(//div[@id='email-wrapper']/input)[2]");
		
		// write email
		selenium.type("xpath=(//input[@id='email'])[2]", "email@gmail.com");
		
		// click password 
		selenium.click("xpath=(//input[@id='password'])[2]");
		
		// write password
		selenium.type("xpath=(//input[@id='password'])[2]", "password");
		
		// click submit
		selenium.click("xpath=(//input[@id='edit-submit-1'])[2]");
		selenium.waitForPageToLoad("30000");
		
		if (isLoggedIn())
			System.out.println("Logged!");
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
