package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderCartItem;
import com.magestore.app.lib.model.sales.OrderCustomSalesInfo;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by folio on 5/4/2017.
 */

public class PosOrderCustomSalesInfo extends PosAbstractModel implements OrderCustomSalesInfo {
    String product_id;
    String product_name;
    String unit_price;
    String tax_class_id;
    String is_virtual;
    String qty;


}
