package com.book.inventory.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckInventoryRequest {

    String address;

    @Schema(description = "List of items to check inventory")
    @Valid
    @NotEmpty(message = "Items cannot be empty")
    List<Item> items;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item {

        @NotBlank(message = "Product ID cannot be blank")
        String productId;

        @NotNull(message = "Quantity cannot be null")
                @Min(value = 0, message = "Quantity must be at least 0")
        int quantity;
    }


}
