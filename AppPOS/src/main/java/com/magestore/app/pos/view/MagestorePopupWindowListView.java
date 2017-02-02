package com.magestore.app.pos.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.custom.PopupWindow;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.pos.databinding.PanelPopupWindowBinding;

/**
 * Created by Johan on 1/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class MagestorePopupWindowListView extends AbstractSimpleListView<PopupWindow> {
    public MagestorePopupWindowListView(Context context) {
        super(context);
    }

    public MagestorePopupWindowListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagestorePopupWindowListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, PopupWindow item, int position) {
        PanelPopupWindowBinding binding = DataBindingUtil.bind(view);
        binding.setPopupWindow(item);
    }
}
