package it.unisa.metric.web.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.tomcat.jni.Address;

public class EmailUtility {
	
	public static void sendEmail(String host, String port,
            final String userName, final String password, String fromAddress,
            String subject, String message) throws AddressException,
            MessagingException {
		String to="unicafetest@gmail.com";
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress(fromAddress));
        InternetAddress[] fromA= {  new InternetAddress(fromAddress) };
       
        //msg.addFrom(fromA);
        msg.setReplyTo(fromA);
        InternetAddress[] toA= { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.TO, toA);
        
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        String messaggio="email: " + fromAddress + "\n"+  "Message: " + message;
        msg.setText(messaggio);
        // sends the e-mail
        Transport.send(msg);
 
    }
}
