package com.example.URL_Shortener.Service;

import com.example.URL_Shortener.Exception.ResourceNotFoundException;
import com.example.URL_Shortener.Exception.UserAlreadyExistsException;
import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with " + user.getEmail() + " Already Exists");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new ResourceNotFoundException("User Does not exist");
        }
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User Does not exist");
        }
        userRepository.deleteById(id);
    }


}
