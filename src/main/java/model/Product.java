package model;

public class Product {

    private int productID;
    private int categoryID;
    private int supplierID;
    private String productName;
    private String sku;
    private double unitPrice;
    private int reorderLevel;
    private String description;
    private String status;

    public Product() {
    }

    public Product(int categoryID, int supplierID, String productName,
                   String sku, double unitPrice,
                   int reorderLevel, String description,
                   String status) {

        this.categoryID = categoryID;
        this.supplierID = supplierID;
        this.productName = productName;
        this.sku = sku;
        this.unitPrice = unitPrice;
        this.reorderLevel = reorderLevel;
        this.description = description;
        this.status = status;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
