package entity;

/**
 * Created by SAI on 10/13/2016.
 */
public class ClientItemTransaction {
    private String uuid, available_qty, cost, price, barcode, spplierName;

    public ClientItemTransaction(String uuid, String available_qty, String cost, String price, String barcode, String spplierName) {
        this.uuid = uuid;
        this.available_qty = available_qty;
        this.cost = cost;
        this.price = price;
        this.barcode = barcode;
        this.spplierName = spplierName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSpplierName() {
        return spplierName;
    }

    public void setSpplierName(String spplierName) {
        this.spplierName = spplierName;
    }
}
