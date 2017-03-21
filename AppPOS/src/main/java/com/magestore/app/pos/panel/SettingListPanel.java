package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.databinding.CardSettingListContentBinding;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SettingListPanel extends AbstractListPanel<Setting> {
    public SettingListPanel(Context context) {
        super(context);
    }

    public SettingListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, Setting item, int position) {
        CardSettingListContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setSetting(item);
    }
}
