package com.app.datablog.service.Impl;

import com.app.datablog.dto.RoleDTO;
import com.app.datablog.exceptions.ResourceNotFoundException;
import com.app.datablog.models.Role;
import com.app.datablog.models.User;
import com.app.datablog.repository.RoleRepository;
import com.app.datablog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<RoleDTO> listAllRoles(int page, int size, String sortBy, String direction) {

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

        Page<Role> rolesPage = roleRepository.findAll(pageable);
        Page<RoleDTO> roleDTOs = rolesPage.map(this::mapToDTO);

        return roleDTOs;
    }

    @Override
    public Optional<RoleDTO> getRoleById(Long id) {

        Optional<Role> roleOptional = roleRepository.findById(id);

        return roleOptional.map(this::mapToDTO);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = mapToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);

        return mapToDTO(savedRole);
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {

        Role existingRole = roleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Role with ID: " + id + " not found"));

        existingRole.setName(roleDTO.getName());
        existingRole.setDescription(roleDTO.getDescription());

        Role updatedRole = roleRepository.save(existingRole);

        return mapToDTO(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);

        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Role with ID " + id + " not found");
        }

        Role role = roleOptional.get();

        for (User user : role.getUsers()) {
            user.setRole(null); // Elimina la asociación del rol al usuario
        }

        roleRepository.deleteById(id);
    }

    private Role mapToEntity(RoleDTO roleDTO){
        Role role = new Role();

        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());

        return role;
    }

    private RoleDTO mapToDTO(Role role){
        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());

        return roleDTO;
    }

}

