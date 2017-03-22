package com.emailer.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.emailer.dto.MessageRequest;

public final class EmailUtil {

	private EmailUtil() {
		
	}
	
	public static void send(MessageRequest request) {
	   
	      Properties properties = System.getProperties();
	      // Setup mail server
	      properties.setProperty("mail.smtp.host", "localhost");
	      properties.setProperty("mail.smtp.port", "1025");
	      Session session = Session.getDefaultInstance(properties);

	      try {
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress("testadmin@test.com"));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getEmailAddress()));
	         message.setSubject(request.getSubject());
	         message.setText(request.getBody());
	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException ex) {
	    	 System.out.println("Email sending exception : " + ex);
	      }
	}
	
}
