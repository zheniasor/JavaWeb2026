package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.service.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LogManager.getLogger(MailServiceImpl.class);

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "zheniasor@gmail.com";
    private static final String SMTP_PASSWORD = "dmauhftsshqyqvbx";
    private static final String FROM_EMAIL = "noreply@yourapp.com";
    private static final String APP_URL = "http://100.68.15.21:8080/demo";

    private static MailService instance;

    private MailServiceImpl() {}

    public static MailService getInstance() {
        if (instance == null) {
            instance = new MailServiceImpl();
        }
        return instance;
    }

    @Override
    public void sendConfirmationEmail(User user) {
        String subject = "Подтверждение регистрации";
        String confirmationLink = APP_URL + "/controller?command=CONFIRM&token=" + user.getConfirmationToken();

        String htmlContent = """
            <html>
            <body>
                <h2>Добро пожаловать, %s!</h2>
                <p>Для завершения регистрации перейдите по ссылке:</p>
                <a href="%s">%s</a>
                <p>Если вы не регистрировались, проигнорируйте это письмо.</p>
                <p>С уважением,<br>Команда поддержки</p>
            </body>
            </html>
            """.formatted(user.getLogin(), confirmationLink, confirmationLink);

        sendEmail(user.getEmail(), subject, htmlContent);
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetToken) {
        String subject = "Восстановление пароля";
        String resetLink = APP_URL + "/controller?command=RESET_PASSWORD&token=" + resetToken;

        String htmlContent = """
            <html>
            <body>
                <h2>Восстановление пароля</h2>
                <p>Вы запросили восстановление пароля. Перейдите по ссылке:</p>
                <a href="%s">%s</a>
                <p>Если вы не запрашивали восстановление, проигнорируйте это письмо.</p>
                <p>С уважением,<br>Команда поддержки</p>
            </body>
            </html>
            """.formatted(resetLink, resetLink);

        sendEmail(email, subject, htmlContent);
    }

    @Override
    public void sendNotification(String to, String subject, String content) {
        sendEmail(to, subject, content);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            LOGGER.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email to: {}", to, e);
        }
    }
}