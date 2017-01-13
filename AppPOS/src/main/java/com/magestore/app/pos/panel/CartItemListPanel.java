package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.daimajia.swipe.SwipeLayout;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.databinding.CardCartListContentBinding;
import com.magestore.app.pos.databinding.PanelCartListBinding;
import com.magestore.app.view.FormatTextView;

/**
 * Quản lý hiển thị danh sách các hàng chọn trong cart
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class CartItemListPanel extends AbstractListPanel<Items> {
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
        View v = inflate(getContext(), R.layout.panel_cart_list, null);
        addView(v);
        mBinding = DataBindingUtil.bind(v);

        // Chuẩn bị layout từng item trong danh sách khách hàng
        setLayoutItem(R.layout.card_cart_list_content);

        // View chưa danh sách các mặt hàng trong đơn
        initRecycleView(R.id.sales_order_container, new GridLayoutManager(getContext(), 1));

        // Button
        mCheckoutButton = (Button) findViewById(R.id.btn_sales_order_checkout);
    }

    @Override
    protected void bindItem(View view, final Items item, int position) {

        final CardCartListContentBinding binding = DataBindingUtil.bind(view);
        // map dữ liệu của item vào
        binding.setCartItem(item);
//        mImageView = (ImageView) view.findViewById(R.id.sales_order_image);
        final Product product = item.getProduct();

        // Cho phép swipe drag
        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.sales_order_swipe_layout);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.sales_order_swipe_delete_layout));

        // các button trên swipe
        Button mIncButton = (Button) swipeLayout.findViewById(R.id.sales_order_swipe_inc_quantity);
        Button mDesButton = (Button) swipeLayout.findViewById(R.id.sales_order_swipe_des_quantity);
        ImageButton mDelButton = (ImageButton) swipeLayout.findViewById(R.id.sales_order_swipe_del_button);

        // Xử lý sự kiện ấn nút tăng trên swipe
        mIncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getOrderItemListController().addProduct(item.getProduct(), 1);
                binding.salesOrderSwipeTextview.setText(Integer.toString(item.getQuantity()));
            }
        });

        // Xử lý sự kiện ấn nút giảm trên swipe
        mDesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (item.getQuantity() <= 1) return;
                getOrderItemListController().substructProduct(item.getProduct(), 1);
                binding.salesOrderSwipeTextview.setText(Integer.toString(item.getQuantity()));
            }
        });

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
                CartItemListPanel.this.notifyDatasetChanged();
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }

    /**
     * Cập nhật bảng giá tổng
     */
    public void updateTotalPrice(Order order) {
        mBinding.setOrder(order);
    }

    /**
     * Trả về controller
     * @return
     */
    public CartItemListController getOrderItemListController() {
        return (CartItemListController) getController();
    }
}
