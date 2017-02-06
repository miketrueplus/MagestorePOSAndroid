package com.magestore.app.pos.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardCustomerListContentBinding;

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
    protected void bindItem(View view, Customer item, int position) {
        CardCustomerListContentBinding binding = DataBindingUtil.bind(view);
        binding.setCustomer(item);
    }

    @Override
    public void initLayout() {
        // Xử lý sự kiện floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
