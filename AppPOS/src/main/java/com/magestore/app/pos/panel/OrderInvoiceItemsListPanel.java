package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderInvoiceItemsListController;
import com.magestore.app.pos.databinding.CardOrderInvoiceItemContentBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.EditTextFloat;
import com.magestore.app.view.EditTextInteger;
import com.magestore.app.view.EditTextQuantity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Johan on 2/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderInvoiceItemsListPanel extends AbstractListPanel<CartItem> {
    List<CartItem> listItems;
    List<CartItem> listCurrentItem;
    Order mOrder;
    float total_price = 0;
    HashMap<CartItem, EditTextQuantity> listQuantity;

    public void setOrder(Order mOrder) {
        this.mOrder = mOrder;
    }

    public OrderInvoiceItemsListPanel(Context context) {
        super(context);
    }

    public OrderInvoiceItemsListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderInvoiceItemsListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
//        super.initLayout();
        // Load layout view danh sách items của khách hàng
        View v = inflate(getContext(), R.layout.panel_order_invoice_item_list, null);
        addView(v);

        // chuẩn bị list view
        initListView(R.id.order_invoice_items_list);

        // Chuẩn bị layout từng item trong danh sách items
        setLayoutItem(R.layout.card_order_invoice_item_content);

        // Chuẩn bị list danh sách item
//        mRecycleView = (RecyclerView) findViewById(R.id.order_invoice_items_list);
//        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
//        mRecycleView.setNestedScrollingEnabled(false);
        listQuantity = new HashMap<>();
    }

    @Override
    protected void bindItem(View view, CartItem item, int position) {
        if (item.getPriceInvoice() == 0) {
            item.setPriceInvoice(item.getBasePriceInclTax());
        }
        CardOrderInvoiceItemContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setOrderItem(item);
        CartItem cartItem = listItems.get(position);
        EditTextQuantity edt_qty_to_invoice = (EditTextQuantity) view.findViewById(R.id.qty_to_invoice);
        edt_qty_to_invoice.setOrderHistory(true);
        edt_qty_to_invoice.setDecimal(item.isDecimal());
        cartItem.setOrderItemId(cartItem.getItemId());
        cartItem.setQtyChange(item.QtyInvoice());
        listQuantity.put(cartItem, edt_qty_to_invoice);
        actionQtyToInvoice(cartItem, edt_qty_to_invoice);
    }

    float total_current = 0;
    private void actionQtyToInvoice(final CartItem item, final EditTextQuantity qty_to_invoice) {
        qty_to_invoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float qty_invoiced = qty_to_invoice.getValueFloat();
                float qty = 0;
                if (mOrder.getBaseTotalDue() > 0) {
                    qty = item.QtyInvoiceable();
                } else {
                    qty = item.QtyInvoice();
                }
                if (qty_invoiced < 0 || qty_invoiced > qty) {
                    qty_to_invoice.setText(ConfigUtil.formatQuantity(qty));
                    item.setQuantity(qty);
                    item.setQtyChange(qty);
                } else {
                    item.setQuantity(qty_invoiced);
                    item.setQtyChange(qty_invoiced);
                }

                if (mOrder.getBaseTotalDue() > 0) {
                    item.setQtyInvoiceable(item.QtyInvoice());
//                    checkQuantityInvoice();
                }
                ((OrderInvoiceItemsListController) mController).isShowButtonUpdateQty(checkChangeQtyItem() ? true : false);
                ((OrderInvoiceItemsListController) mController).isEnableButtonSubmitInvoice(checkChangeQtyItem() ? false : true);
                ((OrderInvoiceItemsListController) mController).isEnableButtonUpdateInvoice(checkTotalQuantity() ? true : false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkQuantityInvoice() {
        total_price = 0;
        for (CartItem item : listItems) {
            checkQtyInvoice(item);
        }
    }

    public void checkQtyInvoice(CartItem item) {
        if (item.getPriceInvoice() == 0) {
            item.setPriceInvoice(item.getBasePriceInclTax());
        }
        float total_paid = (mOrder.getBaseTotalPaid() - mOrder.getBaseTotalInvoiced() - mOrder.getWebposBaseChange() - mOrder.getBaseTotalRefunded());
        if (total_price < total_paid) {
            float quantity = 0;
            if (item.getQtyChange() != item.QtyInvoiceable()) {
                quantity = item.getQtyChange();
            } else {
                quantity = item.QtyInvoice();
            }
            if (quantity > 0) {
                int qty = 0;
                float total_invoice = 0;

                for (int i = 0; i <= quantity; i++) {
                    total_invoice = qty * (item.getPriceInvoice() - ((item.getBaseDiscountAmount() + item.getBaseGiftVoucherDiscount() + item.getRewardpointsBaseDiscount()) / item.getQtyOrdered()));
                    float total_price_all = total_invoice + total_price;
                    if (total_price_all > total_paid) {
                        break;
                    }
                    qty++;
                }
                item.setQtyInvoiceable(qty - 1);
                item.setQuantity(qty - 1);
                total_price += item.getQuantity() * (item.getPriceInvoice() - ((item.getBaseDiscountAmount() + item.getBaseGiftVoucherDiscount() + item.getRewardpointsBaseDiscount()) / item.getQtyOrdered()));
            }
        } else {
            item.setQtyInvoiceable(0);
            item.setQuantity(0);
        }
    }

    public void setDataListItem(List<CartItem> orderItems) {
        listItems = orderItems;
        listCurrentItem = listItems;
    }

    public List<CartItem> bind2List() {
        return listItems;
    }

    private boolean checkChangeQtyItem() {
            for (CartItem item : listItems) {
                    if (mOrder.getBaseTotalDue() > 0) {
                        if (item.getQtyChange() != item.getQtyCurrent()) {
                            return true;
                        }
                    } else {
                        if (item.getQtyChange() != item.QtyInvoice()) {
                            return true;
                        }
                    }

            }
        return false;
    }

    private boolean checkTotalQuantity() {
        int total_qty = 0;
        for (CartItem item : listItems) {
            total_qty += item.getQuantity();
        }
        if (total_qty > 0) {
            return true;
        } else {
            return false;
        }
    }
}
