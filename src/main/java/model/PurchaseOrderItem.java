package model;

public class PurchaseOrderItem {

    private int purchaseOrderItemID;
    private int purchaseOrderID;
    private int productID;
    private int quantity;
    private double unitPrice;

    public PurchaseOrderItem() {
    }

    public int getPurchaseOrderItemID() {
        return purchaseOrderItemID;
    }

    public void setPurchaseOrderItemID(int purchaseOrderItemID) {
        this.purchaseOrderItemID = purchaseOrderItemID;
    }

    public int getPurchaseOrderID() {
        return purchaseOrderID;
    }

    public void setPurchaseOrderID(int purchaseOrderID) {
        this.purchaseOrderID = purchaseOrderID;
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