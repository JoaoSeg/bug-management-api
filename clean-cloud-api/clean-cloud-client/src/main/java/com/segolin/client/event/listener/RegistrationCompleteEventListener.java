package com.segolin.client.event.listener;

import com.segolin.client.entity.Employee;
import com.segolin.client.event.RegistrationCompleteEvent;
import com.segolin.client.service.EmployeeService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmRegistration(RegistrationCompleteEvent event) throws MessagingException, UnsupportedEncodingException {
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        Employee employee = event.getEmployee();
        String token = UUID.randomUUID().toString();
        employeeService.saveVerificationTokenForUser(token, employee);

        String confirmationUrl
                = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("joaosegolin@gmail.com","Joao");
        helper.setTo("joaosegolin@gmail.com");
        helper.setSubject("Please verify your information");
        content = content.replace("[[name]]", employee.getFirstName()).replace("[[URL]]", confirmationUrl);

        helper.setText(content, true);

        mailSender.send(message);


    }
}
