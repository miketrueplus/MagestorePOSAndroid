package com.magestore.app.lib.model.order;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.order.PosOrderAttributes;
import com.magestore.app.pos.model.order.PosOrderBillingAddress;
import com.magestore.app.pos.model.order.PosOrderPayment;

import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface Order extends Model{
    PosOrderBillingAddress getBillingAddress();

    PosOrderPayment getPayment();

    List<OrderItems> getItems();

    PosOrderAttributes getExtensionAttributes();
}
