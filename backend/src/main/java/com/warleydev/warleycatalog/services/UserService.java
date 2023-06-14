package com.warleydev.warleycatalog.services;

import com.warleydev.warleycatalog.dto.UserDTO;
import com.warleydev.warleycatalog.entities.User;
import com.warleydev.warleycatalog.repositories.UserRepository;
import com.warleydev.warleycatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> users = repository.findAll(pageable);
        return users.map( x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        UserDTO userDto =  new UserDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. Id: "+ id)));
        return userDto;
    }

}
