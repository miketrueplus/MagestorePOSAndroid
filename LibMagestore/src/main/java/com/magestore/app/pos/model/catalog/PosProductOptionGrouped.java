package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOptionBundle;
import com.magestore.app.lib.model.catalog.ProductOptionGrouped;
import com.magestore.app.lib.model.inventory.Stock;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.inventory.PosStock;

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

    private List<PosStock> stock;


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
    public float getPrice() {
        return Float.parseFloat(price);
    }

    @Override
    public void setPrice(float price) {
        this.price = Float.toString(price);
    }

    @Override
    public int getDefaultQty() {
        return (int) Float.parseFloat(default_qty);
    }

    @Override
    public void setDefaultQty(int default_qty) {
        this.default_qty = Integer.toString(default_qty);
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

    @Override
    public List<Stock> getStock() {
        return (List<Stock>) (List<?>) stock;
    }

    @Override
    public int getQuantityIncrement() {
        if ((stock == null) || (stock.size() <= 0)) return 1;
        return stock.get(0).getQuantityIncrement();
    }
}