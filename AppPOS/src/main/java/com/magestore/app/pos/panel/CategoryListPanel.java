package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CategoryListController;
import com.magestore.app.pos.databinding.CardCategoryContentBinding;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CategoryListPanel extends AbstractListPanel<Category> {
    RelativeLayout rl_select_category;

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
    public void initLayout() {

    }

    @Override
    protected void bindItem(View view, final Category item, int position) {
        CardCategoryContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setCategory(item);

        rl_select_category = (RelativeLayout) view.findViewById(R.id.rl_select_category);
        rl_select_category.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CategoryListController) mController).selectCategoryChild(item);
            }
        });
    }
}
