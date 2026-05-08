package com.rbf.product.console.controller;

import com.rbf.product.console.client.ProductManagementClient;
import com.rbf.product.console.dto.product.CatalogOptionRequest;
import com.rbf.product.console.dto.product.ProductConsoleRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductConsoleController {

    private final ProductManagementClient productClient;

    public ProductConsoleController(ProductManagementClient productClient) {
        this.productClient = productClient;
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("pageTitle", "Products");
        return "product/products";
    }

    @GetMapping("/product-form")
    public String productForm(Model model) {
        model.addAttribute("pageTitle", "Product Form");
        return "product/product-form";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("pageTitle", "Categories");
        return "product/categories";
    }

    @GetMapping("/brands")
    public String brands(Model model) {
        model.addAttribute("pageTitle", "Brands");
        return "product/brands";
    }

    @GetMapping("/api/products")
    @ResponseBody
    public Map<String, Object> productsData(HttpSession session,
                                            @RequestParam(required = false) String search,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return productClient.products(session, search, page, size);
    }

    @GetMapping("/api/products/{id}")
    @ResponseBody
    public Map<String, Object> product(HttpSession session, @PathVariable Long id) {
        return productClient.getProduct(session, id);
    }

    @PostMapping("/api/products")
    @ResponseBody
    public Map<String, Object> createProduct(HttpSession session, @Valid @RequestBody ProductConsoleRequest request) {
        return productClient.createProduct(session, request);
    }

    @PutMapping("/api/products/{id}")
    @ResponseBody
    public Map<String, Object> updateProduct(HttpSession session,
                                             @PathVariable Long id,
                                             @Valid @RequestBody ProductConsoleRequest request) {
        return productClient.updateProduct(session, id, request);
    }

    @DeleteMapping("/api/products/{id}")
    @ResponseBody
    public void deactivateProduct(HttpSession session, @PathVariable Long id) {
        productClient.deactivateProduct(session, id);
    }

    @GetMapping("/api/categories")
    @ResponseBody
    public List<Map<String, Object>> categoriesData(HttpSession session) {
        return productClient.categories(session);
    }

    @PostMapping("/api/categories")
    @ResponseBody
    public Map<String, Object> createCategory(HttpSession session, @Valid @RequestBody CatalogOptionRequest request) {
        return productClient.createCategory(session, request);
    }

    @PutMapping("/api/categories/{id}")
    @ResponseBody
    public Map<String, Object> updateCategory(HttpSession session,
                                              @PathVariable Long id,
                                              @Valid @RequestBody CatalogOptionRequest request) {
        return productClient.updateCategory(session, id, request);
    }

    @DeleteMapping("/api/categories/{id}")
    @ResponseBody
    public void deactivateCategory(HttpSession session, @PathVariable Long id) {
        productClient.deactivateCategory(session, id);
    }

    @GetMapping("/api/brands")
    @ResponseBody
    public List<Map<String, Object>> brandsData(HttpSession session) {
        return productClient.brands(session);
    }

    @PostMapping("/api/brands")
    @ResponseBody
    public Map<String, Object> createBrand(HttpSession session, @Valid @RequestBody CatalogOptionRequest request) {
        return productClient.createBrand(session, request);
    }

    @PutMapping("/api/brands/{id}")
    @ResponseBody
    public Map<String, Object> updateBrand(HttpSession session,
                                           @PathVariable Long id,
                                           @Valid @RequestBody CatalogOptionRequest request) {
        return productClient.updateBrand(session, id, request);
    }

    @DeleteMapping("/api/brands/{id}")
    @ResponseBody
    public void deactivateBrand(HttpSession session, @PathVariable Long id) {
        productClient.deactivateBrand(session, id);
    }

    @GetMapping("/api/inventory/{productId}")
    @ResponseBody
    public Map<String, Object> stock(HttpSession session, @PathVariable Long productId) {
        return productClient.stock(session, productId);
    }

    @GetMapping("/api/tax-slabs")
    @ResponseBody
    public List<Map<String, Object>> taxSlabs(HttpSession session) {
        return productClient.taxSlabs(session);
    }
}
