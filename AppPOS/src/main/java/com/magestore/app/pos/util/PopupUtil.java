package com.magestore.app.pos.util;

import android.content.Context;

import com.magestore.app.lib.model.custom.PopupWindow;
import com.magestore.app.pos.view.MagestorePopupWindow;

import java.util.List;

/**
 * Created by Johan on 1/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PopupUtil {
    public static MagestorePopupWindow popupWindow(Context context, List<PopupWindow> popupWindowList) {
        MagestorePopupWindow popupWindow = new MagestorePopupWindow(context, popupWindowList);
        return popupWindow;
    }
}
