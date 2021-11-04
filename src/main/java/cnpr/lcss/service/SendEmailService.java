package cnpr.lcss.service;

import cnpr.lcss.util.Constant;
import org.apache.tomcat.util.bcel.Const;
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
            message.setSubject("Chào mừng bạn đến với trung tâm CNPR!");
            message.setText("Tài khoản đăng nhập của bạn là: " + accountUsername
                    + "\n\nMật khẩu: " + accountPassword
                    + "\n\nHãy nhớ đổi mật khẩu ngay lần đăng nhập đầu tiên nhé!"
                    + "\n\nChúc bạn một ngày vui vẻ!"
                    + "\n\nCNPR.");

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
            message.setSubject("Chào, " + accountName);
            message.setText("Hệ thống trung tâm ngoại ngữ CNPR chúng tôi vừa nhận được yêu cầu khôi phục mật khẩu từ bạn với tài khoản " + accountUsername + ", " + " mật khẩu của bạn là: " + accountPassword + ".\n" +
                    "\n" +
                    "\n" +
                    "Chân thành cám ơn,\n" +
                    "CNPR.");

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
