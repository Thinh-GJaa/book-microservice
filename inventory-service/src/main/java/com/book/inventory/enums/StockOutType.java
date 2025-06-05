package com.book.inventory.enums;

public enum StockOutType {
    SALE("Bán hàng"),
    RETURN("Trả hàng cho nhà cung cấp"),
    DAMAGE("Hàng hỏng"),
    LOSS("Mất mát"),
    EXPIRED("Hết hạn"),
    OTHER("Khác");

    private final String description;

    StockOutType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
