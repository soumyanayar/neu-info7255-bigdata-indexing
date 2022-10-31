package com.bigdata.medicalplanner.controller;

import com.bigdata.medicalplanner.models.AuthenticationRequest;
import com.bigdata.medicalplanner.models.AuthenticationResponse;
import com.bigdata.medicalplanner.models.User;
import com.bigdata.medicalplanner.service.UserServiceImpl;
import com.bigdata.medicalplanner.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final UserServiceImpl userDetailsService;

    @Autowired
    public UsersController(AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil, UserServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping(value ="/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        if (username == null || password == null || firstName == null || lastName == null) {
            return new ResponseEntity<Object>("{\"message\": \"Invalid Payload. Kindly provide the email, password, firstname and lastname for the user\" }", HttpStatus.BAD_REQUEST);
        }

        if (userDetailsService.isUserExist(username)) {
            return new ResponseEntity<Object>("{\"message\": \"User already exists with the username: " + username + "\" }", HttpStatus.CONFLICT);
        }

        user.setActive(true);
        user.setRoles("USER");

        userDetailsService.registerUser(user);
        return ResponseEntity.ok().body(" {\"message\": \"User : " + username + " is registered successfully\" }");
    }
}
