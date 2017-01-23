package com.magestore.app.pos.model.custom;

import com.magestore.app.lib.model.custom.PopupWindow;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPopupWindow extends PosAbstractModel implements PopupWindow {
    String title;
    String value;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String strTitle) {
        title = strTitle;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String strValue) {
        value = strValue;
    }
}
