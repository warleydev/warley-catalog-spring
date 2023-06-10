package com.warleydev.warleycatalog.tests;

import com.warleydev.warleycatalog.dto.ProductDTO;
import com.warleydev.warleycatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        return new Product(1L, "Celular","Celular maneiro",1250.0,"https://warley.com", Instant.now());
    }

    public static ProductDTO createProductDTO(){
        return new ProductDTO(createProduct());
    }
}
