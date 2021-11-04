package cnpr.lcss.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class SendEmailService {

    public boolean sendGmail(String userGmail, String accountName, String accountUsername, String accountPassword) throws AuthenticationFailedException {

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
            message.setFrom(new InternetAddress("nguyenthehuu116@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userGmail)
            );
            message.setSubject("Chào mừng bạn đến với trung tâm CNPR!");
            message.setText("Tài khoản đăng nhập của bạn là: " + accountUsername
                    + "\n\nMật khẩu: " + accountPassword
                    + "\n\nHãy nhớ đổi mật khẩu ngay lần đăng nhập đầu tiên nhé!"
                    + "\n\nChúc bạn một ngày vui vẻ!"
                    + "\n\nCNPR");

            Transport.send(message);
            result = true;
            return result;
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new AuthenticationFailedException("USERNAME AND PASSWORD NOT ACCEPT");
        }
    }

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
