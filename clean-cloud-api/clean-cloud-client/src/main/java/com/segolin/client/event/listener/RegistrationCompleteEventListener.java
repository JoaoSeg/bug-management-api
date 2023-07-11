package com.segolin.client.event.listener;

import com.segolin.client.entity.User;
import com.segolin.client.event.RegistrationCompleteEvent;
import com.segolin.client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //TODO Create the Verification Token for the User with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        //TODO Send mail to user
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;
        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}", url);
    }
}
