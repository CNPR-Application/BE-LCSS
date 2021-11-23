package cnpr.lcss.service;

import cnpr.lcss.util.Constant;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class SendEmailService {
    //<editor-fold desc="1.05-create-new-account">
    public boolean sendGmail(String userGmail, String accountName, String accountUsername, String accountPassword) throws AuthenticationFailedException {
        boolean result = false;
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Constant.SYSTEM_MAIL_USERNAME, Constant.SYSTEM_MAIL_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userGmail, Constant.SYSTEM_NAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userGmail)
            );
            message.setSubject(Constant.SYSTEM_MAIL_SUBJECT_CREATE_NEW_ACOUNT);
            message.setText(String.format(Constant.SYSTEM_MAIL_CONTENT_CREATE_NEW_ACOUNT, accountUsername, accountPassword));

            Transport.send(message);
            result = true;
            return result;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AuthenticationFailedException("USERNAME AND PASSWORD NOT ACCEPT");
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.21-forgot-password">
    public boolean sendForgotMail(String userGmail, String accountName, String accountUsername, String accountPassword) throws AuthenticationFailedException {
        boolean result = false;
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Constant.SYSTEM_MAIL_USERNAME, Constant.SYSTEM_MAIL_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userGmail, Constant.SYSTEM_NAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userGmail)
            );
            message.setSubject(Constant.SYSTEM_MAIL_SUBJECT_FORGOT_PASSWORD);
            message.setText(String.format(Constant.SYSTEM_MAIL_CONTENT_FORGOT_PASSWORD,accountName,accountUsername,accountPassword));

            Transport.send(message);
            result = true;
            return result;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AuthenticationFailedException("USERNAME AND PASSWORD NOT ACCEPT");
        }
    }
    //</editor-fold>

    //<editor-fold desc="15.07 send-noti-and-email-to-group-person">
    public boolean sendMailToGroup(String userGmail, String accountName, String className, String oldOpeningDate, String newOpeningDate) throws AuthenticationFailedException {
        boolean result = false;
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Constant.SYSTEM_MAIL_USERNAME, Constant.SYSTEM_MAIL_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userGmail, Constant.SYSTEM_NAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userGmail)
            );
            message.setSubject(Constant.SYSTEM_MAIL_SUBJECT_SEND_NOTI_MAIL_TO_GROUP);
            message.setText(String.format(Constant.SYSTEM_MAIL_CONTENT_SEND_NOTI_EMAIL_TO_GROUP, accountName, className, oldOpeningDate, newOpeningDate));
            Transport.send(message);
            result = true;
            return result;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AuthenticationFailedException("USERNAME AND PASSWORD NOT ACCEPT");
        }
    }
    //</editor-fold>
}
