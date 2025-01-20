package com.example.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
        private ProductRepository productRepository;
        public List<Product> findproductByName(String name){
            return productRepository.findByName( name);
        }
        
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
