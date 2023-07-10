package com.segolin.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    //TODO Create logic to check if passwords match
    private String matchingPassword;
}
