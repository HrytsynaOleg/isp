package service;

public interface IEmailService {
    void sendEmail(String mailTo, String subject, String body);
}
