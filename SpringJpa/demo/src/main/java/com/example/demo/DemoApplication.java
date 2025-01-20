package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Tạo một sản phẩm mới và lưu vào cơ sở dữ liệu
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(100);
        productService.saveProduct(product);

        // Tìm kiếm sản phẩm theo tên
        List<Product> products = productService.findproductByName("Product A");
        for (Product p : products) {
            System.out.println(p.getName() + ": " + p.getPrice());
        }
    }
}
