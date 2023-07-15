package com.segolin.client.controller;

import com.segolin.client.entity.Employee;
import com.segolin.client.entity.VerificationToken;
import com.segolin.client.event.RegistrationCompleteEvent;
import com.segolin.client.model.PasswordModel;
import com.segolin.client.model.EmployeeModel;
import com.segolin.client.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ApplicationEventPublisher publisher;


    @PostMapping("/register")
    public String registerUser(@RequestBody EmployeeModel employeeModel, HttpServletRequest request) {
        Employee employee = employeeService.registerUser(employeeModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                employee,
                applicationUrl(request)
        ));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = employeeService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User verifies Successfully";
        }
        return "Bad User";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
