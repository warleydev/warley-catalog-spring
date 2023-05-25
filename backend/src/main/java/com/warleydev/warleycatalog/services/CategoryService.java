package com.warleydev.warleycatalog.services;

import com.warleydev.warleycatalog.dto.CategoryDTO;
import com.warleydev.warleycatalog.entities.Category;
import com.warleydev.warleycatalog.repositories.CategoryRepository;
import com.warleydev.warleycatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> list = repository.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Category cat = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        return new CategoryDTO(cat);
    }

    @Transactional(readOnly = false)
    public CategoryDTO insert(CategoryDTO dto){
        Category entity = new Category(null, dto.getName());
        dto = new CategoryDTO(repository.save(entity));
        return dto;
    }

    @Transactional(readOnly = false)
    public CategoryDTO update(Long id, CategoryDTO dto){
        try{
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id "+id+" não encontrado!");
        }

    }
}
