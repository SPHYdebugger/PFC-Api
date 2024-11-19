package com.sphy.PFC_Api.service;



import com.sphy.PFC_Api.model.User;

import com.sphy.PFC_Api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User save(User user) {
        return  userRepository.save(user);
    }


}