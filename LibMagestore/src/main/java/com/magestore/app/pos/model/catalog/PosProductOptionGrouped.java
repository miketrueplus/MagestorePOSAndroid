package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOptionBundle;
import com.magestore.app.lib.model.catalog.ProductOptionGrouped;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by folio on 3/7/2017.
 */
public class PosProductOptionGrouped extends PosAbstractModel implements ProductOptionGrouped {
    String type_id;
    String sku;
    String name;
    String price;
    String default_qty;
    String image;
    String tax_class_id;

    @Override
    public String getTypeID() {
        return type_id;
    }

    @Override
    public void setTypeID(String type_id) {
        this.type_id = type_id;
    }

    @Override
    public String getSKU() {
        return sku;
    }

    @Override
    public void setSKU(String sku) {
        this.sku = sku;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String getDefaultQty() {
        return default_qty;
    }

    @Override
    public void setDefaultQty(String default_qty) {
        this.default_qty = default_qty;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getTaxClassID() {
        return tax_class_id;
    }

    /**
     * ID tax
     * @param tax_class_id
     */
    @Override
    public void setTaxClassID(String tax_class_id) {
        this.tax_class_id = tax_class_id;
    }
}