package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.catalog.PosProductOptionBundleItem;

import java.util.List;

/**
 * Created by folio on 3/7/2017.
 */

public interface ProductOptionGrouped extends Model {
    String getTypeID();

    void setTypeID(String type_id);

    String getSKU();

    void setSKU(String sku);

    String getName();

    void setName(String name);

    String getPrice();

    void setPrice(String price);

    String getDefaultQty();

    void setDefaultQty(String default_qty);

    String getImage();

    void setImage(String image);

    String getTaxClassID();

    void setTaxClassID(String tax_class_id);
}
