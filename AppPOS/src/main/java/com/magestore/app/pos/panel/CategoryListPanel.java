package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.databinding.CardCategoryContentBinding;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CategoryListPanel extends AbstractListPanel<Category> {

    public CategoryListPanel(Context context) {
        super(context);
    }

    public CategoryListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, Category item, int position) {
        CardCategoryContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setCategory(item);
    }
}
