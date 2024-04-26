package com.qrebl.users.controller;

import com.qrebl.users.DTO.UserDTO;
import com.qrebl.users.service.KeyCloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(path = "api/user")
public class KeyCloakController {

    @Autowired
    KeyCloakService service;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(service.createUser(userDTO));
    }

    @GetMapping(path = "/{userName}")
    public ResponseEntity<?> getUser(@PathVariable("userName") String userName) {
        List<UserRepresentation> user = service.getUser(userName);
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/createRole")
    public ResponseEntity<?> createRole(String rolename) {
        service.createRole(rolename);
        return ResponseEntity.ok("Create Role Successfully.");
    }

    @PostMapping(path = "/addRoleToUser")
    public ResponseEntity<?> addRoleToUser(String username, String rolename) {
        service.addRoleToUser(username, rolename);
        return ResponseEntity.ok("Add Role To User Successfully.");
    }

    @DeleteMapping(path = "/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId) {
        service.deleteUser(userId);
        return ResponseEntity.ok("User Deleted Successfully.");
    }

    @GetMapping(path = "/setVerificationEmail")
    public ResponseEntity<?> setVerificationEmail(String userId, Boolean verificationStatus) {
        service.setVerificationEmail(userId, verificationStatus);
        return ResponseEntity.ok("Verification Link Send to Registered E-mail Id.");
    }

    @GetMapping(path = "/setUserEnabled")
    public ResponseEntity<?> setUserEnabled(String userId, Boolean enableStatus) {
        service.setUserEnabled(userId, enableStatus);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(path = "/resetPassword/{userId}/{password}")
    public ResponseEntity<?> resetPassword(@PathVariable("userId") String userId,
            @PathVariable("password") String password) {
        service.resetPassword(userId, password);
        return ResponseEntity.ok("Reset Password Successfully");
    }
}
