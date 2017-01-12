package com.magestore.app.pos.panel;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.daimajia.swipe.SwipeLayout;
import com.magestore.app.lib.adapterview.adapter2pos.AdapterView2Model;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderItemListController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.MagestoreTextView;

/**
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class OrderItemListPanel extends AbstractListPanel<Items> {
    // Chứa danh sách order item
//    private RecyclerView mOrderItemRecycleView;

    // Xử lý các nghiệp vụ của order
//    private CartService mOrderUseCase;

    // Listner xử lý các sự kiện
//    private OrderItemPanelListener mOrderItemPanelListener;

    // các text view hiện thị tổng giá
//    AdapterView2Model mAdapter2View = new AdapterView2Model();
    private MagestoreTextView mSubTotalView;
    private MagestoreTextView mTaxTotalView;
    private MagestoreTextView mDiscountTotalView;

    // button checkout
    private Button mCheckoutButton;

    // adapter
//    OrderItemListPanel.OrderItemListRecyclerViewAdapter mOrderItemListAdapter;


    public OrderItemListPanel(Context context) throws InstantiationException, IllegalAccessException {
        super(context);
    }

    public OrderItemListPanel(Context context, AttributeSet attrs) throws InstantiationException, IllegalAccessException {
        super(context, attrs);
    }

    public OrderItemListPanel(Context context, AttributeSet attrs, int defStyleAttr) throws InstantiationException, IllegalAccessException {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Chuẩn bị layout
     */
    public void initLayout() {
        // Load layout view các mặt hàng trong 1 đơn hàng
        View v = inflate(getContext(), R.layout.panel_order_item, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách khách hàng
        setLayoutItem(R.layout.card_sales_order_list_content);

        // View chưa danh sách các mặt hàng trong đơn
        initRecycleView(R.id.sales_order_container, new GridLayoutManager(getContext(), 1));

        // Tham chiếu các text view tổng và button
        mSubTotalView = (MagestoreTextView) findViewById(R.id.text_sales_order_subtotal);
        mTaxTotalView = (MagestoreTextView) findViewById(R.id.text_sales_order_tax);
        mDiscountTotalView = (MagestoreTextView) findViewById(R.id.text_sales_order_discount);

        // Button
        mCheckoutButton = (Button) findViewById(R.id.btn_sales_order_checkout);
    }

    @Override
    protected void bindItem(View view, final Items item) {
//        mImageView = (ImageView) view.findViewById(R.id.sales_order_image);
        final Product product = item.getProduct();

        // Điền tên, và SKU của product
        ((TextView) view.findViewById(R.id.sales_order_name)).setText(product.getName());
        ((TextView) view.findViewById(R.id.sales_order_sku)).setText(product.getSKU());

        // Điền giá và số lượng
        final MagestoreTextView txtPrice = ((MagestoreTextView) view.findViewById(R.id.sales_order_price));
        final TextView txtQuantity = ((TextView) view.findViewById(R.id.sales_order_quantity));
        txtPrice.setRawText(product.getPrice() * item.getQuantity());
        txtQuantity.setText("" + item.getQuantity());

        // Điền giá và số lượng cho swipe
        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.sales_order_swipe_layout);
        final TextView quantitySwipe = (TextView) swipeLayout.findViewById(R.id.sales_order_swipe_textview);
        quantitySwipe.setText(txtQuantity.getText());
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.sales_order_swipe_delete_layout));

        Button mIncButton = (Button) swipeLayout.findViewById(R.id.sales_order_swipe_inc_quantity);
        Button mDesButton = (Button) swipeLayout.findViewById(R.id.sales_order_swipe_des_quantity);
        ImageButton mDelButton = (ImageButton) swipeLayout.findViewById(R.id.sales_order_swipe_del_button);

        // Xử lý sự kiện ấn nút tăng trên swipe
        mIncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getOrderItemListController().addProduct(item.getProduct(), 1);
                quantitySwipe.setText("" + item.getQuantity());
            }
        });
//
//
        // Xử lý sự kiện ấn nút giảm trên swipe
        mDesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (item.getQuantity() <= 1) return;
                getOrderItemListController().substructProduct(item.getProduct(), 1);
                quantitySwipe.setText("" + item.getQuantity());
            }
        });
//
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

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                OrderItemListPanel.this.notifyDatasetChanged();
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
        if (mSubTotalView == null || mDiscountTotalView == null) return;

        // cập nhật tổng lên cuối order
        mSubTotalView.setRawText(order.getSubTotal());
        mDiscountTotalView.setRawText(order.getDiscountTotal());
        mTaxTotalView.setRawText(order.getTaxTotal());
        mCheckoutButton.setText(getContext().getString(R.string.checkout) + ": " + ConfigUtil.formatPrice(order.getLastTotal()));
    }

    public OrderItemListController getOrderItemListController() {
        return (OrderItemListController) getController();
    }
}
