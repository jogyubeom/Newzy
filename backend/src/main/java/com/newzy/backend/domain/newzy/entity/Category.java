package com.newzy.backend.domain.newzy.entity;

public enum Category{
    시사("currentEvent"),
    문화("culture"),
    자유("free");

    private final String description;

    Category(String description){
        this.description = description;
    }
}
