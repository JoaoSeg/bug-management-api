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
    public String registerUser(@RequestBody EmployeeModel employeeModel, final HttpServletRequest request) {
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

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = employeeService.generateNewVerificationToken(oldToken);
        Employee employee = verificationToken.getEmployee();
        resendVerificationToken(employee, applicationUrl(request), verificationToken);
        return "Verification link sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        Employee employee = employeeService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (employee != null) {
            String token = UUID.randomUUID().toString();
            employeeService.createPasswordResetTokenForUser(employee, token);
            url = passwordResetTokenEmail(employee, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
        String result = employeeService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid Token";
        }

        Optional<Employee> user = employeeService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            employeeService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password Reset Successfully";
        } else {
            return "Invalid Token";
        }

    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        Employee employee = employeeService.findUserByEmail(passwordModel.getEmail());
        if (!employeeService.checkIfValidOldPassword(employee, passwordModel.getOldPassword())) {
            return "Invalid old password";
        }

        employeeService.changePassword(employee, passwordModel.getNewPassword());
        return "Password changed Successfully";
    }

    private String passwordResetTokenEmail(Employee employee, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("Click the link to reset your password: {}", url);
        return url;
    }

    private void resendVerificationToken(Employee employee, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/resendVerifyToken?token=" + verificationToken.getToken();
        log.info("Click the link to verify your account: {}", url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
