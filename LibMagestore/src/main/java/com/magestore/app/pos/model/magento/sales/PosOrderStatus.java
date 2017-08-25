package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.StatusAttributes;
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
    PosStatusAttributes extensionAttributes;

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String strComment) {
        comment = strComment;
    }

    @Override
    public String getCreatedAt() {
        return created_at;
    }

    @Override
    public void setCreatedAt(String strCreateAt) {
        created_at = strCreateAt;
    }

    @Override
    public void setId(String strId) {
        entity_id = strId;
    }

    @Override
    public String getEntityName() {
        return entity_name;
    }

    @Override
    public void setEntityName(String strEntityName) {
        entity_name = strEntityName;
    }

    @Override
    public String getIsCustomerNotified() {
        return is_customer_notified;
    }

    @Override
    public void setIsCustomerNotified(String strIsCustomerNotified) {
        is_customer_notified = strIsCustomerNotified;
    }

    @Override
    public String getIsVisibleOnFront() {
        return is_visible_on_front;
    }

    @Override
    public void setIsVisibleOnFront(String strIsVisibleOnFront) {
        is_visible_on_front = strIsVisibleOnFront;
    }

    @Override
    public String getParentId() {
        return parent_id;
    }

    @Override
    public void setParentId(String strParentId) {
        parent_id = strParentId;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void setStatus(String strStatus) {
        status = strStatus;
    }

    @Override
    public StatusAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    @Override
    public void setExtensionAttributes(StatusAttributes statusAttributes) {
        extensionAttributes = (PosStatusAttributes) statusAttributes;
    }
}
