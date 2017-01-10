package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.magestore.app.lib.adapterview.adapter2pos.AdapterView2Model;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.controller.CustomerAddressListController;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.R;
import com.magestore.app.pos.util.DialogUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * Hiển thị và quản lý các thông tin chi tiết của 1 customer
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerDetailPanel extends AbstractDetailPanel<Customer> {
    // controller và view để quản lý địa chỉ của khách hàng
    CustomerAddressListPanel mCustomerAddressListPanel;
    CustomerAddressListController mCustomerAddressListController;

    // các control
    ImageButton mbtnCheckOut;
    ImageButton mbtnNewAddress;

    // Map từ entity sang view
    AdapterView2Model mAdapter2View;

    public CustomerDetailPanel(Context context) throws InstantiationException, IllegalAccessException {
        super(context);
    }

    public CustomerDetailPanel(Context context, AttributeSet attrs) throws InstantiationException, IllegalAccessException {
        super(context, attrs);
    }

    public CustomerDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) throws InstantiationException, IllegalAccessException {
        super(context, attrs, defStyleAttr);
    }



    protected void initControlLayout() {
        // Load layout view thông tin khách hàng chi tiết
        View v = inflate(getContext(), R.layout.panel_customer_detail, null);
        addView(v);

        // chuẩn bị panel view và controller danh sách khách hàng
        mCustomerAddressListController = new CustomerAddressListController();
        mCustomerAddressListPanel = (CustomerAddressListPanel) findViewById(R.id.customer_address);
//        mCustomerAddressListController.setCustomerService(service);
//        mCustomerListController.setDetailControl(mCustomerDetailController);

        // Tham chiếu view và controller
        mCustomerAddressListPanel.setController(mCustomerAddressListController);
        mCustomerAddressListController.setView(mCustomerAddressListPanel);

        // load các control vào các biến
//        mbtnCheckOut = (ImageButton) findViewById(R.id.btn_check_out);

        // các button
//        mbtnNewAddress = (ImageButton) findViewById(R.id.btn_new_address);
//        mbtnCheckOut = (ImageButton) findViewById(R.id.btn_check_out);

        // Xử lý sự kiện các button new address
//        mbtnNewAddress.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickNewAddress(v);
//            }
//        });

        // Xử lý sự kiện button checkout
//        mbtnCheckOut.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickCheckout(v);
//            }
//        });
    }

    /**
     * Khởi tạo các value cho các control
     */
    protected void initControlValue() {
        // Khởi tạo customer use case
        mAdapter2View = new AdapterView2Model();
        mAdapter2View.setModelClass(PosCustomer.class);
        mAdapter2View.setView(this);
    }


    /**
     * Sự kiện khi ấn nút checkout
     * @param v
     */
    public void onClickCheckout(View v) {

    }

    /**
     * Sự kiện khi ấn nút new address
     * @param v
     */
    public void onClickNewAddress(View v) {
        // Chuẩn bị layout cho dialog customerAddress
        final CustomerAddressDetailPanel panelAddress = new CustomerAddressDetailPanel(getContext());

        // Khởi tạo sẵn customerAddress mới
//        CustomerAddress customerAddress = mCustomerUsecase.createAddress();
//        panelAddress.setAddress(customerAddress);


        // Tạo dialog và hiển thị
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("New customerAddress")
                .setView(panelAddress)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        try {
//                            mCustomerUsecase.newAddress(panelAddress.getAddress());
//                        } catch (InvocationTargetException e) {
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                        mCustomerAddressRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    @Override
    public void bindItem(Customer item) {
        super.bindItem(item);
        mAdapter2View.setModel(mItem);
        // gán các text box
        try {
            mAdapter2View.toView();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mCustomerAddressListController.doSelectCustomer(item);
    }
}
