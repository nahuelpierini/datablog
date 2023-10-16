package com.app.datablog.service;

import com.app.datablog.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    Page<UserDTO> listAllUsers(int page, int size, String sortBy, String direction);
    Optional<UserDTO> getUserById(Long id);
    Optional<UserDTO> getUserByEmail(String email);
    UserDTO createUser(UserDTO userDTO, Long roleId);
    UserDTO registerUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO,Long newRoleId);
    void deleteUser(Long id);

}
