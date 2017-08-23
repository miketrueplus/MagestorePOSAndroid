package com.magestore.app.pos.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.PrintDialogActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.util.PrintUtil;
import com.magestore.app.pos.util.StarPrintUtitl;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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

    private void actionPrint(Order mOrder) {
        final Dialog dialogPrint = new Dialog(getContext());
        dialogPrint.setCancelable(true);
        dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPrint.setFeatureDrawableAlpha(1, 1);
        if (ConfigUtil.getTypePrint().equals(getContext().getString(R.string.print_type_star_print))) {
            dialogPrint.setContentView(R.layout.star_print_order_layout);
            ViewGroup.LayoutParams params = dialogPrint.getWindow().getAttributes();
            params.width = ConfigUtil.getStarPrintArea() + 50;
            dialogPrint.getWindow().setAttributes((WindowManager.LayoutParams) params);
        } else {
            dialogPrint.setContentView(R.layout.print_preview);
        }

        TextView bt_print = (TextView) dialogPrint.findViewById(R.id.dialog_save);
        bt_print.setText(getContext().getString(R.string.print));
        TextView dialog_cancel = (TextView) dialogPrint.findViewById(R.id.dialog_cancel);
        dialog_cancel.setText(getContext().getString(R.string.cancel));
        TextView dialog_title = (TextView) dialogPrint.findViewById(R.id.dialog_title);
        dialog_title.setText(getContext().getString(R.string.checkout_order_id, mOrder.getIncrementId()));

        if (!ConfigUtil.getTypePrint().equals(getContext().getString(R.string.print_type_star_print))) {
            WebView dialogWebView = (WebView) dialogPrint.findViewById(R.id.webview);
            dialogWebView.getSettings().setJavaScriptEnabled(true);
            dialogWebView.getSettings().setLoadsImagesAutomatically(true);
            dialogWebView.setDrawingCacheEnabled(true);
            dialogWebView.buildDrawingCache();
            dialogWebView.capturePicture();
            PrintUtil.doPrint(getContext(), mOrder, dialogWebView);

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
        } else {
            final LinearLayout rl = (LinearLayout) dialogPrint.findViewById(R.id.ll_content);
            rl.setDrawingCacheEnabled(true);

            ImageView im_logo = (ImageView) dialogPrint.findViewById(R.id.im_logo);
            if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getShowReceiptLogo())) {
                if (ConfigUtil.getConfigPrint().getShowReceiptLogo().equals("1") && !StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getPathLogo())) {
                    im_logo.setVisibility(VISIBLE);
                    Picasso.with(getContext()).load(ConfigUtil.getConfigPrint().getPathLogo()).into(im_logo);
                } else {
                    im_logo.setVisibility(GONE);
                }
            } else {
                im_logo.setVisibility(GONE);
            }
            TextView tv_header = (TextView) dialogPrint.findViewById(R.id.tv_header);
            if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getHeaderText())) {
                tv_header.setVisibility(VISIBLE);
                tv_header.setText(ConfigUtil.getConfigPrint().getHeaderText());
            } else {
                tv_header.setVisibility(GONE);
            }
            TextView tv_order_id = (TextView) dialogPrint.findViewById(R.id.tv_order_id);
            tv_order_id.setText(getContext().getString(R.string.checkout_order_id, mOrder.getIncrementId()));
            TextView tv_date_time = (TextView) dialogPrint.findViewById(R.id.tv_date_time);
            tv_date_time.setText(ConfigUtil.formatDateTime(mOrder.getCreatedAt()));
            TextView tv_cashier_name = (TextView) dialogPrint.findViewById(R.id.tv_cashier_name);
            if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getShowCashierName())) {
                if (ConfigUtil.getConfigPrint().getShowCashierName().equals("1") && !StringUtil.isNullOrEmpty(mOrder.getWebposStaffName())) {
                    tv_cashier_name.setVisibility(VISIBLE);
                    String cashier_title = getContext().getString(R.string.print_header_cashier) + " " + mOrder.getWebposStaffName().toUpperCase();
                    tv_cashier_name.setText(cashier_title);
                } else {
                    tv_cashier_name.setVisibility(GONE);
                }
            } else {
                tv_cashier_name.setVisibility(GONE);
            }
            TextView tv_customer_name = (TextView) dialogPrint.findViewById(R.id.tv_customer_name);
            if (mOrder.getBillingAddress() != null && !StringUtil.isNullOrEmpty(mOrder.getBillingAddress().getName())) {
                if (!mOrder.getBillingAddress().getName().equals(ConfigUtil.getCustomerGuest().getName())) {
                    tv_customer_name.setVisibility(VISIBLE);
                    String customer_name = getContext().getString(R.string.print_header_customer) + " " + mOrder.getBillingAddress().getName();
                    tv_customer_name.setText(customer_name);
                } else {
                    tv_customer_name.setVisibility(GONE);
                }
            } else {
                tv_customer_name.setVisibility(GONE);
            }

            TextView tv_shipping = (TextView) dialogPrint.findViewById(R.id.tv_shipping);
            if (!StringUtil.isNullOrEmpty(mOrder.getShippingDescription())) {
                tv_shipping.setVisibility(GONE);
                tv_shipping.setText(getContext().getString(R.string.print_header_shipping) + " " + mOrder.getShippingDescription());
            } else {
                tv_shipping.setVisibility(GONE);
            }

            StarPrintListItemPanel listItemPanel = (StarPrintListItemPanel) dialogPrint.findViewById(R.id.item_list_panel);
            listItemPanel.bindList(mOrder.getOrderItems());

            LinearLayout ll_subtotal = (LinearLayout) dialogPrint.findViewById(R.id.ll_subtotal);
            TextView tv_price_subtotal = (TextView) dialogPrint.findViewById(R.id.tv_price_subtotal);
            ll_subtotal.setVisibility(mOrder.getBaseSubtotalInclTax() > 0 ? VISIBLE : GONE);
            tv_price_subtotal.setText(ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseSubtotalInclTax())));

            LinearLayout ll_earn_point = (LinearLayout) dialogPrint.findViewById(R.id.ll_earn_point);
            TextView tv_price_earn_point = (TextView) dialogPrint.findViewById(R.id.tv_price_earn_point);
            ll_earn_point.setVisibility(mOrder.getRewardPointsEarn() != 0 ? VISIBLE : GONE);
            tv_price_earn_point.setText(mOrder.getRewardPointsEarn() + "");

            LinearLayout ll_spend_point = (LinearLayout) dialogPrint.findViewById(R.id.ll_spend_point);
            TextView tv_spend_point = (TextView) dialogPrint.findViewById(R.id.tv_spend_point);
            ll_spend_point.setVisibility(mOrder.getRewardPointsSpent() != 0 ? VISIBLE : GONE);
            tv_spend_point.setText(mOrder.getRewardPointsSpent() + "");

            LinearLayout ll_shipping = (LinearLayout) dialogPrint.findViewById(R.id.ll_shipping);
            TextView tv_price_shipping = (TextView) dialogPrint.findViewById(R.id.tv_price_shipping);
            ll_shipping.setVisibility(mOrder.getBaseShippingInclTax() > 0 ? VISIBLE : GONE);
            tv_price_shipping.setText(ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseShippingInclTax())));

            LinearLayout ll_tax = (LinearLayout) dialogPrint.findViewById(R.id.ll_tax);
            TextView tv_price_tax = (TextView) dialogPrint.findViewById(R.id.tv_price_tax);
            ll_tax.setVisibility(mOrder.getBaseTaxAmount() > 0 ? VISIBLE : GONE);
            tv_price_tax.setText(ConfigUtil.convertToPrice(mOrder.getBaseTaxAmount()) + "");

            LinearLayout ll_discount = (LinearLayout) dialogPrint.findViewById(R.id.ll_discount);
            TextView tv_price_discount = (TextView) dialogPrint.findViewById(R.id.tv_price_discount);
            ll_discount.setVisibility(mOrder.getBaseDiscountAmount() > 0 ? VISIBLE : GONE);
            tv_price_discount.setText(ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseDiscountAmount())));

            LinearLayout ll_gift_voucher = (LinearLayout) dialogPrint.findViewById(R.id.ll_gift_voucher);
            TextView tv_price_gift_voucher_discount = (TextView) dialogPrint.findViewById(R.id.tv_price_gift_voucher_discount);
            ll_gift_voucher.setVisibility(mOrder.getBaseGiftVoucherDiscount() > 0 ? VISIBLE : GONE);
            tv_price_gift_voucher_discount.setText(ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseGiftVoucherDiscount())));

            LinearLayout ll_reward_point = (LinearLayout) dialogPrint.findViewById(R.id.ll_reward_point);
            TextView tv_price_reward_point_discount = (TextView) dialogPrint.findViewById(R.id.tv_price_reward_point_discount);
            ll_reward_point.setVisibility(mOrder.getRewardPointsBaseDiscount() > 0 ? VISIBLE : GONE);
            tv_price_reward_point_discount.setText(ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getRewardPointsBaseDiscount())));

            TextView tv_price_grand_total = (TextView) dialogPrint.findViewById(R.id.tv_price_grand_total);
            tv_price_grand_total.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getBaseGrandTotal())));

            StarPrintListPaymentPanel listPaymentPanel = (StarPrintListPaymentPanel) dialogPrint.findViewById(R.id.item_payment_panel);
            List<OrderWebposPayment> listPayment = mOrder.getWebposOrderPayments();
