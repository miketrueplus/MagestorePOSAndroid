package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderStatus extends PosAbstractModel implements OrderStatus {
    String comment;
    String created_at;
    String entity_id;
    String entity_name;
    String is_customer_notified;
    String is_visible_on_front;
    String parent_id;
    String status;

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getCreatedAt() {
        return created_at;
    }
}
