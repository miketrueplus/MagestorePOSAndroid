package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.view.SearchAutoCompleteTextView;
import com.magestore.app.lib.view.item.ModelView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.databinding.CardCartListContentBinding;
import com.magestore.app.pos.databinding.CardProductListContentBinding;
import com.magestore.app.pos.databinding.PanelCartListBinding;
import com.magestore.app.pos.task.LoadProductImageTask;
import com.magestore.app.util.ConfigUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        // init layout
        super.initLayout();

        // Load layout view các mặt hàng trong 1 đơn hàng
        mBinding = DataBindingUtil.bind(getView());

        // Button
        mCheckoutButton = (Button) findViewById(R.id.btn_sales_order_checkout);
    }

    /**
     * Hold ayout view của iten, gán findview id vào các biến
     * @param view
     * @return
     */
    @Override
    protected RecycleViewItemHolder holdItemView(View view) {
        RecycleViewItemHolder viewHolder = new CartItemListPanel.RecycleViewCartItemHolder(view);
        viewHolder.holdView(view);
        return viewHolder;
    }

    /**
     * Enable hoặc disable swipe
     * @param enable
     */
    public void enableSwipeItem(boolean enable) {
        List<ModelView> viewList = getModelViewList();
        if (viewList == null) return;
        for (ModelView view : viewList) {
            if (view.getViewHolder() == null || !(view.getViewHolder() instanceof CartItemListPanel.RecycleViewCartItemHolder)) return;
            ((RecycleViewCartItemHolder) view.getViewHolder()).swipeLayout.setSwipeEnabled(enable);
        }
    }

    /**
     * Hold layout và nội dung các item trong view
     */
    public class RecycleViewCartItemHolder extends RecycleViewItemHolder {
        CardCartListContentBinding binding;
        ImageView imageView;
        SwipeLayout swipeLayout;
        RelativeLayout mDelButton;
        boolean mblnOnSwipe = false;
        boolean mblnLockMsgSwipe = false;

        public RecycleViewCartItemHolder(View view) {
            super(view);
        }

        /**
         * Hold layout item trong view
         * @param view
         */
        @Override
        public void holdView(View view) {
            super.holdView(view);

            // map dữ liệu của item vào
            binding = DataBindingUtil.bind(view);

            // Hiển thị ảnh
            imageView = (ImageView) view.findViewById(R.id.sales_order_image);

            // Cho phép swipe drag
            swipeLayout = (SwipeLayout) view.findViewById(R.id.sales_order_swipe_layout);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.sales_order_swipe_delete_layout));

            // các button trên swipe
            mDelButton = (RelativeLayout) swipeLayout.findViewById(R.id.sales_order_swipe_del_button);

            // Xử lý sự kiện xóa trên swipe
            mDelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // không cho xóa nếu trạng thái không cho thay đổi caritem
                    if (!((CartItemListController)getController()).isAllowChangeCartItem()) return;
                    if (getModelView() == null || getModelView().getModel() == null) return;
                    ((CartItemListController)getController()).deleteProduct(((CartItem)getModelView().getModel()).getProduct());
                }
            });

            // double click trên item
            swipeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((CartItemListController)getController()).isAllowChangeCartItem()) return;
                    if (getModelView() == null || getModelView().getModel() == null) return;
                    if (mblnOnSwipe) return;

                    getController().bindItem((CartItem) getModelView().getModel());
                    getController().doShowDetailPanel(true);

                }
            });

            // Sự kiện khi swipe left/right
            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                    mblnOnSwipe = true;
                    mblnLockMsgSwipe = true;
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

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    mblnLockMsgSwipe = false;
                    final Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (mblnLockMsgSwipe) return;
                            mblnOnSwipe = false;
                        }
                    };
                    mHandler.sendEmptyMessageDelayed(0, 300);
                }
            });
        }

        /**
         * Đặt nội dung item trong view
         * @param item
         * @param position
         */
        public void setItem(ModelView item, int position) {
            binding.setCartItem((CartItem)item.getModel());
            if (((CartItem)item.getModel()).getProduct() != null) {
                Product product = ((CartItem)item.getModel()).getProduct();
                ImageView productImgView = ((ImageView) product.getRefer(LoadProductImageTask.KEY_IMAGEVIEW));
                if (productImgView != null) imageView.setImageDrawable(productImgView.getDrawable());
            }
        }
    }
}
