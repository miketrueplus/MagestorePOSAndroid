package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.swipe.SwipeLayout;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.databinding.CardCartListContentBinding;
import com.magestore.app.pos.databinding.PanelCartListBinding;

/**
 * Quản lý hiển thị danh sách các hàng chọn trong cart
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class CartItemListPanel extends AbstractListPanel<CartItem> {
    // Binding dữ liệu
    PanelCartListBinding mBinding;

    // button checkout
    private Button mCheckoutButton;

    // adapter
//    OrderItemListPanel.OrderItemListRecyclerViewAdapter mOrderItemListAdapter;


    public CartItemListPanel(Context context) throws InstantiationException, IllegalAccessException {
        super(context);
    }

    public CartItemListPanel(Context context, AttributeSet attrs) throws InstantiationException, IllegalAccessException {
        super(context, attrs);
    }

    public CartItemListPanel(Context context, AttributeSet attrs, int defStyleAttr) throws InstantiationException, IllegalAccessException {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Chuẩn bị layout
     */
    public void initLayout() {
        // Load layout view các mặt hàng trong 1 đơn hàng
        mBinding = DataBindingUtil.bind(getView());

        // Button
        mCheckoutButton = (Button) findViewById(R.id.btn_sales_order_checkout);
    }

    @Override
    protected void bindItem(View view, final CartItem item, int position) {
        // map dữ liệu của item vào
        final CardCartListContentBinding binding = DataBindingUtil.bind(view);
        binding.setCartItem(item);

        // Hiển thị ảnh
        ImageView imageView = (ImageView) view.findViewById(R.id.sales_order_image);
        if (item.getProduct() != null) {
            Bitmap bmp = item.getProduct().getBitmap();
            if (bmp != null && !bmp.isRecycled() && imageView != null)
                imageView.setImageBitmap(bmp);
        }

        // Cho phép swipe drag
        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.sales_order_swipe_layout);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.sales_order_swipe_delete_layout));

        // các button trên swipe
        RelativeLayout mDelButton = (RelativeLayout) swipeLayout.findViewById(R.id.sales_order_swipe_del_button);

        // Xử lý sự kiện xóa trên swipe
        mDelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getOrderItemListController().deleteProduct(item.getProduct());
            }
        });

        // Sự kiện khi swipe in
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            /**
             * Khi close swipe
             * @param swipeLayout
             */
            @Override
            public void onClose(SwipeLayout swipeLayout) {
                CartItemListPanel.this.notifyDataSetChanged();
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }

    /*
     * Trả về controller
     * @return
     */
    public CartItemListController getOrderItemListController() {
        return (CartItemListController) getController();
    }
}
