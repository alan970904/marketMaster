package com.MarketMaster.DTO.restock;

public class ProductCategoryDTO {
    private String productCategory;

    public ProductCategoryDTO(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getCategoryName() {
        return productCategory;
    }

    public void setCategoryName(String categoryName) {
        this.productCategory = categoryName;
    }
}