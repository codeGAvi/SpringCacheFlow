package com.example.SpringCacheFlow.CacheLayer;

import com.example.SpringCacheFlow.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CacheEntry {

  private Product product;
  private long expiryTime;  // not storing creation time , jb entry add krnege usi time calculate kr lenge
                         // expiryTime = System.currentTimeMillis()+TTL   .--> currentTime + TTL at insertion/add

  // expiry check function
    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
