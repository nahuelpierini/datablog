package com.app.datablog.service.Impl;

import com.app.datablog.dto.RoleDTO;
import com.app.datablog.dto.UserDTO;
import com.app.datablog.exceptions.ResourceNotFoundException;
import com.app.datablog.models.Role;
import com.app.datablog.models.User;
import com.app.datablog.repository.RoleRepository;
import com.app.datablog.repository.UserRepository;
import com.app.datablog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<UserDTO> listAllUsers(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

        Page<User> usersPage = userRepository.findAll(pageable);
        Page<UserDTO> usersDTO = usersPage.map(this::mapToDTO);

        return usersDTO;
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.map(this::mapToDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.map(this::mapToDTO);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO, Long roleId) {

        User newUser = mapToEntity(userDTO);
        newUser.setRegisteredAt(LocalDateTime.now());
        newUser.setActive(true);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role with ID: " + roleId + "not found"));
        newUser.setRole(role);
        User savedUser = userRepository.save(newUser);

        return mapToDTO(savedUser);

    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User newUser = mapToEntity(userDTO);
        newUser.setRegisteredAt(LocalDateTime.now());
        newUser.setActive(true);

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        newUser.setRole(role);
        User savedUser = userRepository.save(newUser);

        return mapToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO, Long newRoleId) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with ID: " + id + "not found"));

        if (existingUser.getRole() != null){
            Role newRole = roleRepository.findById(newRoleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role with ID: " + newRoleId + "not found"));
            existingUser.setRole(newRole);
        }


        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User updatedUser = userRepository.save(existingUser);

        return mapToDTO(updatedUser);

    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }

        userRepository.deleteById(id);

    }

    private User mapToEntity(UserDTO userDTO){
        User user = new User();

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return user;
    }

    private UserDTO mapToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRegisteredAt(user.getRegisteredAt());
        userDTO.setActive(user.isActive());

        if (user.getRole() != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(user.getRole().getId());
            roleDTO.setName(user.getRole().getName());
            userDTO.setRole(roleDTO);
        }

        return userDTO;
    }
}
