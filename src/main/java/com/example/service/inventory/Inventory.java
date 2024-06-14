package com.example.service.inventory;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
class Inventory {

    @ApplicationModuleListener
    void on(InventoryUpdateEvent inventoryUpdateEvent) throws InterruptedException {
        Thread.sleep(10_000);
        System.out.println("The inventory has been updated.");
    }
}
