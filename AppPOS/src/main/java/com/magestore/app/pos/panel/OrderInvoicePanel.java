package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderItemUpdateQtyParam;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderInvoiceItemsListController;
import com.magestore.app.pos.databinding.PanelOrderInvoiceBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderInvoicePanel extends AbstractDetailPanel<Order> {
    PanelOrderInvoiceBinding mBinding;
    Order mOrder;
    OrderInvoiceItemsListPanel mOrderInvoiceItemsListPanel;
    OrderInvoiceItemsListController mOrderInvoiceItemsListController;
    CheckBox cb_send_email;
    EditText invoice_comment;
    Button btn_update_qty, btn_submit_invoice;
    View view;
    float total_price = 0;
    boolean check_request_update_invoice = false;
    TextView invoice_grandtotal, invoice_discount, invoice_tax, invoice_shipping, invoice_subtotal;

    public OrderInvoicePanel(Context context) {
        super(context);
    }

    public OrderInvoicePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderInvoicePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        view = inflate(getContext(), R.layout.panel_order_invoice, null);
        addView(view);

        cb_send_email = (CheckBox) view.findViewById(R.id.cb_send_email);

        invoice_comment = (EditText) view.findViewById(R.id.invoice_comment);

        mBinding = DataBindingUtil.bind(view);

        mOrderInvoiceItemsListPanel = (OrderInvoiceItemsListPanel) findViewById(R.id.order_invoice_items);

        invoice_grandtotal = (TextView) findViewById(R.id.invoice_grandtotal);
        invoice_discount = (TextView) findViewById(R.id.invoice_discount);
        invoice_tax = (TextView) findViewById(R.id.invoice_tax);
        invoice_shipping = (TextView) findViewById(R.id.invoice_shipping);
        invoice_subtotal = (TextView) findViewById(R.id.invoice_subtotal);

        btn_update_qty = (Button) view.findViewById(R.id.btn_update_qty);
        btn_submit_invoice = (Button) view.findViewById(R.id.btn_submit_invoice);
        initModel();
    }

    @Override
    public void initModel() {
        Controller controller = getController();

        mOrderInvoiceItemsListController = new OrderInvoiceItemsListController();
        mOrderInvoiceItemsListController.setView(mOrderInvoiceItemsListPanel);
        mOrderInvoiceItemsListController.setOrderInvoicePanel(this);

        if (controller instanceof OrderHistoryListController)
            mOrderInvoiceItemsListController.setOrderService(((OrderHistoryListController) controller).getOrderService());
    }

    @Override
    public void bindItem(Order item) {
        if (item == null) return;
        super.bindItem(item);
        mOrderInvoiceItemsListPanel.setOrder(item);
        mOrderInvoiceItemsListController.doSelectOrder(item);
        mBinding.setOrderDetail(item);
        mOrder = item;
        if (!mOrder.checkRequestUpdateInvoice()) {
            requestUpdateQtyTotalDue();
        }
    }

    public void bindTotal(Order item) {
        isShowButtonUpdateQty(false);
        isEnableButtonSubmitInvoice(true);
        ((OrderHistoryListController) getController()).setTotalOrder(item, mOrder);
        invoice_grandtotal.setText(ConfigUtil.formatPrice(item.getGrandTotal()));
        invoice_discount.setText(ConfigUtil.formatPrice(item.getDiscountAmount()));
        invoice_tax.setText(ConfigUtil.formatPrice(item.getTaxAmount()));
        invoice_shipping.setText(ConfigUtil.formatPrice(item.getShippingAmount()));
        invoice_subtotal.setText(ConfigUtil.formatPrice(item.getOrderHistorySubtotal()));
    }

    @Override
    public Order bind2Item() {
        boolean isSendEmail = cb_send_email.isChecked();

        OrderHistoryListController orderHistoryListController = ((OrderHistoryListController) mController);
        OrderInvoiceParams orderInvoiceParams = orderHistoryListController.createOrderInvoiceParams();

        orderInvoiceParams.setOrderId(mOrder.getID());
        if (isSendEmail) {
            orderInvoiceParams.setEmailSent("1");
        } else {
            orderInvoiceParams.setEmailSent("0");
        }

        orderInvoiceParams.setBaseCurrencyCode(mOrder.getBaseCurrencyCode());
        orderInvoiceParams.setBaseDiscountAmount(mOrder.getBaseDiscountAmount());
        orderInvoiceParams.setBaseGrandTotal(mOrder.getBaseGrandTotal());
        orderInvoiceParams.setBaseShippingAmount(mOrder.getBaseShippingAmount());
        orderInvoiceParams.setBaseShippingInclTax(mOrder.getBaseShippingInclTax());
        orderInvoiceParams.setBaseShippingTaxAmount(mOrder.getBaseShippingTaxAmount());
        orderInvoiceParams.setBaseSubtotal(mOrder.getBaseSubtotal());
        orderInvoiceParams.setBaseSubtotalInclTax(mOrder.getBaseSubtotalInclTax());
        orderInvoiceParams.setTaxAmount(mOrder.getTaxAmount());
        orderInvoiceParams.setBaseToGlobalRate(mOrder.getBaseToGlobalRate());
        orderInvoiceParams.setBaseToOrderRate(mOrder.getBaseToOrderRate());
        orderInvoiceParams.setBillingAddressId(mOrder.getBillingAddressId());

        String comment = invoice_comment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            OrderCommentParams commentParams = orderHistoryListController.createCommentParams();
            commentParams.setComment(comment);
            commentParams.setIsVisibleOnFront("1");
            List<OrderCommentParams> listComment = orderHistoryListController.createListComment();
            listComment.add(commentParams);
            orderInvoiceParams.setComments(listComment);
        }

        orderInvoiceParams.setDiscountAmount(mOrder.getDiscountAmount());
        orderInvoiceParams.setGlobalCurrencyCode(mOrder.getGlobalCurrencyCode());
        orderInvoiceParams.setGrandTotal(mOrder.getGrandTotal());

        orderInvoiceParams.setItems(mOrderInvoiceItemsListPanel.bind2List());

        orderInvoiceParams.setOrderCurrencyCode(mOrder.getOrderCurrencyCode());
        // TODO: check shipping address id
        orderInvoiceParams.setShippingAddressId(mOrder.getBillingAddressId());
        orderInvoiceParams.setShippingAmount(mOrder.getShippingAmount());
        orderInvoiceParams.setShippingInclTax(mOrder.getShippingInclTax());
        orderInvoiceParams.setShippingTaxAmount(mOrder.getShippingTaxAmount());
        // TODO: check state
        orderInvoiceParams.setState("2");
        orderInvoiceParams.setStoreCurrencyCode(mOrder.getStoreCurrencyCode());
        orderInvoiceParams.setStoreId(mOrder.getStoreId());
        orderInvoiceParams.setStoreToBaseRate(mOrder.getStoreToBaseRate());
        orderInvoiceParams.setStoreToOrderRate(mOrder.getStoreToOrderRate());
        orderInvoiceParams.setSubtotal(mOrder.getOrderHistorySubtotal());
        orderInvoiceParams.setSubtotalInclTax(mOrder.getSubtotalInclTax());
        orderInvoiceParams.setTaxAmount(mOrder.getTaxAmount());
        orderInvoiceParams.setTotalQty(mOrder.getTotalQtyOrdered());

        mOrder.setParamInvoice(orderInvoiceParams);
        return mOrder;
    }

    public void requestUpdateQtyTotalDue() {
        if (mOrder.getTotalDue() > 0) {
            OrderUpdateQtyParam orderUpdateQtyParam = bindOrderUpdateQty();
            if (check_request_update_invoice) {
                ((OrderHistoryListController) getController()).doInputInvoiceUpdateQty(orderUpdateQtyParam);
            } else {
                mOrder.setBaseSubtotalInclTax(0);
                mOrder.setBaseShippingInclTax(0);
                mOrder.setBaseTaxAmount(0);
                mOrder.setBaseDiscountAmount(0);
                mOrder.setBaseGrandTotal(0);
                mBinding.setOrderDetail(mOrder);
            }
        }
    }

    public OrderUpdateQtyParam bindOrderUpdateQty() {
        OrderHistoryListController orderHistoryListController = ((OrderHistoryListController) mController);
        OrderUpdateQtyParam mOrderUpdateQtyParam = orderHistoryListController.createOrderUpdateQtyParam();
        mOrderUpdateQtyParam.setOrderId(mOrder.getID());
        List<OrderItemUpdateQtyParam> listOrderItem = new ArrayList<>();
        setDataToOrderUpdateQty(listOrderItem, orderHistoryListController);
        mOrderUpdateQtyParam.setItems(listOrderItem);
        return mOrderUpdateQtyParam;
    }

    public void setDataToOrderUpdateQty(List<OrderItemUpdateQtyParam> listOrderItem, OrderHistoryListController orderHistoryListController) {
        List<CartItem> listItem;
        if (!mOrder.checkRequestUpdateInvoice()) {
            listItem = listItemInvoice();
            mOrder.setCheckRequestUpdateInvoice(true);
        } else {
            listItem = mOrderInvoiceItemsListPanel.bind2List();
        }
        for (CartItem cartItem : listItem) {
            OrderItemUpdateQtyParam item = orderHistoryListController.creaOrderItemUpdateQtyParam();
            item.setEntityId(cartItem.getItemId());
            item.setQty(cartItem.getQuantity());
            listOrderItem.add(item);
        }
    }

    private List<CartItem> listItemInvoice() {
        List<CartItem> listCartItem = new ArrayList<>();
        for (CartItem item : mOrderInvoiceItemsListController.getListItems()) {
            CartItem nitem = checkQtyInvoice(item);
            if (nitem.getQuantity() > 0) {
                check_request_update_invoice = true;
            }
            listCartItem.add(nitem);
        }
        return listCartItem;
    }

    public CartItem checkQtyInvoice(CartItem item) {
        if (item.getPriceInvoice() == 0) {
            item.setPriceInvoice(item.getPrice());
        }
        float total_paid = (mOrder.getTotalPaid() - mOrder.getBaseTotalInvoiced() - mOrder.getWebposBaseChange() - mOrder.getBaseTotalRefunded());
        if (total_price < total_paid) {
            if (item.QtyInvoice() > 0) {
                int qty = 0;
                float total_invoice = 0;

                while (total_invoice < total_paid) {
                    total_invoice += qty * (item.getPriceInvoice() - item.getBaseDiscountAmount() - item.getBaseGiftVoucherDiscount() - item.getRewardpointsBaseDiscount());
                    total_price = total_invoice + total_price;
                    if (total_invoice > total_paid) {
                        break;
                    }
                    qty++;
                }
                item.setQtyInvoiceable(qty - 1);
                item.setQuantity(qty - 1);
            }
        } else {
            item.setQtyInvoiceable(0);
            item.setQuantity(0);
        }

        return item;
    }

    public void showAlertRespone() {
        String message = getContext().getString(R.string.order_invoice_success);

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.ok);
    }

    public void isShowButtonUpdateQty(boolean isShow) {
        btn_update_qty.setVisibility(isShow ? VISIBLE : GONE);
    }

    //Felix edit 03/05/2017
    public void isEnableButtonSubmitInvoice(boolean isEnable) {
        btn_submit_invoice.setEnabled(isEnable ? true : false);
        btn_submit_invoice.setBackground(isEnable ? ContextCompat.getDrawable(getContext(), R.drawable.order_history_invoice_button_submit) : ContextCompat.getDrawable(getContext(), R.drawable.order_history_invoice_button_submit_disable));
        btn_submit_invoice.setTextColor(isEnable ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
    }
    //end Felix edit 03/05/2017

}
