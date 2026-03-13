package com.exam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repo.RoleRepository;
import com.exam.repo.UserRepository;
import com.exam.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class userServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
        User local = this.userRepository.findByUsername(user.getUsername());
        if(local != null){
            System.out.println("User is already there !!");
            throw new Exception("User already present !!");
        }else{
            //encode password
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            //creating user
            for(UserRole ur: userRoles){
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            local = this.userRepository.save(user);
        }
        return local;
    }

    //getting user by username
    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(User user) {
        if(this.userRepository.existsById(user.getId())) {
            User existingUser = this.userRepository.findById(user.getId()).orElse(null);
            if(user.getUsername() != null) {
                existingUser.setUsername(user.getUsername());
            } else {
                throw new RuntimeException("Username cannot be null for update.");
            }

            if(user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            } else {
                throw new RuntimeException("Password cannot be null for update.");
            }

            if(user.getFirstName() != null) {
                existingUser.setFirstName(user.getFirstName());
            }
            
            if(user.getLastName() != null) {
                existingUser.setLastName(user.getLastName());
            }

            if(user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if(user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }

            if(user.getProfile() != null) {
                existingUser.setProfile(user.getProfile());
            }
            return this.userRepository.save(existingUser);
        }
        else {
            throw new RuntimeException("User not found with id: " + user.getId());
        }
    }
}
