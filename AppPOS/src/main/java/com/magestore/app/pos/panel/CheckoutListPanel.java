package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.pos.R;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.PanelCheckoutListBinding;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class CheckoutListPanel extends AbstractListPanel<Checkout> {
    private PanelCheckoutListBinding mBinding;
    private CartItemListPanel mCartItemListPanel;
    private CartItemListController mCartItemListController;
    CartService cartService = null;

    public CheckoutListPanel(Context context) {
        super(context);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, Checkout item, int position) {

    }

    @Override
    public void initLayout() {
        super.initLayout();
        mCartItemListPanel = (CartItemListPanel) findViewById(R.id.order_item_panel);
        mBinding = DataBindingUtil.bind(getView());
    }

    @Override
    public void initModel() {
        super.initModel();

        try {
            ServiceFactory factory = ServiceFactory.getFactory(getController().getMagestoreContext());
            cartService = factory.generateCartService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // controller quản lý đơn hàng
        mCartItemListController = new CartItemListController();
        mCartItemListController.setMagestoreContext(getController().getMagestoreContext());
        mCartItemListController.setListPanel(mCartItemListPanel);
        mCartItemListController.setChildListService(cartService);
        mCartItemListController.setParentController(getController());

        // tham chiếu từ checkout sang cartitem
        if (getController() instanceof CheckoutListController)
            ((CheckoutListController) getController()).setCartItemListController(mCartItemListController);
    }

    /**
     * Cập nhật bảng giá tổng
     */
    public void updateTotalPrice(Checkout checkout) {
        mBinding.setCheckout(checkout);
    }
}
