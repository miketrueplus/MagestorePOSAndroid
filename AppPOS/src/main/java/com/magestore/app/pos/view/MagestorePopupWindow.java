package com.magestore.app.pos.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import com.magestore.app.pos.R;

import java.util.List;

/**
 * Created by Johan on 1/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class MagestorePopupWindow extends PopupWindow {
    Context context;
    MagestorePopupWindowListView mMagestorePopupWindowListView;

    public MagestorePopupWindow(Context context, List<com.magestore.app.lib.model.custom.PopupWindow> popupWindows) {
        super(context);
        this.context = context;

        setContentView(LayoutInflater.from(context).inflate(R.layout.magestore_popup_window, null));

        mMagestorePopupWindowListView = (MagestorePopupWindowListView) this.getContentView().findViewById(R.id.popup_list_panel);
        mMagestorePopupWindowListView.bindList(popupWindows);
    }
}
