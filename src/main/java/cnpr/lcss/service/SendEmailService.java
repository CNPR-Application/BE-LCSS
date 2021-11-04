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

    public boolean sendMailToGroup(String userGmail, String accountName,String className,String subjectName, String bookingDate,String oldOpeningDate, String newOpeningDate) throws AuthenticationFailedException {

        final String branchName = "LCSS-LANGUAGE CENTER SUPPORT SYSTEM";
        final String username = "lcssfall2021";
        final String password = "lcss@123";

        boolean result = false;
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
            message.setFrom(new InternetAddress(userGmail, branchName));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userGmail)
            );
            message.setSubject("Kính gửi: Anh/chị "+accountName+",");
            message.setText("Cám ơn anh chị đã đăng ký vào khóa học "+className+" môn "+subjectName+" của trung tâm CNPR vào ngày "+bookingDate+" vừa qua. Sau khi nhận đơn đăng ký, trung tâm đã xử lý và mong muốn khai giảng lớp vào ngày "+oldOpeningDate+" như dự tính.\n" +
                    "Tuy nhiên, do tình hình dịch bệnh, hiện lớp vẫn chưa đủ học viên đăng ký, trung tâm muốn thông báo sẽ dời khai giảng sang ngày "+newOpeningDate+". Mong quý học viên thông cảm !\n" +
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
}
