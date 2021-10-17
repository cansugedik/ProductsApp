package com.example.productsapp.model;

public class ProductModel {
    //Ürünün girildiği alan ( Ürün kategorisi, fiyatı, başlığı, açıklaması ve eklenme tarihi vb. Alanlar)
    private int productId;
    private String productHeader;
    private String productCategory;
    private String productDescription;
    private int productPrice;

   /* public ProductModel(){  }

    public ProductModel(int productid, String productcategory, String productheader, String productdescription, int productprice) {

        this.productId = productid;
        this.productHeader = productheader;
        this.productCategory = productcategory;
        this.productDescription = productdescription;
        this.productPrice = productprice;
    }*/

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductHeader() {
        return productHeader;
    }

    public void setProductHeader(String productHeader) {
        this.productHeader = productHeader;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
