package com.warleydev.warleycatalog.services;

import com.warleydev.warleycatalog.dto.CategoryDTO;
import com.warleydev.warleycatalog.dto.ProductDTO;
import com.warleydev.warleycatalog.entities.Category;
import com.warleydev.warleycatalog.repositories.CategoryRepository;
import com.warleydev.warleycatalog.services.exceptions.DatabaseException;
import com.warleydev.warleycatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category> list = repository.findAll(pageRequest);
        return list.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public Category findById(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
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

    public void delete(Long id){
        try{
            if (repository.existsById(id)){
                repository.deleteById(id);
            }
            else{
                throw new ResourceNotFoundException("Id " + id + "não encontrado!");
            }
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Violação de integridade, esta categoria não pode ser deletada!");
        }
    }

}
