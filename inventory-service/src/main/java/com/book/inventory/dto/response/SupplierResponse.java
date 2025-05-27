package com.book.inventory.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierResponse implements Serializable {
    String supplierId;
    String nameSupplier;
    String contactName;
    String phoneNumber;
    String email;
    String address;
    String description;
}
