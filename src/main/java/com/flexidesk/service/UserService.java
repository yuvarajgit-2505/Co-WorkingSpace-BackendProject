package com.flexidesk.service;

import com.flexidesk.entity.User;

public interface UserService {
    User registerUser(User user);
    User getUserByEmail(String email);

}
