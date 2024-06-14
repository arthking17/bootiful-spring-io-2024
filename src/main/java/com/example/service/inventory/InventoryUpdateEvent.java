package com.example.service.inventory;

public record InventoryUpdateEvent(
        int product, int quantity
) {
}
