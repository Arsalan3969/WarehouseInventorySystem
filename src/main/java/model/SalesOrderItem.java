package model;

public class SalesOrderItem {

    private int salesOrderItemID;
    private int salesOrderID;
    private int productID;
    private int quantity;
    private double unitPrice;

    public SalesOrderItem() {
    }

    public int getSalesOrderItemID() {
        return salesOrderItemID;
    }

    public void setSalesOrderItemID(int salesOrderItemID) {
        this.salesOrderItemID = salesOrderItemID;
    }

    public int getSalesOrderID() {
        return salesOrderID;
    }

    public void setSalesOrderID(int salesOrderID) {
        this.salesOrderID = salesOrderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

}