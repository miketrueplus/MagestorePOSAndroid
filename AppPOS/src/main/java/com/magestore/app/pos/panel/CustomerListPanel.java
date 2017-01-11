package com.magestore.app.pos.panel;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;

/**
 * Panel giao diện quản lý danh sách khách hàng
 * Created by Mike on 12/29/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class CustomerListPanel extends AbstractListPanel<Customer> {
    /**
     * Khởi tạo
     * @param context
     */
    public CustomerListPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     */
    public CustomerListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomerListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, Customer item) {
        ((TextView) view.findViewById(R.id.customer_name)).setText(item.getName());
        ((TextView) view.findViewById(R.id.customer_email)).setText(item.getEmail());
        ((TextView) view.findViewById(R.id.customer_telephone)).setText(item.getTelephone());
    }

    @Override
    public void initLayout() {
//        super.initControlLayout();

        // Load layout view danh sách khách hàng
        View v = inflate(getContext(), R.layout.panel_customer_list, null);
        addView(v);

        // Xử lý sự kiện floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Chuẩn bị layout từng item trong danh sách khách hàng
        setLayoutItem(R.layout.card_customer_list_content);

        // Chuẩn bị list danh sách khách hàng
        mRecycleView = (RecyclerView) findViewById(R.id.customer_list);
        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
    }
}
