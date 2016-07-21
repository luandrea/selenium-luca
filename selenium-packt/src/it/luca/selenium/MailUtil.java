package it.luca.selenium;

import java.util.Properties;

import org.junit.Test;

public class MailUtil {
	
//	static Session getMailSession;
//	static MimeMessage generateMailMessage;
//
//	@Test
//	public void testSendEmail() throws Exception {
//		try {
//			generateAndSendEmailGMail();
//			//generateAndSendEmail();
//			System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
//			//sendEmail("Prova email", "questa è una prova per vedere se riesco ad inviare una mail");
//		} catch (Exception e) {
//			System.out.println("Cannot send email!");
//			e.printStackTrace();
//		}
//	}
//	
//	public static void generateAndSendEmailGMail() throws AddressException, MessagingException {
//		 Properties props = new Properties();
//		 props.put("mail.imap.ssl.enable", "true"); // required for Gmail
//		 props.put("mail.imap.auth.mechanisms", "XOAUTH2");
//		 Session session = Session.getInstance(props);
//		 Store store = session.getStore("imap");
//		 store.connect("imap.gmail.com", emailLogin, emailPassword); 
//	}
// 
//	public static void generateAndSendEmail() throws AddressException, MessagingException {
// 
//		// Step1
//		System.out.println("\n 1st ===> setup Mail Server Properties..");
//		mailServerProperties = System.getProperties();
//		mailServerProperties.put("mail.smtp.port", "587");
//		mailServerProperties.put("mail.smtp.auth", "true");
//		mailServerProperties.put("mail.smtp.starttls.enable", "true");
//		System.out.println("Mail Server Properties have been setup successfully..");
// 
//		// Step2
//		System.out.println("\n\n 2nd ===> get Mail Session..");
//		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
//		generateMailMessage = new MimeMessage(getMailSession);
//		generateMailMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
//		generateMailMessage.addRecipient(RecipientType.CC, new InternetAddress(to2));
//		generateMailMessage.setSubject("Greetings from Crunchify..");
//		String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
//		generateMailMessage.setContent(emailBody, "text/html");
//		System.out.println("Mail Session has been created successfully..");
// 
//		// Step3
//		System.out.println("\n\n 3rd ===> Get Session and Send mail");
//		Transport transport = getMailSession.getTransport("smtp");
// 
//		// Enter your correct gmail UserID and Password
//		// if you have 2FA enabled then provide App Specific Password
//		transport.connect("smtp.gmail.com", emailLogin, emailPassword);
//		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
//		transport.close();
//	}
//	
//
//	private void sendEmail(String subject, String body) {
//		Properties properties = System.getProperties();
//
//		// Setup mail server
//		properties.setProperty("mail.smtp.host", host);
//
//		// Get the default Session object.
//		Session session = Session.getDefaultInstance(properties);
//
//		try {
//			MimeMessage message = new MimeMessage(session);
//
//			message.setFrom(new InternetAddress(from));
//			message.addRecipient(RecipientType.TO, new InternetAddress(to));
//
//			message.setSubject(subject);
//			message.setText(body);
//
//			// Send message
//			Transport.send(message);
//			System.out.println("Sent message successfully....");
//
//		} catch (MessagingException mex) {
//			mex.printStackTrace();
//		}
//	}
}
