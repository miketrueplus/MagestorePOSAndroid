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
}
