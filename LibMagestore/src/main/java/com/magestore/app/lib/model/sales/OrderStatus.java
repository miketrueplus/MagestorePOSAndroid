package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface OrderStatus extends Model {
    String getComment();
    void setComment(String strComment);
    String getCreatedAt();
    void setCreatedAt(String strCreateAt);
    void setId(String strId);
    String getEntityName();
    void setEntityName(String strEntityName);
    String getIsCustomerNotified();
    void setIsCustomerNotified(String strIsCustomerNotified);
    String getIsVisibleOnFront();
    void setIsVisibleOnFront(String strIsVisibleOnFront);
    String getParentId();
    void setParentId(String strParentId);
    String getStatus();
    void setStatus(String strStatus);
    StatusAttributes getExtensionAttributes();
    void setExtensionAttributes(StatusAttributes statusAttributes);
}
