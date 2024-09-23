package Module;

// Java libraries to send mail
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class ReturnMail {
    public static void sendMail(String to, String capcha) {
        // Gmail SMTP server configuration

        String host = "smtp.gmail.com";
        int port = 465;
        String username = "lamvthe180779@fpt.edu.vn"; // Your Gmail address
        String password = "kpfbvbyrbeicgnsc";      // Your App Password (no spaces)

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true"); // Enable SSL
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set From field
            message.setFrom(new InternetAddress(username));

            // Set To field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set email subject and content
            message.setSubject("Verification Code");
            message.setText("Your verification code is: " + capcha);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    // Method to generate a 6-digit verification code
    public static String generateVerificationCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }

}
