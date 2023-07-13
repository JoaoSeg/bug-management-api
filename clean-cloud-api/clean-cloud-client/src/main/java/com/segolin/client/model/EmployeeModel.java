package com.segolin.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String matchingPassword;
}
