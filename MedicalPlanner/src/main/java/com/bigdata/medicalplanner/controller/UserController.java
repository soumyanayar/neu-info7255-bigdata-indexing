package com.bigdata.medicalplanner.controller;
import com.bigdata.medicalplanner.entity.User;
import com.bigdata.medicalplanner.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value ="/login")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if(!userService.isValidEmailAddress(email)) {
            return new ResponseEntity<>("Invalid email address", HttpStatus.BAD_REQUEST);
        }

        if(!userService.doesUserExist(email)) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }

        JSONObject userObject = userService.getUser(email);
        if(!userObject.getString("password").equals(password))   {
            return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new JSONObject().put("message", "Login Successful").toString(), HttpStatus.OK);
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