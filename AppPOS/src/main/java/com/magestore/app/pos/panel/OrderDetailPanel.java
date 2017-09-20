package com.magestore.app.pos.panel;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderItemUpdateQtyParam;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.PrintDialogActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.databinding.PanelOrderDetailBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.util.PrintUtil;
import com.magestore.app.pos.util.StarPrintExtUtil;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class OrderDetailPanel extends AbstractDetailPanel<Order> {
    View v;
    Order mOrder;
    Button btn_invoice, btn_print;
    MagestoreDialog dialog;
    PanelOrderDetailBinding mBinding;

    FrameLayout fr_detail_bottom_left, fr_detail_bottom_right;
    ImageView im_status;
    TextView status, btn_take_payment, tv_information;

    RelativeLayout detail_order_loading, rl_take_payment;

    public OrderDetailPanel(Context context) {
        super(context);
    }

    public OrderDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        // Load layout view danh sách khách hàng
        v = inflate(getContext(), R.layout.panel_order_detail, null);
        addView(v);

        rl_take_payment = (RelativeLayout) v.findViewById(R.id.rl_take_payment);
        btn_take_payment = (TextView) v.findViewById(R.id.btn_take_payment);

        btn_invoice = (Button) v.findViewById(R.id.btn_invoice);
        btn_print = (Button) v.findViewById(R.id.btn_print);

        fr_detail_bottom_left = (FrameLayout) v.findViewById(R.id.fr_detail_bottom_left);

        fr_detail_bottom_right = (FrameLayout) v.findViewById(R.id.fr_detail_bottom_right);

        detail_order_loading = (RelativeLayout) v.findViewById(R.id.detail_order_loading);

        tv_information = (TextView) v.findViewById(R.id.tv_information);

        btn_print.setVisibility(ConfigUtil.isPrintSession() ? VISIBLE : GONE);

        mBinding = DataBindingUtil.bind(v);
    }

    /**
     * Chuẩn bị các model, controller
     */
    @Override
    public void initModel() {
    }

    private void initControlValue() {

    }

    private void initTask() {

    }

    @Override
    public void bindItem(Order item) {
        super.bindItem(item);
        mOrder = item;
        mBinding.setOrderDetail(item);
        if (checkCanInvoice(mOrder)) {
            btn_invoice.setVisibility(VISIBLE);
            fr_detail_bottom_right.setVisibility(GONE);
            fr_detail_bottom_left.setVisibility(GONE);
        } else {
            btn_invoice.setVisibility(GONE);
            fr_detail_bottom_right.setVisibility(VISIBLE);
            fr_detail_bottom_left.setVisibility(VISIBLE);
        }

        ((OrderHistoryListController) mController).getOrderPaymentListController().doSelectOrder(item);
        ((OrderHistoryListController) mController).getOrderCommentListController().doSelectOrder(item);
        ((OrderHistoryListController) mController).getOrderHistoryItemsListController().doSelectOrder(item);

        String information = "";
        if (!StringUtil.isNullOrEmpty(item.getStatus())) {
            information = StringUtil.capitalizedString(item.getStatus());
        }
        if (!StringUtil.isNullOrEmpty(item.getCreatedAt())) {
            information = information + " - " + ConfigUtil.formatDate(item.getCreatedAt()) + " - " + ConfigUtil.formatTime(item.getCreatedAt());
        }
        if (!StringUtil.isNullOrEmpty(item.getWebposStaffName())) {
            information = information + " - " + item.getWebposStaffName();
        }
        tv_information.setText(information);

        status = (TextView) v.findViewById(R.id.status);
        im_status = (ImageView) v.findViewById(R.id.im_status);
        im_status.setImageResource(R.drawable.ic_order_status);

        String item_status = item.getStatus().toLowerCase();
        changeStatusTopOrder(item_status);
        changeColorStatusOrder(item_status);

        btn_print.setVisibility(ConfigUtil.isPrintSession() ? VISIBLE : GONE);
        rl_take_payment.setVisibility(checkCanTakePayment(mOrder) ? VISIBLE : GONE);

        btn_invoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInvoice();
            }
        });

        btn_take_payment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTakePayment();
            }
        });

        btn_print.setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                actionPrint();
            }
        });
    }

    public void changeColorStatusOrder(String status) {
        switch (status) {
            case "pending":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_pending));
                break;
            case "processing":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_processing));
                break;
            case "complete":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_complete));
                break;
            case "canceled":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_cancelled));
                break;
            case "closed":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_closed));
                break;
            case "not_sync":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_notsync));
                break;
        }
    }

    public void setOrder(Order mOrder) {
        this.mOrder = mOrder;
    }

    public Order getOrder() {
        return mOrder;
    }

    public void changeStatusTopOrder(String item_status) {
        status.setText(item_status);
    }

    public void bindDataRespone(Order order) {
        mBinding.setOrderDetail(order);
        if (checkCanInvoice(order)) {
            btn_invoice.setVisibility(VISIBLE);
            fr_detail_bottom_right.setVisibility(GONE);
            fr_detail_bottom_left.setVisibility(GONE);
        } else {
            btn_invoice.setVisibility(GONE);
            fr_detail_bottom_right.setVisibility(VISIBLE);
            fr_detail_bottom_left.setVisibility(VISIBLE);
        }
        rl_take_payment.setVisibility(checkCanTakePayment(order) ? VISIBLE : GONE);
    }

    public void showPopupMenu(View view) {
        if (mOrder == null) {
            return;
        }
        View menuItemView = view.findViewById(R.id.order_action);
        PopupMenu popupMenu = new PopupMenu(getContext(), menuItemView);
        popupMenu.inflate(R.menu.order_action);
        String status = mOrder.getStatus().toLowerCase();
        checkShowItemMenu(mOrder, status, popupMenu);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_send_email:
                        onClickSendEmail();
                        return true;
                    case R.id.action_ship:
                        onClickShipment();
                        return true;
                    case R.id.action_cancel:
                        onClickCancel();
                        return true;
                    case R.id.action_add_comment:
                        onClickAddComment();
                        return true;
                    case R.id.action_re_order:
                        onClickReorder();
                        return true;
                    case R.id.action_refund:
                        onClickRefund();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void checkShowItemMenu(Order order, String status, PopupMenu popupMenu) {
        if (ConfigUtil.isSendEmail()) {
            checkVisibleItemMenu(popupMenu, R.id.action_send_email, "pending".equals(status) | "processing".equals(status) | "complete".equals(status) | "not_sync".equals(status));
        } else {
            checkVisibleItemMenu(popupMenu, R.id.action_send_email, false);
        }
        if (ConfigUtil.isShip()) {
            checkVisibleItemMenu(popupMenu, R.id.action_ship, ((OrderHistoryListController) mController).checkCanShip(order));
        } else {
            checkVisibleItemMenu(popupMenu, R.id.action_ship, false);
        }
        if (ConfigUtil.isCancel()) {
            checkVisibleItemMenu(popupMenu, R.id.action_cancel, ((OrderHistoryListController) mController).checkCanCancel(order));
        } else {
            checkVisibleItemMenu(popupMenu, R.id.action_cancel, false);
        }
        if (ConfigUtil.isAddComment()) {
            checkVisibleItemMenu(popupMenu, R.id.action_add_comment, "pending".equals(status) | "processing".equals(status) | "complete".equals(status) | "canceled".equals(status) | "not_sync".equals(status));
        } else {
            checkVisibleItemMenu(popupMenu, R.id.action_add_comment, false);
        }
        if (ConfigUtil.isReOder()) {
            checkVisibleItemMenu(popupMenu, R.id.action_re_order, "pending".equals(status) | "processing".equals(status) | "complete".equals(status) | "canceled".equals(status) | "closed".equals(status) | "not_sync".equals(status));
        } else {
            checkVisibleItemMenu(popupMenu, R.id.action_re_order, false);
        }
        if (ConfigUtil.isCanUseRefund()) {
            checkVisibleItemMenu(popupMenu, R.id.action_refund, ((OrderHistoryListController) mController).checkCanRefund(order));
        } else {
            checkVisibleItemMenu(popupMenu, R.id.action_refund, false);
        }
    }

    private void checkVisibleItemMenu(PopupMenu popupMenu, int id, boolean check) {
        popupMenu.getMenu().findItem(id).setVisible(check);
    }

    private boolean checkCanInvoice(Order order) {
        return ((OrderHistoryListController) mController).checkCanInvoice(order);
    }

    private boolean checkCanTakePayment(Order order) {
        return ((OrderHistoryListController) mController).checkCanTakePayment(order);
    }

    private void onClickSendEmail() {
        final OrderSendEmailPanel mOrderSendEmailPanel = new OrderSendEmailPanel(getContext());
        mOrderSendEmailPanel.setController(mController);
        mOrderSendEmailPanel.bindItem(mOrder);
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_send_email_title), mOrderSendEmailPanel);
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width));
        dialog.show();

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSendEmail(mOrderSendEmailPanel);
                dialog.dismiss();
            }
        });
    }

    private void actionSendEmail(OrderSendEmailPanel mOrderSendEmailPanel) {
        Order order = mOrderSendEmailPanel.bind2Item();
        String email = order.getCustomerEmail();
        String orderId = order.getID();

        if (TextUtils.isEmpty(email)) {
            mOrderSendEmailPanel.showAlertEmail();
            return;
        }

        if (StringUtil.checkEmail(email)) {
            mOrderSendEmailPanel.showRequiedEmail();
            return;
        }

        ((OrderHistoryListController) mController).setOrderSendEmailPanel(mOrderSendEmailPanel);
        Map<String, Object> paramSendEmail = new HashMap<>();
        paramSendEmail.put("email", email);
        paramSendEmail.put("order_id", orderId);
        ((OrderHistoryListController) mController).doInputSendEmail(paramSendEmail);
    }

    private void onClickAddComment() {
        final OrderAddCommentPanel mOrderAddCommentPanel = new OrderAddCommentPanel(getContext());
        mOrderAddCommentPanel.setController(mController);
        mOrderAddCommentPanel.bindItem(mOrder);
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_add_comment_title), mOrderAddCommentPanel);
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width));
        dialog.show();

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionAddComment(mOrderAddCommentPanel);
                dialog.dismiss();
            }
        });
    }

    private void actionAddComment(OrderAddCommentPanel mOrderAddCommentPanel) {
        Order order = mOrderAddCommentPanel.bind2Item();
        String comment = order.getParamStatus().getComment();

        if (TextUtils.isEmpty(comment)) {
            mOrderAddCommentPanel.showAlertComment();
            return;
        }

        ((OrderHistoryListController) mController).setOrderAddCommentPanel(mOrderAddCommentPanel);
        ((OrderHistoryListController) mController).doInputInsertStatus(order);
    }

    private void onClickShipment() {
        final OrderShipmentPanel mOrderShipmentPanel = new OrderShipmentPanel(getContext());
        mOrderShipmentPanel.setController(mController);
        mOrderShipmentPanel.initModel();
        mOrderShipmentPanel.bindItem(mOrder);
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_shipment_title), mOrderShipmentPanel);
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.order_dialog_ship_width));
        dialog.setGoneButtonSave(true);
        dialog.show();

        Button btn_submit_shipment = (Button) dialog.findViewById(R.id.btn_submit_shipment);
        btn_submit_shipment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = mOrderShipmentPanel.bind2Item();
                ((OrderHistoryListController) mController).setOrderShipmentPanel(mOrderShipmentPanel);
                ((OrderHistoryListController) mController).doInputCreateShipment(order);
                dialog.dismiss();
            }
        });
    }

    private void onClickRefund() {
        final OrderRefundPanel mOrderRefundPanel = new OrderRefundPanel(getContext());
        ((OrderHistoryListController) mController).setOrderRefundPanel(mOrderRefundPanel);
        mOrderRefundPanel.setController(mController);
        mOrderRefundPanel.initModel();
        mOrderRefundPanel.bindItem(mOrder);
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_refund_title), mOrderRefundPanel);
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.order_dialog_refund_width));
        dialog.setDialogSave(getContext().getString(R.string.order_refund_btn_save));
        dialog.show();
        Button btn_submit_refund = (Button) dialog.findViewById(R.id.btn_submit_refund);
        btn_submit_refund.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String customer_id = mOrder.getCustomerId();
                Order order = mOrderRefundPanel.bind2Item();
                if (!StringUtil.isNullOrEmpty(customer_id) && !customer_id.equals(ConfigUtil.getCustomerGuest().getID())) {
                    if (ConfigUtil.isEnableGiftCard() && ConfigUtil.isEnableStoreCredit()) {
                        if (mOrder.getStoreCreditRefund() <= mOrder.getMaxStoreCreditRefund()) {
                            if (order.getStoreCreditRefund() > 0) {
                                ((OrderHistoryListController) mController).doInputRefundByCredit(order);
                            }
                            if (mOrder.getBaseGiftVoucherDiscount() != 0) {
                                ((OrderHistoryListController) mController).doInputRefundByGiftCard(order);
                            }
                            ((OrderHistoryListController) mController).doInputRefund(order);
                            dialog.dismiss();
                        } else {
                            String message = getContext().getString(R.string.order_refund_limit, ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getMaxRefunded())));
                            // Tạo dialog và hiển thị
                            com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.done);
                        }
                    } else if (ConfigUtil.isEnableGiftCard()) {
                        if (mOrder.getBaseGiftVoucherDiscount() != 0) {
                            ((OrderHistoryListController) mController).doInputRefundByGiftCard(order);
                        }
                        ((OrderHistoryListController) mController).doInputRefund(order);
                        dialog.dismiss();
                    } else if (ConfigUtil.isEnableStoreCredit()) {
                        if (mOrder.getStoreCreditRefund() <= mOrder.getMaxStoreCreditRefund()) {
                            if (order.getStoreCreditRefund() > 0) {
                                ((OrderHistoryListController) mController).doInputRefundByCredit(order);
                            }
                            ((OrderHistoryListController) mController).doInputRefund(order);
                            dialog.dismiss();
                        } else {
                            String message = getContext().getString(R.string.order_refund_limit, ConfigUtil.formatPrice(mOrder.getMaxRefunded()));
                            // Tạo dialog và hiển thị
                            com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.done);
                        }
                    } else {
                        ((OrderHistoryListController) mController).doInputRefund(order);
                    }
                } else {
                    float total_price_qty_item = order.getTotalPriceChangeQtyRefund();
                    float price_shipping = order.getRefundShipping();
                    float adjust_refund = order.getAdjustRefund();
                    float adjust_free = order.getAdjustFree();
                    float max_store_credit = ((total_price_qty_item + price_shipping + adjust_refund) - (adjust_free + order.getMaxGiftCardRefund()));
                    if (max_store_credit <= mOrder.getMaxRefunded()) {
                        ((OrderHistoryListController) mController).doInputRefund(order);
                        dialog.dismiss();
                    } else {
                        String message = getContext().getString(R.string.order_refund_limit, ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getMaxRefunded())));
                        // Tạo dialog và hiển thị
                        com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.done);
                    }
                }
            }
        });
    }

    private void onClickInvoice() {
        float invoiceable = mOrder.getBaseTotalPaid() - mOrder.getBaseTotalInvoiced() - mOrder.getWebposBaseChange();
        if (invoiceable == 0 && mOrder.getBaseGrandTotal() > 0) {
            String message = getContext().getString(R.string.order_invoice_take_payment);
            // Tạo dialog và hiển thị
            com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
            return;
        }
        final OrderInvoicePanel mOrderInvoicePanel = new OrderInvoicePanel(getContext());
        mOrderInvoicePanel.setController(mController);
        mOrderInvoicePanel.initModel();
        ((OrderHistoryListController) mController).setOrderInvoicePanel(mOrderInvoicePanel);
        mOrderInvoicePanel.bindItem(mOrder);

        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_invoice_title), mOrderInvoicePanel);
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.order_dialog_invoice_width));
        dialog.setGoneButtonSave(true);
        dialog.show();

        Button btn_submit_invoice = (Button) dialog.findViewById(R.id.btn_submit_invoice);
        Button btn_update_qty = (Button) dialog.findViewById(R.id.btn_update_qty);

        btn_update_qty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderUpdateQtyParam orderUpdateQtyParam = mOrderInvoicePanel.bindOrderUpdateQty();
                if (checkTotalUpdateInvoice(orderUpdateQtyParam)) {
                    ((OrderHistoryListController) mController).doInputInvoiceUpdateQty(orderUpdateQtyParam);
                } else {
                    String message = getContext().getString(R.string.order_invoice_cannot_update);
                    // Tạo dialog và hiển thị
                    com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
                }
            }
        });

        btn_submit_invoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkItemInvoice(mOrder)) {
                    Order order = mOrderInvoicePanel.bind2Item();
                    ((OrderHistoryListController) mController).doInputInvoice(order);
                    dialog.dismiss();
                } else {
                    String message = getContext().getString(R.string.order_invoice_choose_item);
                    // Tạo dialog và hiển thị
                    com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
                }
            }
        });
    }

    private boolean checkItemInvoice(Order order) {
        boolean checkItem = false;
        for (CartItem item : order.getOrderItems()) {
            if (item.getQuantity() > 0) {
                checkItem = true;
            }
        }
        return checkItem;
    }

    private boolean checkTotalUpdateInvoice(OrderUpdateQtyParam orderUpdateQtyParam) {
        List<OrderItemUpdateQtyParam> listItem = orderUpdateQtyParam.getItems();
        float total_invoice = 0;
        for (OrderItemUpdateQtyParam item : listItem) {
            total_invoice += item.getTotalInvoice();
        }
        if (total_invoice > orderUpdateQtyParam.getTotalPaid()) {
            return false;
        }
        return true;
    }

    private void onClickCancel() {
        final OrderCancelPanel mOrderCancelPanel = new OrderCancelPanel(getContext());
        mOrderCancelPanel.setController(mController);
        mOrderCancelPanel.bindItem(mOrder);

        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_cancel_title), mOrderCancelPanel);
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width));
        dialog.show();

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.confirm(getContext(), getContext().getString(R.string.ask_are_you_sure_to_cancel_order), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Order order = mOrderCancelPanel.bind2Item();
                        ((OrderHistoryListController) mController).setOrderCancelPanel(mOrderCancelPanel);
                        ((OrderHistoryListController) mController).doInputCancel(order);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void onClickReorder() {
        ((OrderHistoryListController) mController).doInputReorder(mOrder);
    }

    private void onClickTakePayment() {
        OrderTakePaymentPanel mOrderTakePaymentPanel = new OrderTakePaymentPanel(getContext());
        ((OrderHistoryListController) mController).resetListChoosePayment();
        ((OrderHistoryListController) mController).setOrderTakePaymentPanel(mOrderTakePaymentPanel);
        mOrderTakePaymentPanel.setController(mController);
        mOrderTakePaymentPanel.initModel();
        mOrderTakePaymentPanel.bindItem(mOrder);

        final MagestoreDialog dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_detail_top_take_payment), mOrderTakePaymentPanel);
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.order_dialog_take_payment_width));
        dialog.setDialogSave(getContext().getString(R.string.submit));
        dialog.show();

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OrderHistoryListController) mController).doInputTakePayment(mOrder);

                if (((OrderHistoryListController) mController).
                        checkDimissDialogTakePayment(mOrder)) {
                    dialog.dismiss();
                }

            }
        });
    }

    private void actionPrint() {
        final Dialog dialogPrint = new Dialog(getContext());
        dialogPrint.setCancelable(true);
        dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPrint.setFeatureDrawableAlpha(1, 1);
        if (ConfigUtil.getTypePrint().equals(getContext().getString(R.string.print_type_star_print))) {
            dialogPrint.setContentView(R.layout.star_print_order_layout);
            ViewGroup.LayoutParams params = dialogPrint.getWindow().getAttributes();
            params.width = ConfigUtil.getStarPrintArea() + 300;
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
            if (mOrder.getOrderItems() != null && mOrder.getOrderItems().size() > 0) {
                List<CartItem> listOrder = new ArrayList<>();
                for (CartItem item : mOrder.getOrderItems()) {
                    if (item.getOrderParentItem() == null) {
                        listOrder.add(item);
                    }
                }
                listItemPanel.bindList(listOrder);
            }

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
                    StarPrintExtUtil.showSearchPrint(getContext(), ((OrderHistoryListController) mController).getMagestoreContext().getActivity(), bitmap, mOrder);
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

    /* Felix 3/4/2017 Start */
    public void showDetailOrderLoading(boolean visible) {
        detail_order_loading.setVisibility(visible ? VISIBLE : INVISIBLE);
    }
     /* Felix 3/4/2017 End */

    public void showErrorRefund(int type) {
        String message;
        if (type == 0) {
            message = getContext().getString(R.string.err_refund_giftcard);
        } else {
            message = getContext().getString(R.string.err_refund_store_credit);
        }
        // Tạo dialog và hiển thị
        com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
    }
}
