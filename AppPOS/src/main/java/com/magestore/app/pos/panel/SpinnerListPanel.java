package com.magestore.app.pos.panel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.R;
import com.magestore.app.pos.adapter.CategoryAdapter;
import com.magestore.app.pos.controller.ProductListController;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by baonguyen on 26/04/2017.
 */

public class SpinnerListPanel extends AbstractListPanel<Category> {
    LinearLayout linearLayoutCategoy;
    ProductListController productListController;
    TextView titleCategory;
    PopupWindow popupWindow;

    public void setProductListController(ProductListController productListController) {
        this.productListController = productListController;
    }

    public SpinnerListPanel(Context context) {
        super(context);
    }

    public SpinnerListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        linearLayoutCategoy = (LinearLayout) findViewById(R.id.layout_categoryId);
        titleCategory = (TextView) findViewById(R.id.text_category);
    }

    boolean isOpen = false;

    //Start Felix 27/4/2017
    public void bindCategory(final List<Category> datas) {
        initSpinner(datas);
        linearLayoutCategoy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(titleCategory, -(int) getResources().getDimension(R.dimen.layout_margin_130) / 4 - 60, 0,Gravity.CENTER);
            }
        });

    }

    public void initSpinner(final List<Category> datas) {
        LayoutInflater layoutInflater
                = (LayoutInflater) getContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_list_category, null);

        ListView listView = (ListView) popupView.findViewById(R.id.recycler_category);
        CategoryAdapter adapter = new CategoryAdapter();
        adapter.setContext(getContext());
        adapter.setDatas(datas);
        listView.setAdapter(adapter);
        listView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(
                popupView,
                listView.getMeasuredWidth(),
                LayoutParams.WRAP_CONTENT);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    productListController.bindCategory((Category) null);
                } else
                    productListController.bindCategory(datas.get(position));
                titleCategory.setText(datas.get(position).getName());
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // Removes default background.
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    //End Felix 27/4


}
