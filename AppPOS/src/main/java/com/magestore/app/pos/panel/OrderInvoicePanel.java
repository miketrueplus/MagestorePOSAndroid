package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderInvoiceItemsListController;
import com.magestore.app.pos.databinding.PanelOrderInvoiceBinding;
import com.magestore.app.util.DialogUtil;

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
        View view = inflate(getContext(), R.layout.panel_order_invoice, null);
        addView(view);

        cb_send_email = (CheckBox) view.findViewById(R.id.cb_send_email);

        invoice_comment = (EditText) view.findViewById(R.id.invoice_comment);

        mBinding = DataBindingUtil.bind(view);

        mOrderInvoiceItemsListPanel = (OrderInvoiceItemsListPanel) findViewById(R.id.order_invoice_items);

        initModel();
    }

    @Override
    public void initModel() {
        Controller controller = getController();

        mOrderInvoiceItemsListController = new OrderInvoiceItemsListController();
        mOrderInvoiceItemsListController.setView(mOrderInvoiceItemsListPanel);

        if (controller instanceof OrderHistoryListController)
            mOrderInvoiceItemsListController.setOrderService(((OrderHistoryListController) controller).getOrderService());
    }

    @Override
    public void bindItem(Order item) {
        if (item == null) return;
        super.bindItem(item);
        mBinding.setOrderDetail(item);
        mOrder = item;
        mOrderInvoiceItemsListController.doSelectOrder(item);
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
        orderInvoiceParams.setShippingAddressId(mOrder.getBillingAddressId());
        orderInvoiceParams.setShippingAmount(mOrder.getShippingAmount());
        orderInvoiceParams.setShippingInclTax(mOrder.getShippingInclTax());
        orderInvoiceParams.setShippingTaxAmount(mOrder.getShippingTaxAmount());
        orderInvoiceParams.setState(mOrder.getState());
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

    public void showAlertRespone() {
        String message = getContext().getString(R.string.order_invoice_success);

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}