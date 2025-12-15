package com.example.SpringCacheFlow.Entity;

import com.example.SpringCacheFlow.ENUM.Category;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private  String name;

    @Column
    private int  price;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

}
