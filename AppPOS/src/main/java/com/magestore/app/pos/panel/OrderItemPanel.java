package com.magestore.app.pos.panel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.daimajia.swipe.SwipeLayout;
import com.magestore.app.lib.entity.Order;
import com.magestore.app.lib.entity.OrderItem;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosOrder;
import com.magestore.app.lib.usecase.OrderUseCase;
import com.magestore.app.lib.usecase.UseCaseFactory;
import com.magestore.app.pos.R;
import com.magestore.app.pos.SalesListActivity;
import com.magestore.app.util.ConfigUtil;

import java.util.List;

/**
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class OrderItemPanel extends FrameLayout {
    // Chứa danh sách order item
    private RecyclerView mOrderItemRecycleView;

    // Xử lý các nghiệp vụ của order
    private OrderUseCase mOrderUseCase;

    // Listner xử lý các sự kiện
    private OrderItemPanelListener mOrderItemPanelListener;

    // các text view hiện thị tổng giá
    private TextView mSubTotalView;
    private TextView mTaxTotalView;
    private TextView mDiscountTotalView;

    // button checkout
    private Button mCheckoutButton;

    // adapter
    OrderItemPanel.OrderItemListRecyclerViewAdapter mOrderItemListAdapter;


    public OrderItemPanel(Context context) {
        super(context);
        init();
    }

    public OrderItemPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrderItemPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initControlLayout();
        initControlValue();
        initTask();
    }

    /**
     * Chuẩn bị layout
     */
    private void initControlLayout() {
        // Load layout view các mặt hàng trong 1 đơn hàng
        View v = inflate(getContext(), R.layout.panel_order_item, null);
        addView(v);

        // View chưa danh sách các mặt hàng trong đơn
        mOrderItemRecycleView = (RecyclerView) findViewById(R.id.sales_order_container);
        if (mOrderItemRecycleView != null)
            mOrderItemRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        // Tham chiếu các text view tổng và button
        mSubTotalView = (TextView) findViewById(R.id.text_sales_order_subtotal);
        mTaxTotalView = (TextView) findViewById(R.id.text_sales_order_tax);
        mDiscountTotalView = (TextView) findViewById(R.id.text_sales_order_discount);

        // Button
        mCheckoutButton = (Button) findViewById(R.id.btn_sales_order_checkout);
    }

    private void initControlValue() {
        // Lập một đơn hàng mới
        mOrderUseCase = UseCaseFactory.generateOrderUseCase(null, null);
        mOrderUseCase.newSales(new PosOrder());

        mOrderItemListAdapter
                = new OrderItemPanel.OrderItemListRecyclerViewAdapter(mOrderUseCase.getOrder());
        mOrderItemRecycleView.setAdapter(mOrderItemListAdapter);
    }

    private void initTask() {

    }

    public void addOrderItem(Product product) {
        if (mOrderUseCase == null || mOrderItemListAdapter == null) return;
        mOrderUseCase.addOrderItem(product, 1, product.getPrice());
        mOrderItemListAdapter.notifyDataSetChanged();

        // cập nhật tổng lên cuối order
        updateTotalPrice();
    }

    /**
     * Đặt sự kiện xử lý khi load Product
     * @param orderItemPanelListener
     */
    public void setListener(OrderItemPanelListener orderItemPanelListener) {
        mOrderItemPanelListener = orderItemPanelListener;
    }

    /**
     * Cập nhật bảng giá tổng
     */
    private void updateTotalPrice() {
        if (mSubTotalView == null || mDiscountTotalView == null) return;

        // cập nhật tổng lên cuối order
        mSubTotalView.setText(ConfigUtil.formatPrice(mOrderUseCase.calculateSubTotalOrderItems()));
        mDiscountTotalView.setText(ConfigUtil.formatPrice(mOrderUseCase.calculateDiscountTotalOrderItems()));
        mTaxTotalView.setText(ConfigUtil.formatPrice((mOrderUseCase.calculateTaxOrderItems())));
        mCheckoutButton.setText(getContext().getString(R.string.checkout) + ": " + ConfigUtil.formatPrice(mOrderUseCase.calculateLastTotalOrderItems()));
    }

    public class OrderItemListRecyclerViewAdapter
            extends RecyclerView.Adapter<OrderItemListRecyclerViewAdapter.OrderItemListViewHolder> {
        Order mOrder;


        public OrderItemListRecyclerViewAdapter(Order order) {
            mOrder = order;
        }

        @Override
        public OrderItemListRecyclerViewAdapter.OrderItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sales_order_list_content, parent, false);
            return new OrderItemListRecyclerViewAdapter.OrderItemListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OrderItemListRecyclerViewAdapter.OrderItemListViewHolder holder, final int position) {
            final OrderItem item = mOrder.getOrderItems().get(position);
            holder.mItem = item;

            // Giữ tham chiếu imageview theo product
            final Product product = item.getProduct();

            // Đặt các trường text vào danh sách
            holder.mNameView.setText(product.getName());
            holder.mPriceView.setText(ConfigUtil.formatPrice(product.getPrice() * item.getQuantity()));
            holder.mSKUView.setText(product.getSKU());
            holder.mQuantityView.setText("" + item.getQuantity());
            holder.mQuantitySwipe.setText(holder.mQuantityView.getText());

            // Gán ảnh cho view
            Bitmap bmp = product.getBitmap();
            if (bmp != null && !bmp.isRecycled()) holder.mImageView.setImageBitmap(bmp);

            // Xử lý sự kiện ấn nút tăng trên swipe
            holder.mIncButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mOrderUseCase.addOrderItem(product, 1, product.getPrice());
                    holder.mQuantitySwipe.setText("" + item.getQuantity());
                }
            });


            // Xử lý sự kiện ấn nút giảm trên swipe
            holder.mDesButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (item.getQuantity() <= 1) return;
                    mOrderUseCase.subtructOrderItem(product, 1);
                    holder.mQuantitySwipe.setText("" + item.getQuantity());
                }
            });

            // Xử lý sự kiện xóa trên swipe
            holder.mDelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mOrderUseCase.delOrderItem(position);
                    mOrderItemRecycleView.getAdapter().notifyDataSetChanged();
                    updateTotalPrice();
                }
            });

            holder.mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {

                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout swipeLayout) {
                    mOrderItemRecycleView.getAdapter().notifyDataSetChanged();
                    updateTotalPrice();
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
        }

        @Override
        public int getItemCount() {
            if (mOrder == null) return 0;
            if (mOrder.getOrderItems() == null) return 0;
            return mOrder.getOrderItems().size();
        }

        public class OrderItemListViewHolder extends RecyclerView.ViewHolder {
            public SwipeLayout mSwipeLayout = null;

            public final TextView mNameView;
            public final ImageView mImageView;
            public final TextView mPriceView;
            public final TextView mSKUView;
            public final TextView mQuantityView;
            public final TextView mQuantitySwipe;
            public final Button mIncButton;
            public final Button mDesButton;
            public final ImageButton mDelButton;
            public OrderItem mItem;
            public View mView;

            public OrderItemListViewHolder(View view) {
                super(view);
//                swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.sales_order_swipe_layout);

                mImageView = (ImageView) view.findViewById(R.id.sales_order_image);
                mNameView = (TextView) view.findViewById(R.id.sales_order_name);
                mSKUView = (TextView) view.findViewById(R.id.sales_order_sku);
                mPriceView = (TextView) view.findViewById(R.id.sales_order_price);
                mQuantityView = (TextView) view.findViewById(R.id.sales_order_quantity);
                mView = view.findViewById(R.id.sales_order_card_view);

                mIncButton = (Button) view.findViewById(R.id.sales_order_swipe_inc_quantity);
                mDesButton = (Button) view.findViewById(R.id.sales_order_swipe_des_quantity);
                mDelButton = (ImageButton) view.findViewById(R.id.sales_order_swipe_del_button);


                mSwipeLayout = (SwipeLayout) view.findViewById(R.id.sales_order_swipe_layout);
                mQuantitySwipe = (TextView) mSwipeLayout.findViewById(R.id.sales_order_swipe_textview);
                mSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, mSwipeLayout.findViewById(R.id.sales_order_swipe_delete_layout));
            }
        }
    }
}
