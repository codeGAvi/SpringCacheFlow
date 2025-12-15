package com.example.SpringCacheFlow.Controller;

import com.example.SpringCacheFlow.Entity.Product;
import com.example.SpringCacheFlow.Service.ProductService;
import com.example.SpringCacheFlow.dto.Request.ProductRequest;
import com.example.SpringCacheFlow.dto.Response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;


    @GetMapping("/{id}")
    public ResponseEntity getProductsById(@PathVariable int id){
        try {
            Product product = productService.getProductsById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity addProduct(@RequestBody ProductRequest productRequest){
         ProductResponse productResponse = productService.addProduct(productRequest);
         return  new ResponseEntity<>(productResponse,HttpStatus.CREATED);
    }
}
