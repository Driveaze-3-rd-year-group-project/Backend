package com.driveaze.driveaze.dto;

public class InventoryDTO {
    private int itemId;
    private String name;
    private int count;
    private int initialCount;

    public InventoryDTO() {
    }

    public InventoryDTO(int itemId, String name, int count, int intialCount) {
        this.itemId = itemId;
        this.name = name;
        this.count = count;
        this.initialCount = intialCount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(int initialCount) {
        this.initialCount = initialCount;
    }

    @Override
    public String toString() {
        return "InventoryDTO{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", initialCount=" + initialCount +
                '}';
    }
}
