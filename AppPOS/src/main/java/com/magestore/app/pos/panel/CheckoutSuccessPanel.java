package com.magestore.app.pos.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.PrintDialogActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.util.PrintUtil;
import com.magestore.app.util.DialogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johan on 3/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutSuccessPanel extends AbstractDetailPanel<Order> {
    TextView txt_order_id;
    Button btn_new_order, btn_send_email, btn_print;
    CheckoutListController mCheckoutListController;
    EditText edt_email_customer;
    RelativeLayout email_checkout_loading;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutSuccessPanel(Context context) {
        super(context);
    }

    public CheckoutSuccessPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutSuccessPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        txt_order_id = (TextView) findViewById(R.id.order_id);
        btn_print = (Button) findViewById(R.id.btn_print);
        btn_new_order = (Button) findViewById(R.id.btn_new_order);
        edt_email_customer = (EditText) findViewById(R.id.edt_email_customer);
        btn_send_email = (Button) findViewById(R.id.btn_send_email);
        email_checkout_loading = (RelativeLayout) findViewById(R.id.email_checkout_loading);
        initValue();
    }

    @Override
    public void initValue() {
        btn_new_order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckoutListController.actionNewOrder();
            }
        });

        btn_send_email.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSendEmail();
            }
        });
    }

    private void onClickSendEmail() {
        Checkout checkout = mCheckoutListController.getSelectedItem();
        Order order = checkout.getOrderSuccess();
        Map<String, Object> paramSendEmail = new HashMap<>();
        paramSendEmail.put("email", order.getCustomerEmail());
        paramSendEmail.put("increment_id", order.getIncrementId());
        mCheckoutListController.doInputSendEmail(paramSendEmail);
    }

    @Override
    public void bindItem(final Order item) {
        super.bindItem(item);
        txt_order_id.setText(getContext().getString(R.string.checkout_order_id, item.getIncrementId()));
        btn_print.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionPrint(item);
            }
        });
    }

    private void actionPrint(Order mOrder){
        final Dialog dialogPrint = new Dialog(getContext());
        dialogPrint.setCancelable(true);
        dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPrint.setFeatureDrawableAlpha(1, 1);
        dialogPrint.setContentView(R.layout.print_preview);
        WebView dialogWebView = (WebView) dialogPrint.findViewById(R.id.webview);
        TextView bt_print = (TextView) dialogPrint.findViewById(R.id.dialog_save);
        bt_print.setText(getContext().getString(R.string.print));
        TextView dialog_cancel = (TextView) dialogPrint.findViewById(R.id.dialog_cancel);
        dialog_cancel.setText(getContext().getString(R.string.cancel));
        TextView dialog_title = (TextView) dialogPrint.findViewById(R.id.dialog_title);
        dialog_title.setText(getContext().getString(R.string.checkout_order_id, mOrder.getIncrementId()));
        dialogWebView.getSettings().setJavaScriptEnabled(true);
        dialogWebView.getSettings().setLoadsImagesAutomatically(true);
        dialogWebView.setDrawingCacheEnabled(true);
        dialogWebView.buildDrawingCache();
        dialogWebView.capturePicture();
        PrintUtil.doPrint(getContext(), mOrder, dialogWebView);
        dialogPrint.show();

        dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPrint.dismiss();
            }
        });

        bt_print.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/RetailerPOS/PrintOrder.pdf");
                Intent printIntent = new Intent(getContext(),
                        PrintDialogActivity.class);
                printIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
                printIntent.putExtra("title", "");
                getContext().startActivity(printIntent);
            }
        });
    }
    /*Felix 4-4-2017 start*/
    public void fillEmailCustomer(Order order){
        edt_email_customer.setText(order.getCustomerEmail());
    }

    public void showAlertRespone(boolean statusRespone, String messageRes) {
        String message = "";
        if (statusRespone) {
            message = messageRes.length() > 0 ?
                    messageRes :
                    getContext().getString(R.string.checkout_send_email_success);
        } else {
            message = getContext().getString(R.string.err_send_email);
        }

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }

    public void showDetailOrderLoading(boolean visible){
        email_checkout_loading.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

     /*Felix 4-4-2017 end*/
}
