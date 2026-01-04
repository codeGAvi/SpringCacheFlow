package com.example.SpringCacheFlow.dto.Request;

import com.example.SpringCacheFlow.ENUM.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private  String name;
    private int price;
    private Category category;
}
