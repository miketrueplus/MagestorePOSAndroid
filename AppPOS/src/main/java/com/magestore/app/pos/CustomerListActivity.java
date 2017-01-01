package com.magestore.app.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.magestore.app.lib.entity.Customer;
import com.magestore.app.pos.panel.CustomerDetailPanel;
import com.magestore.app.pos.panel.CustomerDetailPanelListener;
import com.magestore.app.pos.panel.CustomerListPanel;
import com.magestore.app.pos.panel.CustomerListPanelListener;
import com.magestore.app.pos.ui.AbstractActivity;

import java.util.List;

/**
 *
 */
public class CustomerListActivity extends AbstractActivity
        implements
        CustomerListPanelListener, CustomerDetailPanelListener {
    private CustomerListPanel mCustomerListPanel = null;
    private CustomerDetailPanel mCustomerDetailPanel = null;
    private boolean mblnTwoPane;
    private Toolbar mToolbar;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_menu);

        initControlLayout();
        initControlValue();
        initTask();

        // load danh sách khách hàng
        mCustomerListPanel.loadCustomerList();
    }

    @Override
    protected void initControlLayout() {
        super.initControlLayout();

        // chuẩn bị tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        initToolbarMenu(mToolbar);

        // chuẩn bị panel danh sách khách hàng
        mCustomerListPanel = (CustomerListPanel) findViewById(R.id.customer_list_panel);
        mCustomerListPanel.setListener(this);

        // chuẩn bị panel thông tin khách hàng chi tiết
        mCustomerDetailPanel = (CustomerDetailPanel) findViewById(R.id.customer_detail_panel);
        if (mCustomerDetailPanel != null)
            mCustomerDetailPanel.setListener(this);

        // xem giao diện 2 pane hay 1 pane
        mblnTwoPane = findViewById(R.id.two_pane) != null;
    }

    @Override
    protected void initControlValue() {
        super.initControlValue();
    }

    @Override
    protected void initTask() {
        super.initTask();
    }

    @Override
    public void onSelectCustomer(Customer customer) {
        mCustomerDetailPanel.setCustomer(customer);
        if (!mblnTwoPane) {
            mCustomerDetailPanel.setVisibility(View.VISIBLE);
            mCustomerListPanel.setVisibility(View.INVISIBLE);

            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                mToolbar.getMenu().clear();
                setSupportActionBar(mToolbar);
//                actionBar.get
//                actionBar.set`
            }
        }


    }

    @Override
    public void onSuccessLoadCustomer(List<Customer> customerList) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mCustomerDetailPanel.setVisibility(View.INVISIBLE);
            mCustomerListPanel.setVisibility(View.VISIBLE);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            initToolbarMenu(mToolbar);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
