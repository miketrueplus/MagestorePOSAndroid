package com.magestore.app.pos.panel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.SearchAutoCompletePanel;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.SalesActivity;
import com.magestore.app.pos.controller.CheckoutAddCustomerController;

/**
 * Created by Johan on 2/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddCustomerPanel extends AbstractDetailPanel<Checkout> {
    CustomerListPanel mCustomerListPanel;
    CheckoutAddCustomerController mCustomerListController;
    MagestoreContext mMagestoreContext;
    CustomerService mCustomerService;
    CheckoutListPanel mCheckoutListPanel;
    ImageView customer_barcode;
    SearchAutoCompletePanel panel_search_customer;

    public CheckoutAddCustomerPanel(Context context) {
        super(context);
    }

    public CheckoutAddCustomerPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutAddCustomerPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMagestoreContext(MagestoreContext mMagestoreContext) {
        this.mMagestoreContext = mMagestoreContext;
    }

    public void setCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    public void setCheckoutListPanel(CheckoutListPanel mCheckoutListPanel) {
        this.mCheckoutListPanel = mCheckoutListPanel;
    }

    public CheckoutAddCustomerController getCustomerListController() {
        return mCustomerListController;
    }

    @Override
    protected void initLayout() {
        View v = inflate(getContext(), R.layout.panel_checkout_add_customer, null);
        addView(v);

        mCustomerListPanel = (CustomerListPanel) v.findViewById(R.id.sales_customer_items);
        customer_barcode = (ImageView) v.findViewById(R.id.customer_barcode);
        FloatingActionButton fab = (FloatingActionButton) mCustomerListPanel.findViewById(R.id.fab);
        fab.setVisibility(GONE);

        panel_search_customer = (SearchAutoCompletePanel) mCustomerListPanel.findViewById(R.id.panel_search_customer);
        AutoCompleteTextView text_search_customer = (AutoCompleteTextView) mCustomerListPanel.findViewById(R.id.text_search_customer);
        text_search_customer.setFocusable(true);
        initModel();
    }

    @Override
    public void bindItem(Checkout item) {
        if(item == null){return;}
        super.bindItem(item);
    }

    @Override
    public void initModel() {
        // Tạo list controller
        mCustomerListController = new CheckoutAddCustomerController();
        mCustomerListController.setMagestoreContext(mMagestoreContext);
        mCustomerListController.setCustomerService(mCustomerService);
        mCustomerListController.setListPanel(mCustomerListPanel);
        mCustomerListController.setCheckoutListPanel(mCheckoutListPanel);
        mCustomerListController.setCheckoutAddCustomerPanel(this);

        // chuẩn bị model cho các panel
        mCustomerListPanel.initModel();
    }

    @Override
    public void initValue() {
        mCustomerListController.doRetrieve();

        customer_barcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
    }

    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        String cameraPermission = Manifest.permission.CAMERA;
        int permissionCheck = ContextCompat.checkSelfPermission(mCustomerListController.getMagestoreContext().getActivity(), cameraPermission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            scanBarcode();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCustomerListController.getMagestoreContext().getActivity().requestPermissions(new String[]{cameraPermission}, SalesActivity.REQUEST_PERMISSON_CAMERA);
            }
        }
    }

    public void scanBarcode() {
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(mCustomerListController.getMagestoreContext().getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
//                        mSearchAutoCompletePanel.actionSearch();
                        panel_search_customer.getAutoTextView().setText(barcode.rawValue);
                        panel_search_customer.actionSearch();
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    public void updateCustomerToOrder(Customer customer) {
        mCheckoutListPanel.updateCustomerToOrder(customer);
    }

    public void notifiDataListCustomer(){
        mCustomerListPanel.notifyDataSetChanged();
    }

    public void scrollToTop(){
        mCustomerListPanel.getRecycleViewLayoutManager().scrollToPosition(0);
    }
}
