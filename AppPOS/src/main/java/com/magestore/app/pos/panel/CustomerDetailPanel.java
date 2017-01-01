package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.magestore.app.lib.entity.Customer;
import com.magestore.app.pos.R;

/**
 * Hiển thị và quản lý các thông tin chi tiết của 1 customer
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerDetailPanel extends FrameLayout {
    // Listner
    CustomerDetailPanelListener mCustomerListPanelListener;

    // khách hàng
    Customer mCustomer;

    // các control
    ImageButton mbtnCheckOut;
    TextView mtxtFirstName;
    TextView mtxtLastName;
    TextView mtxtEmail;
    TextView mtxtGroup;

    public CustomerDetailPanel(Context context) {
        super(context);
        init();
    }

    public CustomerDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initControlLayout();
        initControlValue();
        initTask();
    }

    private void initControlLayout() {
        // Load layout view thông tin khách hàng chi tiết
        View v = inflate(getContext(), R.layout.panel_customer_detail, null);
        addView(v);

        // load các control vào các biến
        mtxtFirstName = (TextView) findViewById(R.id.txt_first_name);
        mtxtLastName = (TextView) findViewById(R.id.txt_last_name);
        mtxtEmail = (TextView) findViewById(R.id.txt_email);
        mtxtGroup = (TextView) findViewById(R.id.txt_group);
        mbtnCheckOut = (ImageButton) findViewById(R.id.btn_check_out);
    }

    private void initControlValue() {

    }

    private void initTask() {

    }

    /**
     * Listener bắt sự kiện
     * @param customerPanelListener
     */
    public void setListener(CustomerDetailPanelListener customerPanelListener) {
        mCustomerListPanelListener = customerPanelListener;
    }

    /**
     * Đặt customer
     * @param customer
     */
    public void setCustomer(Customer customer) {
        mCustomer = customer;
        mtxtFirstName.setText(getContext().getText(R.string.first_name) + ": " + customer.getFirstName());
        mtxtLastName.setText(getContext().getText(R.string.last_name) + ": " +customer.getLastName());
        mtxtEmail.setText(getContext().getText(R.string.email) + ": " +customer.getEmail());
        mtxtGroup.setText(getContext().getText(R.string.group) + ": " +customer.getGroupID());
    }
}
