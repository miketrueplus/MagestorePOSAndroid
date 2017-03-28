package com.magestore.app.pos;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.SubjectObserv;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.controller.CustomerAddressListController;
import com.magestore.app.pos.controller.CustomerListController;
import com.magestore.app.pos.panel.CustomerAddressListPanel;
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

    // view và controller cho địa chỉ
    CustomerAddressListPanel mCustomerAddressListPanel;
    CustomerAddressListController mCustomerAddressListController;

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

        super.setheader();
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

        // panel địa chỉ khách hàng
        mCustomerAddressListPanel = (CustomerAddressListPanel) findViewById(R.id.customer_address);

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
        CustomerService customersService = null;
        CustomerAddressService customerAddressService = null;

        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            customersService = factory.generateCustomerService();
            customerAddressService = factory.generateCustomerAddressService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // Tạo list controller
        mCustomerListController = new CustomerListController();
        mCustomerListController.setMagestoreContext(magestoreContext);
        mCustomerListController.setListService(customersService);
        mCustomerListController.setCustomerService(customersService);
        mCustomerListController.setListPanel(mCustomerListPanel);
        mCustomerListController.setDetailPanel(mCustomerDetailPanel);
        mCustomerListController.setInsertAtFirst();
        mCustomerListController.setInsertBeforeSuccess();

        // Chuẩn bị controller quản lý danh sách địa chỉ khách hàng
        mCustomerAddressListController = new CustomerAddressListController();
        mCustomerAddressListController.setView(mCustomerAddressListPanel);
        mCustomerAddressListController.setMagestoreContext(magestoreContext);
        mCustomerAddressListController.setCustomerService(customersService);
        mCustomerAddressListController.setChildListService(customerAddressService);



        // chuẩn bị model cho các panel
        mCustomerListPanel.initModel();
        mCustomerDetailPanel.initModel();

        // khởi tạo subject, observe
        SubjectObserv subjectObserv = new SubjectObserv();
        mCustomerListController.setSubject(subjectObserv);
        mCustomerAddressListController.setSubject(subjectObserv);
        mCustomerAddressListController
                .attachListenerObserve()
                .setStateCode(GenericState.DEFAULT_STATE_CODE_ON_SELECT_ITEM)
                .setControllerState(mCustomerListController)
                .setMethodName("bindParent");

        mCustomerAddressListController
                .attachListenerObserve()
                .setStateCode(CustomerListController.STATE_CODE_ON_CLICK_NEW_ADDRESS)
                .setControllerState(mCustomerListController)
                .setMethodName("doShowInsertItemInput");
    }

    @Override
    protected void initValue() {
        // load danh sách khách hàng
        mCustomerListController.doRetrieve();
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
