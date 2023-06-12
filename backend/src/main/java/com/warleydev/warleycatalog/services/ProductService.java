package com.warleydev.warleycatalog.services;

import com.warleydev.warleycatalog.dto.ProductDTO;
import com.warleydev.warleycatalog.entities.Category;
import com.warleydev.warleycatalog.entities.Product;
import com.warleydev.warleycatalog.repositories.ProductRepository;
import com.warleydev.warleycatalog.services.exceptions.DatabaseException;
import com.warleydev.warleycatalog.services.exceptions.ResourceNotFoundException;
import com.warleydev.warleycatalog.services.utils.ListUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryService categoryService;


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = repository.findAll(pageable);
        return list.map(product -> new ProductDTO(product, product.getCategories()));
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado!"));
    }

    @Transactional(readOnly = false)
    public ProductDTO insert(ProductDTO dto) {
        if (!ListUtils.listEmptyOrNull(dto.getIdCategories())) {
            Product entity = new Product(null, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getImgUrl(), dto.getDate());
            List<Category> categoryList = new ArrayList<>();

            dto.getIdCategories().forEach(id -> categoryList.add(categoryService.findById(id)));
            categoryList.forEach(cat -> entity.getCategories().add(cat));

            dto = new ProductDTO(repository.save(entity));
            return dto;
        } else {
            throw new ResourceNotFoundException("Um produto precisa ter ao menos 1 categoria!");
        }
    }

    @Transactional(readOnly = false)
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Produto " + id + " não encontrado!");
        }
    }

    public void delete(Long id) {
        try{
            if (repository.existsById(id)){
                repository.deleteById(id);
            }
            else throw new ResourceNotFoundException("Id "+id+" não encontrado!");
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Violação de integridade, este produto não pode ser deletado!");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());

        if (!ListUtils.listEmptyOrNull(dto.getIdCategories())) {
            entity.getCategories().clear();
            dto.getIdCategories().forEach(idCategory -> entity.getCategories().add(categoryService.findById(idCategory)));
        }

    }
}