//            if (listPayment != null && listPayment.size() > 0) {
//                listPaymentPanel.setVisibility(VISIBLE);
//                listPaymentPanel.bindList(listPayment);
//            } else {
            listPaymentPanel.setVisibility(GONE);
//            }

            LinearLayout ll_change = (LinearLayout) dialogPrint.findViewById(R.id.ll_change);
            TextView tv_price_change = (TextView) dialogPrint.findViewById(R.id.tv_price_change);
            ll_change.setVisibility(mOrder.getWebposBaseChange() > 0 ? VISIBLE : GONE);
            tv_price_change.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getWebposBaseChange())));

            LinearLayout ll_paid = (LinearLayout) dialogPrint.findViewById(R.id.ll_paid);
            TextView tv_price_paid = (TextView) dialogPrint.findViewById(R.id.tv_price_paid);
            ll_paid.setVisibility(mOrder.getBaseTotalPaid() > 0 ? VISIBLE : GONE);
            tv_price_paid.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getBaseTotalPaid())));

            LinearLayout ll_due = (LinearLayout) dialogPrint.findViewById(R.id.ll_due);
            TextView tv_price_due = (TextView) dialogPrint.findViewById(R.id.tv_price_due);
            ll_due.setVisibility(mOrder.getBaseTotalDue() > 0 ? VISIBLE : GONE);
            tv_price_due.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getBaseTotalDue())));

            TextView tv_comment_title = (TextView) dialogPrint.findViewById(R.id.tv_comment_title);
            StarPrintListCommentPanel listCommentPanel = (StarPrintListCommentPanel) dialogPrint.findViewById(R.id.item_comment_panel);
            List<OrderStatus> listComment = mOrder.getOrderStatus();
            if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getShowComment())) {
                if (ConfigUtil.getConfigPrint().getShowComment().equals("1") && listComment != null && listComment.size() > 0) {
                    tv_comment_title.setVisibility(VISIBLE);
                    listCommentPanel.setVisibility(VISIBLE);
                    listCommentPanel.bindList(listComment);
                } else {
                    tv_comment_title.setVisibility(GONE);
                    listCommentPanel.setVisibility(GONE);
                }
            } else {
                tv_comment_title.setVisibility(GONE);
                listCommentPanel.setVisibility(GONE);
            }

            TextView tv_footer = (TextView) dialogPrint.findViewById(R.id.tv_footer);
            if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getFooterText())) {
                tv_footer.setText(ConfigUtil.getConfigPrint().getFooterText());
            } else {
                tv_footer.setText(getContext().getString(R.string.print_footer_default));
            }

            bt_print.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap bitmap = rl.getDrawingCache();
                    StarPrintUtitl.showSearchPrint(getContext(), ((OrderHistoryListController) mController).getMagestoreContext().getActivity(), bitmap);
                }
            });
        }

        dialogPrint.show();

        dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPrint.dismiss();
            }
        });
    }

    /*Felix 4-4-2017 start*/
    public void fillEmailCustomer(Order order) {
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

    public void showDetailOrderLoading(boolean visible) {
        email_checkout_loading.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

     /*Felix 4-4-2017 end*/
}
