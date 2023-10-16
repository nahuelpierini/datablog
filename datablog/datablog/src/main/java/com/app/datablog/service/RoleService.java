package com.app.datablog.service;

import com.app.datablog.dto.RoleDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RoleService {
    Page<RoleDTO> listAllRoles(int page, int size, String sortedBy, String direction);
    Optional<RoleDTO> getRoleById(Long id);
    RoleDTO createRole(RoleDTO roleBDTO);
    RoleDTO updateRole(Long id, RoleDTO roleBDTO);
    void deleteRole(Long id);
}

