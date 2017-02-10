package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderCommentParams extends Model {
    String getComment();
    void setComment(String strComment);

    String getIsVisibleOnFront();
    void setIsVisibleOnFront(String strIsVisibleOnFront);

    void setEntityName(String strEntityName);
    String getEntityName();

    String getIsCustomerNotified();
    void setIsCustomerNotified(String strIsCustomerNotified);

    void setParentId(String strParentId);

    void setStatus(String strStatus);
}
