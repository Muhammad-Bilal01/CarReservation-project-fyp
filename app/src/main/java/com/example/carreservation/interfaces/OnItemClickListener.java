package com.example.carreservation.interfaces;

import com.example.carreservation.models.Spot;

import java.util.Map;

public interface OnItemClickListener {
    void onDeleteButtonClick(Map<String,Object> model, int position);
    void onUpdateButtonClick(Map<String,Object> model, int position);
    void onDetailsButtonClick(Map<String,Object> model, int position);
    void onBookButtonClick(Spot model, int position);
    void onMyBookButtonClick(Map<String,Object> model, int position);
}
