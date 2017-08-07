package com.magestore.app.pos.controller;

import android.view.View;
import android.widget.LinearLayout;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.OrderPaymentLaterListPanel;
import com.magestore.app.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderPaymentListController extends AbstractListController<OrderWebposPayment> implements ListController<OrderWebposPayment> {
    Order mSelectedOrder;
    OrderHistoryListController mOrderHistoryListController;
    OrderHistoryService mOrderService;
    OrderPaymentLaterListPanel mOrderPaymentLaterListPanel;
    LinearLayout ll_payment_later;

    public void setOrderPaymentLaterListPanel(OrderPaymentLaterListPanel orderPaymentLaterListPanel) {
        mOrderPaymentLaterListPanel = orderPaymentLaterListPanel;
    }

    public void setLayoutPaymentLater(LinearLayout ll_payment_later) {
        this.ll_payment_later = ll_payment_later;
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

    @Override
    protected List<OrderWebposPayment> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 order cho payment
     *
     * @param order
     */
    public void doSelectOrder(Order order) {
        mSelectedOrder = order;
        mList = checkIntegration(checkPaymentList(order), order);
        mView.bindList(mList);
    }

    public List<OrderWebposPayment> checkPaymentList(Order order) {
        if (order.getBaseTotalDue() > 0) {
            List<OrderWebposPayment> webposPaymentList = new ArrayList<>();
            List<OrderWebposPayment> webposPaymentsListLater = new ArrayList<>();
            List<OrderWebposPayment> listPayment = order.getWebposOrderPayments();
            if (listPayment != null && listPayment.size() > 0) {
                for (OrderWebposPayment payment : listPayment) {
                    if (payment.getBasePaymentAmount() > 0) {
                        webposPaymentList.add(payment);
                    } else {
                        webposPaymentsListLater.add(payment);
                    }
                }
            }
            if (webposPaymentsListLater.size() > 0) {
                ll_payment_later.setVisibility(View.VISIBLE);
                mOrderPaymentLaterListPanel.bindList(webposPaymentsListLater);
            } else {
                ll_payment_later.setVisibility(View.GONE);
            }
            return webposPaymentList;
        } else {
            ll_payment_later.setVisibility(View.GONE);
            return order.getWebposOrderPayments();
        }
    }

    public List<OrderWebposPayment> checkIntegration(List<OrderWebposPayment> listPayment, Order order) {
        boolean showIntegration = false;
        if(ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2)){
            if (order.getPayment() != null) {
                if ((order.getBaseGiftVoucherDiscount() < 0 || order.getRewardPointsBaseDiscount() < 0) && order.getPayment().getMethod().equals("multipaymentforpos")) {
                    showIntegration = true;
                }
            }
        }else{
            if ((order.getBaseGiftVoucherDiscount() < 0 || order.getRewardPointsBaseDiscount() < 0)) {
                showIntegration = true;
            }
        }

        if (showIntegration) {
            if (order.getBaseGiftVoucherDiscount() < 0) {
                OrderWebposPayment giftPayment = mOrderService.createOrderWebposPayment();
                float base_gift = 0 - order.getBaseGiftVoucherDiscount();
                float gift = 0 - order.getGiftVoucherDiscount();
                giftPayment.setBaseDisplayAmount(base_gift);
                giftPayment.setBasePaymentAmount(base_gift);
                giftPayment.setDisplayAmount(gift);
                giftPayment.setPaymentAmount(gift);
                giftPayment.setMethod("giftCardIntergration");
                giftPayment.setOrderId(order.getID());
                giftPayment.setMethodTitle(getMagestoreContext().getActivity().getString(R.string.plugin_order_detail_bottom_giftcard_discount));
                listPayment.add(giftPayment);
            }

            if (order.getRewardPointsBaseDiscount() < 0) {
                OrderWebposPayment rewardPayment = mOrderService.createOrderWebposPayment();
                float base_reward = 0 - order.getRewardPointsBaseDiscount();
                float reward = 0 - order.getRewardPointsDiscount();
                rewardPayment.setBaseDisplayAmount(base_reward);
                rewardPayment.setBasePaymentAmount(base_reward);
                rewardPayment.setDisplayAmount(reward);
                rewardPayment.setPaymentAmount(reward);
                rewardPayment.setMethod("rewardPointsIntergration");
                rewardPayment.setOrderId(order.getID());
                rewardPayment.setMethodTitle(getMagestoreContext().getActivity().getString(R.string.plugin_order_detail_payment_content));
                listPayment.add(rewardPayment);
            }
        }

        return listPayment;
    }

    public void notifyDataSetChanged() {
        mView.notifyDataSetChanged();
    }
}
