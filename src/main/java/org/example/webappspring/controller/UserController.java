package org.example.webappspring.controller;

import org.example.webappspring.model.Product;
import org.example.webappspring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ProductRepository productRepository;

    // Метод для получения списка продуктов и отображения на странице
    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);  // Добавляем продукты в модель
        return "products";  // Возвращаем имя шаблона (например, products.html)
    }
}
