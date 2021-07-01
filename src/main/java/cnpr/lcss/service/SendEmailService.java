package cnpr.lcss.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendEmailService {
    public void sendGmail(String userGmail, String accountName, String accountUsername, String accountPassword) {
        final String branchName = "LCSS-LANGUAGE CENTER SUPPORT SYSTEM";
        final String username = "nguyenthehuu116";
        final String password = "Thehuu2908";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("nguyenthehuu116@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userGmail)
            );
            message.setSubject("WELCOME TO " + branchName);
            message.setText("Dear " + accountName + ","
                    + "\n\nThank you for joining us!"
                    + "\n\nHere is your username: " + accountUsername
                    + "\n\npassword: " + accountPassword
                    + "\n\nPlease change your password after the 1st login.");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
