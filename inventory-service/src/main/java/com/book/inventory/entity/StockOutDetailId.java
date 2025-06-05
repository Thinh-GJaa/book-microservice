package com.book.inventory.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StockOutDetailId implements Serializable {
     private String stockOutId;
     private String productId;
}
