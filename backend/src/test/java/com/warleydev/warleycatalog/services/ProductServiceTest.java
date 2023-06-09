package com.warleydev.warleycatalog.services;

import com.warleydev.warleycatalog.dto.ProductDTO;
import com.warleydev.warleycatalog.entities.Product;
import com.warleydev.warleycatalog.repositories.ProductRepository;
import com.warleydev.warleycatalog.services.exceptions.ResourceNotFoundException;
import com.warleydev.warleycatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {


    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    Product product;
    ProductDTO dto;


    Long existingId;
    Long nonExistingId;

    @BeforeEach
    void setUp(){
        product = Factory.createProduct();
        existingId = 1L;
        nonExistingId = 30L;

    }


    @Test
    void findByIdShouldReturnWithSuccessWhenIdExists(){
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));

        Product p = service.findById(existingId);
        assertEquals(product, p);
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test

    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        when(productRepository.findById(nonExistingId)).thenThrow(new ResourceNotFoundException("Produto não encontrado, Id: "+ nonExistingId));
        final ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
           service.findById(nonExistingId);
        });

        assertEquals("Produto não encontrado, Id: "+nonExistingId, e.getMessage());
        assertEquals(ResourceNotFoundException.class, e.getClass());
        verify(productRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists(){
        doNothing().when(productRepository).deleteById(existingId);
        when(productRepository.existsById(existingId)).thenReturn(true);
        service.delete(existingId);
        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        when(productRepository.existsById(nonExistingId)).thenThrow(new ResourceNotFoundException("Id inexistente!"));
        try {
            service.delete(nonExistingId);
        }
        catch (ResourceNotFoundException e){
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Id inexistente!", e.getMessage());
            verify(productRepository, times(0)).deleteById(existingId);
        }
    }
}
