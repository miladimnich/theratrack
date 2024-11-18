package org.dci.theratrack.service;

import org.dci.theratrack.entity.User;
import org.dci.theratrack.enums.UserRole;
import org.dci.theratrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user, UserRole role) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseGet(() -> {
                    // Create a new user if it doesn't exist
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setUserRole(role); // Assign role as Therapist
                    return userRepository.save(user);
                });
        return existingUser;

    }
}
