package com.substring.auth.auth_app_backend.services;

import com.substring.auth.auth_app_backend.dtos.UserDto;
import com.substring.auth.auth_app_backend.exceptions.ResourceNotFoundException;
import com.substring.auth.auth_app_backend.helper.UserHelper;
import com.substring.auth.auth_app_backend.models.Provider;
import com.substring.auth.auth_app_backend.models.User;
import com.substring.auth.auth_app_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if(userDto.getEmail() == null || userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        //convert dto into entity object
        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);

        //role assign here to user ---for authorization
        //TODO
        User savedUser  = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmailId(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return modelMapper.map(user, UserDto.class);            //ModelMapper internally does something like:  UserDto dto = new UserDto(); But since Java uses type erasure, it cannot automatically infer the return type at runtime.So we must explicitly tell it: DestinationType.class
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uid = UserHelper.parseUUID(userId);
        User existingUser = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        //we are not going to change email id as we have kept it as unique
        if(userDto.getName() != null) existingUser.setName(userDto.getName());
        if(userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        if(userDto.getImage() != null) existingUser.setImage(userDto.getImage());
//        TODO: change password updation logic
        if(userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());

        existingUser.setEnable(userDto.isEnable());
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uid = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uid = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map( user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
