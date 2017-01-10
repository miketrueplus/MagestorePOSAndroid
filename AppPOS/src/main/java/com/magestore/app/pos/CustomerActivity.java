package com.magestore.app.pos;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.controller.CustomerListController;
import com.magestore.app.pos.panel.CustomerDetailPanel;
import com.magestore.app.pos.panel.CustomerListPanel;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 *
 */
public class CustomerActivity extends AbstractActivity {

    // View và controller tương ứng
    private CustomerListPanel mCustomerListPanel = null;
    private CustomerListController mCustomerListController = null;

    private CustomerDetailPanel mCustomerDetailPanel = null;
//    private CustomerDetailController mCustomerDetailController = null;
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
        mCustomerListController.doLoadData();
    }

    @Override
    protected void initControlLayout() {
        super.initControlLayout();

        // chuẩn bị tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        initToolbarMenu(mToolbar);

        // chuẩn bị service
        ServiceFactory factory;
        CustomerService service = null;
        try {
            factory = ServiceFactory.getFactory(null);
            service = factory.generateCustomerService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // chuẩn bị panel view và controller danh sách khách hàng
        mCustomerListController = new CustomerListController();
        mCustomerListController.setCustomerService(service);

        mCustomerListPanel = (CustomerListPanel) findViewById(R.id.customer_list_panel);
        mCustomerListPanel.setController(mCustomerListController);

        mCustomerDetailPanel = (CustomerDetailPanel) findViewById(R.id.customer_detail_panel);

        mCustomerListController.setView(mCustomerListPanel);
        mCustomerListController.setDetailPanel(mCustomerDetailPanel);

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

//    @Override
//    public void onSelectItemInList(Customer item) {
//        mCustomerDetailPanel.setCustomer(item);
//        if (!mblnTwoPane) {
//            mCustomerDetailPanel.setVisibility(View.VISIBLE);
//            mCustomerListPanel.setVisibility(View.INVISIBLE);
//
//            // Show the Up button in the action bar.
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setDisplayHomeAsUpEnabled(true);
//                mToolbar.getMenu().clear();
//                setSupportActionBar(mToolbar);
////                actionBar.get
////                actionBar.set`
//            }
//        }
//    }

//    @Override
//    public void onSuccessLoadList(List<Customer> list) {
//
//    }
}
