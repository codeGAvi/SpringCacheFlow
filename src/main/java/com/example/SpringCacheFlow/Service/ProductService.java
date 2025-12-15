package com.example.SpringCacheFlow.Service;

import com.example.SpringCacheFlow.CacheLayer.CacheEntry;
import com.example.SpringCacheFlow.CacheLayer.CacheStore;


import com.example.SpringCacheFlow.Entity.Product;
import com.example.SpringCacheFlow.Exception.ProductNotFoundException;
import com.example.SpringCacheFlow.Repository.ProductRepository;
import com.example.SpringCacheFlow.dto.Request.ProductRequest;
import com.example.SpringCacheFlow.dto.Response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;



@Service
public class ProductService {

    @Autowired
    CacheStore cacheStore;

    @Autowired
    ProductRepository productRepository;


    public  ProductResponse addProduct(ProductRequest productRequest) {

        // dto to entity
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());

        // save product in database
        Product savedProduct = productRepository.save(product);

        // now save the product in cache also .....
        cacheStore.addCacheEntry(savedProduct.getId(), new CacheEntry(savedProduct,0));

        // entity to response
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(savedProduct.getName());
        productResponse.setPrice(savedProduct.getPrice());

        return productResponse;
    }





    public Product getProductsById(int id){
       // call cache with id(key) if present in cache then return
     CacheStore.Node cacheNode  = cacheStore.getEntry(id);
       if(cacheNode != null){  // if this will return null then below code will execute
           System.out.println("Cache hit for product_id: " + id);
           return cacheNode.getCacheEntry().getProduct();   // returns from in-memory Hashmap
       }
       System.out.println("Cache missed for product_id: " + id + " fetching from database....");

         // if not.  1. then fetch from DB
         Optional<Product> product =  productRepository.findById(id);
       if(product.isEmpty()){
           throw new ProductNotFoundException("product with this id: " + id + " not available");
       }
       Product products = product.get();

       // 2.  and add this(key,cacheEntry) into cache
       cacheStore.addCacheEntry(products.getId(),new CacheEntry(products,0));

       return products;  // and then simply return from DB;
   }
}
