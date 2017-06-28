package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.panel.OrderInvoiceItemsListPanel;
import com.magestore.app.pos.panel.OrderInvoicePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderInvoiceItemsListController extends AbstractListController<CartItem> implements ListController<CartItem> {
    Order mSelectedOrder;
    OrderHistoryListController mOrderHistoryListController;
    OrderHistoryService mOrderService;
    OrderInvoicePanel mOrderInvoicePanel;

    public void setOrderInvoicePanel(OrderInvoicePanel mOrderInvoicePanel) {
        this.mOrderInvoicePanel = mOrderInvoicePanel;
    }

    /**
     * Thiết lập controller
     *
     * @param mOrderHistoryListController
     */
    public void setOrderHistoryListController(OrderHistoryListController mOrderHistoryListController) {
        this.mOrderHistoryListController = mOrderHistoryListController;
//        setParentController(mOrderHistoryListController);
    }

    /**
     * Thiết lập service
     *
     * @param mOrderService
     */
    public void setOrderService(OrderHistoryService mOrderService) {
        this.mOrderService = mOrderService;
    }

    /**
     * Thiết lập 1 order cho comment
     *
     * @param order
     */
    public void doSelectOrder(Order order) {
        mSelectedOrder = order;
        mList = checkParentItem(order);
        ((OrderInvoiceItemsListPanel) mView).setDataListItem(mList);
        mView.bindList(mList);
    }

    private List<CartItem> checkParentItem(Order order) {
        List<CartItem> listCartItems = new ArrayList<>();
        for (CartItem cart : order.getOrderItems()) {
            if (cart.getOrderParentItem() == null) {
                listCartItems.add(cart);
            }
        }
        return listCartItems;
    }

    public void isShowButtonUpdateQty(boolean isShow) {
        mOrderInvoicePanel.isShowButtonUpdateQty(isShow);
    }

    public void isEnableButtonUpdateInvoice(boolean isEnable) {
        mOrderInvoicePanel.isEnableButtonUpdateInvoice(isEnable);
    }

    public void isEnableButtonSubmitInvoice(boolean isEnable) {
        mOrderInvoicePanel.isEnableButtonSubmitInvoice(isEnable);
    }
}
