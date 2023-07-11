package com.segolin.client.service;

import com.segolin.client.entity.User;
import com.segolin.client.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);
}
