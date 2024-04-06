package com.example.carreservation.models;

public class SelectedSlot {

    private String slotName;
//    private int slotNo;

    private Double slotPrice;

//    public int getSlotNo() {
//        return slotNo;
//    }

//    public void setSlotNo(int slotNo) {
//        this.slotNo = slotNo;
//    }

//    default constructor
    public SelectedSlot() {
    }

    public SelectedSlot(String slotName, Double slotPrice) {
        this.slotName = slotName;
        this.slotPrice = slotPrice;
//        this.slotNo = slotNo;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public Double getSlotPrice() {
        return slotPrice;
    }

    public void setSlotPrice(Double slotPrice) {
        this.slotPrice = slotPrice;
    }

}
