package com.substring.auth.auth_app_backend.services;

import com.substring.auth.auth_app_backend.dtos.UserDto;

public interface UserService {

    //void registerUser(String email, String password);

    UserDto createUser(UserDto userDto);

    UserDto getUserByEmailId(String email);

    UserDto updateUser(UserDto userDto, String userId);

    void deleteUser(String userId);

    UserDto getUserById(String userId);

    Iterable<UserDto> getAllUsers();        //Iterable<T> is a Java interface that represents a collection of objects that can be iterated (looped).
                                        //Enhanced for-loop (for-each) . for (UserDto user : getAllUsers()) {
    //                                          System.out.println(user);}


}
