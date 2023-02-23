package service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IAwsSecretService;
import service.IEmailService;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import static settings.properties.AppPropertiesManager.getProperty;

public class EmailService implements IEmailService {
    IAwsSecretService secretService = new AwsSecretService();
    Logger logger= LogManager.getLogger(EmailService.class);

    @Override
    public void sendEmail(String mailTo, String subject, String body) {
        String sender = secretService.getSecretByKey(getProperty("mail.user"));
        String password = secretService.getSecretByKey(getProperty("mail.password"));
        Properties props = new Properties();
        props.put("mail.transport.protocol", getProperty("mail.transport.protocol"));
        props.put("mail.smtp.auth", getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", getProperty("mail.smtp.port"));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        MimeMessage message = new MimeMessage(session);

        Thread newThread = new Thread(() -> {
            try {
                message.setFrom(new InternetAddress(sender));
                message.setSubject(subject);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                mimeBodyPart.setContent(body, "text/html; charset=utf-8");
                multipart.addBodyPart(mimeBodyPart);
                message.setContent(multipart);
                Transport.send(message);
            } catch (MessagingException e) {
                logger.error(e.getMessage());
            }
        });

        newThread.start();

    }
}
