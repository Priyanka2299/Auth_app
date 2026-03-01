package com.substring.auth.auth_app_backend.controllers;

import com.substring.auth.auth_app_backend.dtos.UserDto;
import com.substring.auth.auth_app_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")            //client will through request in json format and Springboot framework will convert that in Java Object
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
//        return ResponseEntity.ok(userService.createUser(userDto));  //This is give ok status to the client
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }
    //get all user api
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    //get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserbyEmail(@PathVariable ("email") String email){            // the name of {email} in @GetMapping("/email/{email}")should be same in that of email in (@PathVariable String email) else we need to expicitly write the {email} in @PathVaribale like this "email" -> (@PathVariable ("email") String email)
        return ResponseEntity.ok(userService.getUserByEmailId(email));
    }


}

