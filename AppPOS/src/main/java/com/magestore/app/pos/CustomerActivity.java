package com.magestore.app.pos;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.lib.service.customer.CustomerComplainService;
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

        initLayout();
        initModel();
        initValue();
    }

    @Override
    protected void initLayout() {
        // chuẩn bị tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        initToolbarMenu(mToolbar);

        // panel danh sách khách hàng và thông tin khách hàng chi tiết
        mCustomerListPanel = (CustomerListPanel) findViewById(R.id.customer_list_panel);
        mCustomerDetailPanel = (CustomerDetailPanel) findViewById(R.id.customer_detail_panel);

        // xem giao diện 2 pane hay 1 pane
        mblnTwoPane = findViewById(R.id.two_pane) != null;
    }

    @Override
    protected void initModel() {
        // Context sử dụng xuyên suốt hệ thống
        MagestoreContext magestoreContext = new MagestoreContext();
        magestoreContext.setActivity(this);

        // chuẩn bị service
        ServiceFactory factory;
        CustomerService service = null;
        CustomerAddressService addressService = null;
        CustomerComplainService complainService = null;

        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            service = factory.generateCustomerService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // Tạo list controller
        mCustomerListController = new CustomerListController();
        mCustomerListController.setMagestoreContext(magestoreContext);
        mCustomerListController.setCustomerService(service);
        mCustomerListController.setListPanel(mCustomerListPanel);
        mCustomerListController.setDetailPanel(mCustomerDetailPanel);

        // chuẩn bị model cho các panel
        mCustomerListPanel.initModel();
        mCustomerDetailPanel.initModel();
    }

    @Override
    protected void initValue() {
        // load danh sách khách hàng
        mCustomerListController.doRetrieveItem(1, 30);
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
