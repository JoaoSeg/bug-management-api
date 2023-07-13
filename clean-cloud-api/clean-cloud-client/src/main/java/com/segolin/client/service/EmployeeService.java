package com.segolin.client.service;

import com.segolin.client.entity.Employee;
import com.segolin.client.entity.VerificationToken;
import com.segolin.client.model.EmployeeModel;

import java.util.Optional;

public interface EmployeeService {
    Employee registerUser(EmployeeModel employeeModel);

    void saveVerificationTokenForUser(String token, Employee employee);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    Employee findUserByEmail(String email);

    void createPasswordResetTokenForUser(Employee employee, String token);

    String validatePasswordResetToken(String token);

    Optional<Employee> getUserByPasswordResetToken(String token);

    void changePassword(Employee employee, String newPassword);

    boolean checkIfValidOldPassword(Employee employee, String oldPassword);
}
