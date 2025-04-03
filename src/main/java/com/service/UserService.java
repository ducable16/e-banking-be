package com.service;

import com.entity.User;
import com.enums.Role;
import com.repository.UserRepository;
import com.request.SignUpOTPRequest;
import com.request.UpdateInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    public Optional<User> findById(Integer userId) {return userRepository.findById(userId);}

    public Optional<User> findByEmail(String email) {return userRepository.findByEmail(email);}

    public Optional<User> findByUsername(String username) {return userRepository.findByUsername(username);}

    public void createUser(SignUpOTPRequest request) {
        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail(), request.getFirstName(), request.getLastName(), request.getPhoneNumber(), request.getAddress(), request.getRole());
        userRepository.save(user);
    }

    public boolean login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if(password.equals(user.get().getPassword())) {
                return true;
            }
            else return false;
        }
        else return false;
    }

    public Optional<User> getInfo(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findByUsername(username);
    }


    public List<User> getUserByRole(Role role) {
        return userRepository.findUsersByRole(role);
    }

    public User setRole(Integer userId, Role role) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(role);
            userRepository.save(user);
            return user;
        }
        return null;
    }
}
