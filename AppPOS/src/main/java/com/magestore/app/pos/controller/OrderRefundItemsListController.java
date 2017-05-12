package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.service.order.OrderHistoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderRefundItemsListController extends AbstractListController<CartItem> implements ListController<CartItem> {
    Order mSelectedOrder;
    OrderHistoryListController mOrderHistoryListController;
    OrderHistoryService mOrderService;

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

    public OrderItemParams createOrderRefundItemParams() {
        return mOrderService.createOrderItemParams();
    }

    public void changeMaxStoreCreditRefund() {
        float total_item_price = 0;
        float total_giftcard = 0;
        for (CartItem cart : mList) {
            total_item_price += ((cart.getBasePriceInclTax() - ((cart.getBaseDiscountAmount() + cart.getBaseGiftVoucherDiscount() + cart.getRewardpointsBaseDiscount()) / cart.getQtyOrdered())) * cart.QtyRefund());;
            total_giftcard += cart.getBaseGiftVoucherDiscount();
        }
        mOrderHistoryListController.updateToTalPriceChangeQtyRefund(total_item_price);
        mOrderHistoryListController.updateMaxRefundGiftCard(total_giftcard);
    }
}
