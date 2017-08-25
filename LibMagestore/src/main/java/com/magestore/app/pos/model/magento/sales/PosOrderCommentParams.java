package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderCommentParams extends PosAbstractModel implements OrderCommentParams {
    String comment;
    String createdAt;
    String isVisibleOnFront;
    String entityId;
    String entityName;
    PosOrderAttributes extensionAttributes;
    String isCustomerNotified;
    String parentId;
    String status;

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String strComment) {
        comment = strComment;
    }

    @Override
    public String getIsVisibleOnFront() {
        return isVisibleOnFront;
    }

    @Override
    public void setIsVisibleOnFront(String strIsVisibleOnFront) {
        isVisibleOnFront = strIsVisibleOnFront;
    }

    @Override
    public void setEntityName(String strEntityName) {
        entityName = strEntityName;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getIsCustomerNotified() {
        return isCustomerNotified;
    }

    @Override
    public void setIsCustomerNotified(String strIsCustomerNotified) {
        isCustomerNotified = strIsCustomerNotified;
    }

    @Override
    public void setParentId(String strParentId) {
        parentId = strParentId;
    }

    @Override
    public void setStatus(String strStatus) {
        status = strStatus;
    }
}
