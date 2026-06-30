package com.greet.service;

import com.greet.model.User;
import com.greet.repository.UserRepository;
import com.greet.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String username,
                      String password) {

        User user =
                userRepository.findByUsername(username);

        if (user == null) {
            return null;
        }

        String hashedPassword =
                HashUtil.hashPassword(password);

        if (hashedPassword.equals(user.getPassword())) {
            return user;
        }

        return null;
    }

    @Override
    public boolean register(User user) {

        User existing =
                userRepository.findByUsername(
                        user.getUsername()
                );

        if (existing != null) {
            return false;
        }

        user.setPassword(
                HashUtil.hashPassword(
                        user.getPassword()
                )
        );

        return userRepository.save(user);
    }
}
