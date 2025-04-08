//package com.service;
//
//import com.entity.User;
//import com.enums.Role;
//import com.exception.EntityNotFoundException;
//import com.repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@AllArgsConstructor
//@Service
//public class UserService {
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final JwtService jwtService;
//
//    public User findById(Integer userId) {return userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException());}
//
//    public User findByEmail(String email) {return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException());}
//
//
//    public List<User> getUserByRole(Role role) {
//        return userRepository.findUsersByRole(role);
//    }
//
//    public User setRole(Integer userId, Role role) {
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if(optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            user.setRole(role);
//            userRepository.save(user);
//            return user;
//        }
//        return null;
//    }
//}
