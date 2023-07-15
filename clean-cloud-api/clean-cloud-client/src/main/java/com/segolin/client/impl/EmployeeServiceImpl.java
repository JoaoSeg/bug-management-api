package com.segolin.client.impl;

import com.segolin.client.entity.PasswordResetToken;
import com.segolin.client.entity.Employee;
import com.segolin.client.entity.VerificationToken;
import com.segolin.client.model.EmployeeModel;
import com.segolin.client.repository.PasswordResetTokenRepository;
import com.segolin.client.repository.EmployeeRepository;
import com.segolin.client.repository.VerificationTokenRepository;
import com.segolin.client.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Employee registerUser(EmployeeModel employeeModel) {
        Employee employee = new Employee();
        employee.setEmail(employeeModel.getEmail());
        employee.setFirstName(employeeModel.getFirstName());
        employee.setLastName(employeeModel.getLastName());
        employee.setRole(employeeModel.getRole());
        employee.setPassword(passwordEncoder.encode(employeeModel.getPassword()));

        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public void saveVerificationTokenForUser(String token, Employee employee) {
        VerificationToken verificationToken = new VerificationToken(employee, token);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid";
        }

        Employee employee = verificationToken.getEmployee();
        Calendar calendar = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0))  {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        employee.setEnabled(true);
        employeeRepository.save(employee);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public Employee findUserByEmail(String email) throws UsernameNotFoundException {
        if (employeeRepository.findByEmail(email) == null) {
            throw new UsernameNotFoundException("Email not found");
        }

        return employeeRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(Employee employee, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(employee, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            return "invalid";
        }

        Employee employee = passwordResetToken.getEmployee();
        Calendar calendar = Calendar.getInstance();

        if ((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0))  {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<Employee> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getEmployee());
    }

    @Override
    public void changePassword(Employee employee, String newPassword) {
        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }

    @Override
    public boolean checkIfValidOldPassword(Employee employee, String oldPassword) {
        return passwordEncoder.matches(oldPassword, employee.getPassword());
    }

}
