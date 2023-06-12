package com.warleydev.warleycatalog.repositories;

import com.warleydev.warleycatalog.entities.Product;
import com.warleydev.warleycatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class ProductRepositoryTests {

    private long countTotalProducts;
    private long idExisting;
    private long idNonExisting;

    @BeforeEach
    void setUp() throws Exception{
        countTotalProducts = 25L;
        idExisting = 1L;
        idNonExisting = 250L;
    }

    @Autowired
    private ProductRepository repository;

    @Test
    void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(idExisting);
        Optional<Product> p = repository.findById(idExisting);
        Assertions.assertFalse(p.isPresent());
        System.out.println("TESTE 1 FINALIZADO");
    }

    @Test
    void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    void findByIdShouldReturnObjectWhenIdExists(){
        Optional<Product> p = repository.findById(idExisting);

        Assertions.assertTrue(p.isPresent());
    }

    @Test
    void findByIdShouldReturnObjectEmptyWhenIdNonExists(){
        Optional<Product> p = repository.findById(idNonExisting);

        Assertions.assertFalse(p.isPresent());
    }




}
