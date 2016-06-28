package it.luca.selenium;

import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

public class ClaimPacktFreeBooks {
	private Selenium selenium;

	private static WebDriver driver;

	// Sender's email
	private static final String from = "automatic@gmail.com";
	
	// Recipient's email
	private static final String to = "email@email.com";
	private static final String to2 = "email@email.com";

	// Assuming you are sending email from localhost
	String host = "localhost";	

	// Email login
	private static final String emailLogin = "email@email.com";
	private static final String emailPassword = "password";
	
	// Packt login
	private static final String packtLogin = "email@email.com";
	private static final String packtPassword = "password";

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;

	public static void generateAndSendEmailGMail() throws AddressException, MessagingException {
		 Properties props = new Properties();
		 props.put("mail.imap.ssl.enable", "true"); // required for Gmail
		 props.put("mail.imap.auth.mechanisms", "XOAUTH2");
		 Session session = Session.getInstance(props);
		 Store store = session.getStore("imap");
		 store.connect("imap.gmail.com", emailLogin, emailPassword); 
	}
 
	public static void generateAndSendEmail() throws AddressException, MessagingException {
 
		// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
 
		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
		generateMailMessage.addRecipient(RecipientType.CC, new InternetAddress(to2));
		generateMailMessage.setSubject("Greetings from Crunchify..");
		String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");
 
		// Step3
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");
 
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", emailLogin, emailPassword);
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\lucaa\\Documents\\selenium\\selenium-luca\\selenium-packt\\driver\\chromedriver.exe");
		driver = new ChromeDriver();

		// driver = new FirefoxDriver();

		String baseUrl = "https://www.packtpub.com/packt/offers/free-learning";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}
 
	@Test
	public void testSendEmail() throws Exception {
		try {
			generateAndSendEmailGMail();
			//generateAndSendEmail();
			System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
			//sendEmail("Prova email", "questa è una prova per vedere se riesco ad inviare una mail");
		} catch (Exception e) {
			System.out.println("Cannot send email!");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testClaimPacktFreeBooks() throws Exception {
		try {
			// Open first page
			selenium.open("https://www.packtpub.com/packt/offers/free-learning");

			WebElement title = driver.findElement(By.cssSelector("div.dotd-title h2"));
			String bookTitle = title.getText();
			System.out.println("book to claim: " + title.getText());

			removeBox();

			if (!isLoggedIn())
				login();

			// click claim button
			selenium.click("//div[@id='deal-of-the-day']/div/div/div[2]/div[4]/div[2]/a/div/input");
			selenium.waitForPageToLoad("30000");

			List<WebElement> productList = driver.findElements(By.cssSelector("#product-account-list > div"));

			int i = 0;
			String bookList = "Last 10 Books already claimed:\n";
			for (WebElement p : productList) {
				i++;
				String t = p.getAttribute("title");
				System.out.println("\t" + t);
				bookList += "\t" + t + "\n";
				if (i > 10)
					break;
			}

			// send email
			sendEmail("Book '" + bookTitle + "' claimed succesfully!", "Dear Master,\n I have claimed the book '"
					+ bookTitle + "' succesfully! \n\n"+bookList+"\n\nSincerely,\nyour Raspberry.");

		} catch (Exception e) {
			System.out.println("Cannot claim book!");
			e.printStackTrace();
		}
	}

	private void removeBox() throws InterruptedException {
		System.out.print("Has box?");

		WebElement box = getWebElement("w-div");
		if (box != null) {
			System.out.println("YES");

			String boxId = box.getAttribute("id");

			// remove box
			JavascriptExecutor js;
			if (driver instanceof JavascriptExecutor) {
				js = (JavascriptExecutor) driver;

				js.executeScript("return document.getElementById('" + boxId + "').remove();");
			}

		} else {
			WebElement box2 = driver.findElement(By.tagName("w-div"));
			System.out.println("NO");
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
		System.out.println("Login... ");
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
			System.out.println("Logged!");
	}

	private boolean isLoggedIn() {
		if (driver.findElement(By.id("account-bar-logged-in")).isDisplayed())
			return true;
		return false;
	}

	private void sendEmail(String subject, String body) {
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));
			message.addRecipient(RecipientType.TO, new InternetAddress(to));

			message.setSubject(subject);
			message.setText(body);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}