package com.magestore.app.lib.model.custom;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PopupWindow extends Model {
    String getTitle();

    void setTitle(String strTitle);

    String getValue();

    void setValue(String strValue);
}
