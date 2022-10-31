package com.bigdata.medicalplanner.controller;
import com.bigdata.medicalplanner.entity.JwtRequest;
import com.bigdata.medicalplanner.entity.User;
import com.bigdata.medicalplanner.service.UserService;
import com.bigdata.medicalplanner.util.JwtUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, JwtUtility jwtUtility, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value ="/login")
    public ResponseEntity<Object> loginUser(@RequestBody JwtRequest jwtRequest) {
        String email = jwtRequest.getEmail();
        String password = jwtRequest.getPassword();

        if(!userService.isValidEmailAddress(email)) {
            return new ResponseEntity<>("Invalid email address", HttpStatus.BAD_REQUEST);
        }

        if(!userService.doesUserExist(email)) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }

       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(email, password)
           );
       } catch (Exception e) {
           return new ResponseEntity<>("Invalid password", HttpStatus.BAD_REQUEST);
       }

       final UserDetails userDetails = userService.loadUserByUsername(email);
       final String token = jwtUtility.generateToken(userDetails);

       JSONObject json = new JSONObject();
       json.put("token", token);
       json.put("email", email);
       return new ResponseEntity<>(json.toString(), HttpStatus.OK);
    }

    @PostMapping(value ="/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        if(user == null || email == null || password == null || firstName == null || lastName == null) {
            return new ResponseEntity<Object>("{\"message\": \"Invalid Payload. Kindly provide the email, password, firstname and lastname for the user\" }", HttpStatus.BAD_REQUEST);
        }

        if(!userService.isValidEmailAddress(email)) {
            return new ResponseEntity<Object>("{\"message\": \"Invalid email address\" }", HttpStatus.BAD_REQUEST);
        }

        if(userService.doesUserExist(email)) {
            return new ResponseEntity<Object>("{\"message\": \"User already exists with the email: " + email + "\" }", HttpStatus.CONFLICT);
        }

        userService.registerUser(user);
        return ResponseEntity.ok().body(" {\"message\": \"User : " + email + " is registered successfully\" }");
    }
}