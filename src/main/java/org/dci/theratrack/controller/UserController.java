package org.dci.theratrack.controller;

import org.dci.theratrack.entity.User;
import org.dci.theratrack.enums.UserRole;
import org.dci.theratrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(("/assign-role/{id}"))
    public ResponseEntity<User> createTherapist(@PathVariable Long id, @RequestParam UserRole role) {
        User user = userService.assignRoleToUser(id, role);
        return ResponseEntity.ok(user);
    }
}
