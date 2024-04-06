package com.example.carreservation.models;

public class ListItem {
    private String itemName;
    private int iconResId;

    public ListItem(String itemName, int iconResId) {
        this.itemName = itemName;
        this.iconResId = iconResId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getIconResId() {
        return iconResId;
    }
}
