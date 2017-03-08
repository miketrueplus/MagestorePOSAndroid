package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.catalog.PosProductOptionBundleItem;

import java.util.List;

/**
 * Created by folio on 3/7/2017.
 */

public interface ProductOptionBundle extends Model {
    String getTitle();

    boolean isRequired();

    String getType();

    String getProductId();

    String getShipmentType();

    List<PosProductOptionBundleItem> getItems();
}
