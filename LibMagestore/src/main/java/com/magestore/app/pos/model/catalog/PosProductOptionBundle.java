package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductOptionBundle;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by folio on 3/7/2017.
 */
public class PosProductOptionBundle extends PosAbstractModel implements ProductOptionBundle {
    String title;
    String required;
    String type;
    String product_id;
    String shipment_type;
    List<PosProductOptionBundleItem> items;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isRequired() {
        return "1".equals(required);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getProductId() {
        return product_id;
    }

    @Override
    public String getShipmentType() {
        return shipment_type;
    }

    @Override
    public List<PosProductOptionBundleItem> getItems() {
        return items;
    }
}
