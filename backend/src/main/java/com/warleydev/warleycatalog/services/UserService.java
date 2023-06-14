package com.warleydev.warleycatalog.services;

import com.warleydev.warleycatalog.dto.RoleDTO;
import com.warleydev.warleycatalog.dto.UserDTO;
import com.warleydev.warleycatalog.dto.UserInsertDTO;
import com.warleydev.warleycatalog.entities.Role;
import com.warleydev.warleycatalog.entities.User;
import com.warleydev.warleycatalog.repositories.RoleRepository;
import com.warleydev.warleycatalog.repositories.UserRepository;
import com.warleydev.warleycatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> users = repository.findAll(pageable);
        return users.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        UserDTO userDto = new UserDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. Id: " + id)));
        return userDto;
    }

    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    public void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()) {
            Role role = roleRepository.findById(roleDto.getId()).get();
            entity.getRoles().add(role);
        }
    }
}

