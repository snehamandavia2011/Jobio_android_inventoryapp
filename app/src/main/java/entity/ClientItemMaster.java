package entity;

/**
 * Created by SAI on 10/11/2016.
 */
public class ClientItemMaster {
    String uuid, item_name, short_code, specification, category_name, uom_id,
            package_type_name, location_name, item_status, manufacturer, model, monthly_demand,
            min_qty_for_restock, display_status, custom_field_1, custom_field_2, custom_field_3,
            custom_field_4, custom_field_5;

    public ClientItemMaster(String uuid, String item_name, String short_code, String specification, String category_name, String uom_id, String package_type_name, String location_name, String item_status, String manufacturer, String model, String monthly_demand, String min_qty_for_restock, String display_status, String custom_field_1, String custom_field_2, String custom_field_3, String custom_field_4, String custom_field_5) {
        this.uuid = uuid;
        this.item_name = item_name;
        this.short_code = short_code;
        this.specification = specification;
        this.category_name = category_name;
        this.uom_id = uom_id;
        this.package_type_name = package_type_name;
        this.location_name = location_name;
        this.item_status = item_status;
        this.manufacturer = manufacturer;
        this.model = model;
        this.monthly_demand = monthly_demand;
        this.min_qty_for_restock = min_qty_for_restock;
        this.display_status = display_status;
        this.custom_field_1 = custom_field_1;
        this.custom_field_2 = custom_field_2;
        this.custom_field_3 = custom_field_3;
        this.custom_field_4 = custom_field_4;
        this.custom_field_5 = custom_field_5;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getShort_code() {
        return short_code;
    }

    public void setShort_code(String short_code) {
        this.short_code = short_code;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getUom_id() {
        return uom_id;
    }

    public void setUom_id(String uom_id) {
        this.uom_id = uom_id;
    }

    public String getPackage_type_name() {
        return package_type_name;
    }

    public void setPackage_type_name(String package_type_name) {
        this.package_type_name = package_type_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMonthly_demand() {
        return monthly_demand;
    }

    public void setMonthly_demand(String monthly_demand) {
        this.monthly_demand = monthly_demand;
    }

    public String getMin_qty_for_restock() {
        return min_qty_for_restock;
    }

    public void setMin_qty_for_restock(String min_qty_for_restock) {
        this.min_qty_for_restock = min_qty_for_restock;
    }

    public String getDisplay_status() {
        return display_status;
    }

    public void setDisplay_status(String display_status) {
        this.display_status = display_status;
    }

    public String getCustom_field_1() {
        return custom_field_1;
    }

    public void setCustom_field_1(String custom_field_1) {
        this.custom_field_1 = custom_field_1;
    }

    public String getCustom_field_2() {
        return custom_field_2;
    }

    public void setCustom_field_2(String custom_field_2) {
        this.custom_field_2 = custom_field_2;
    }

    public String getCustom_field_3() {
        return custom_field_3;
    }

    public void setCustom_field_3(String custom_field_3) {
        this.custom_field_3 = custom_field_3;
    }

    public String getCustom_field_4() {
        return custom_field_4;
    }

    public void setCustom_field_4(String custom_field_4) {
        this.custom_field_4 = custom_field_4;
    }

    public String getCustom_field_5() {
        return custom_field_5;
    }

    public void setCustom_field_5(String custom_field_5) {
        this.custom_field_5 = custom_field_5;
    }
}
