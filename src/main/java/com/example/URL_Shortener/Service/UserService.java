package com.example.URL_Shortener.Service;

import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) throws IllegalArgumentException {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("User Already Exists");
        }

        // TODO: Hash password using BCryptPasswordEncoder before saving
        return userRepository.save(user);
    }

    public  User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public  User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public  User updateUser(User user) throws Exception{
        if(!userRepository.existsById(user.getId())){
            throw new IllegalArgumentException("User Does not exist");
        }
        return userRepository.save(user);
    }

    public String deleteUserById(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
        return "User Deleted Successfully";
    }


}
