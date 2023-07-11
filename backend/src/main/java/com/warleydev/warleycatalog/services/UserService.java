package com.warleydev.warleycatalog.services;

import com.warleydev.warleycatalog.dto.RoleDTO;
import com.warleydev.warleycatalog.dto.UserDTO;
import com.warleydev.warleycatalog.dto.UserInsertDTO;
import com.warleydev.warleycatalog.dto.UserUpdateDTO;
import com.warleydev.warleycatalog.entities.Role;
import com.warleydev.warleycatalog.entities.User;
import com.warleydev.warleycatalog.repositories.RoleRepository;
import com.warleydev.warleycatalog.repositories.UserRepository;
import com.warleydev.warleycatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = repository.findById(id).get();
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Usuário não encontrado. Id: " + id);
        }
    }

    public void delete(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else throw new ResourceNotFoundException("Usuário não encontrado. Id: " + id);

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null){
            logger.error("Usuário não encontrado: "  + username);
            throw new UsernameNotFoundException("Email não encontrado!");
        }
        logger.info("Usuário encontrado: " + username);
        return user;
    }
}
