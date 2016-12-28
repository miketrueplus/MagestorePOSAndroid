package com.magestore.app.pos;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;


import com.daimajia.swipe.SwipeLayout;
import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.entity.Order;
import com.magestore.app.lib.entity.OrderItem;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosOrder;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.lib.usecase.OrderUseCase;
import com.magestore.app.lib.usecase.UseCaseFactory;
import com.magestore.app.pos.controller.LoadProductController;
import com.magestore.app.pos.controller.LoadProductImageController;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.pos.ui.SalesUI;

import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SalesDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SalesListActivity extends AbstractActivity
        implements
        SalesUI {
    // 2 pane
    private boolean mTwoPane;

    // Recycle view nắm list
    private RecyclerView mProductListRecyclerView;
    private RecyclerView mOrderListRecycleView;
    private List<Product> mListProduct;
    private OrderUseCase mOrderUseCase;

    private TextView mSubTotalView;
    private TextView mTaxTotalView;
    private TextView mDiscountTotalView;

    private Button mCheckoutButton;
    private AutoCompleteTextView mSearchProductView;
    //
    private LoadProductController mLoadProduct = null;
    private LoadProductImageController mLoadImageTask;

    SalesListActivity.OrderItemListRecyclerViewAdapter mOrderItemListAdapter;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_menu);
//        com.chauthai.swipereveallayout.SwipeRevealLayout a;
        // chuẩn bị control layout
        initControlLayout();

        // chuản bị các value trong layout
        initControlValue();

        // chuẩn bị các task
        initTask();

        // Load product
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
        {
            mLoadProduct.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else // Below Api Level 13
        {
            mLoadProduct.execute();
        }
    }

    @Override
    protected void initControlLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category);
        if (toolbar == null) toolbar = (Toolbar) findViewById(R.id.toolbar_order);
        initToolbarMenu(toolbar);

        // Nút tab để tạo đơn hàng mới
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSearchProductView = (AutoCompleteTextView) findViewById(R.id.text_search_product);
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        String[] dogArr = getResources().getStringArray(R.array.dogs_list);
        List<String> dogList = Arrays.asList(dogArr);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layoutItemId, dogList);
        mSearchProductView.setAdapter(adapter);

        // Xác định là 2 pane hay 1 pane
        if (findViewById(R.id.toolbar_category) != null) {
            mTwoPane = true;
        }

        // Lấy view product và xác định số cột theo giao diện
        mProductListRecyclerView = (RecyclerView) findViewById(R.id.sales_list);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, mTwoPane ? 4 : 1);
        mProductListRecyclerView.setLayoutManager(mLayoutManager);

        // thiết lập view bên order
        mOrderListRecycleView = (RecyclerView) findViewById(R.id.sales_order_container);
        if (mOrderListRecycleView != null) {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
            mOrderListRecycleView.setLayoutManager(layoutManager);
        }

        // các text total
        mSubTotalView = (TextView) findViewById(R.id.text_sales_order_subtotal);
        mTaxTotalView = (TextView) findViewById(R.id.text_sales_order_tax);
        mDiscountTotalView = (TextView) findViewById(R.id.text_sales_order_discount);

        // Button
        mCheckoutButton = (Button) findViewById(R.id.btn_sales_order_checkout);

    }

    @Override
    protected void initTask() {
        mLoadProduct = new LoadProductController(new LoadProductListener());
//        mLoadImageTask = new LoadProductImageController(mListProduct);
    }

    @Override
    public List<Product> getProductList() {
        return mListProduct;
    }

    @Override
    public void setProductList(List<Product> listProduct) {
        mListProduct = listProduct;
    }

    @Override
    protected void initControlValue() {
        super.initControlValue();
        mOrderUseCase = UseCaseFactory.generateOrderUseCase(null, null);
        mOrderUseCase.newSales(new PosOrder());
        mOrderItemListAdapter
                = new SalesListActivity.OrderItemListRecyclerViewAdapter(mOrderUseCase.getOrder());
        if (mOrderListRecycleView != null)
            mOrderListRecycleView.setAdapter(mOrderItemListAdapter);
    }

    @Override
    public void loadListProductImage() {
        mLoadImageTask = new LoadProductImageController(new LoadProductImageListener(), mListProduct);
        if (mLoadImageTask != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            {
                mLoadImageTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else // Below Api Level 13
            {
                mLoadImageTask.execute();
            }
    }

    private void updateTotalPrice() {
        // cập nhật tổng lên cuối order
        mSubTotalView.setText(ConfigUtil.formatPrice(mOrderUseCase.calculateSubTotalOrderItems()));
        mDiscountTotalView.setText(ConfigUtil.formatPrice(mOrderUseCase.calculateDiscountTotalOrderItems()));
        mTaxTotalView.setText(ConfigUtil.formatPrice((mOrderUseCase.calculateTaxOrderItems())));
        mCheckoutButton.setText(getString(R.string.checkout) + ": " + ConfigUtil.formatPrice(mOrderUseCase.calculateLastTotalOrderItems()));
    }

//    @Override
//    public void onPreLoadProduct() {
//        showProgress(true);
//    }

//    @Override
//    public void onPostLoadProduct(List<Product> productList) {
//        mLoadProduct = null;
//        showProgress(false);
//        setProductList(productList);
//        if (mListProduct != null)
//            mRecyclerView.setAdapter(new SalesListActivity.SimpleItemRecyclerViewAdapter(mListProduct));
//        loadListProductImage();
//    }

//    @Override
//    public void onCancelLoadProduct() {
//        mLoadProduct = null;
//        showProgress(false);
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean navMenu = super.onNavigationItemSelected(item);

        // close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return navMenu;
    }

    /**
     * Hiển thị progress bar lúc load danh sách lần đầu
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                    }
//                });
//
//                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                mProgressView.animate().setDuration(shortAnimTime).alpha(
//                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                    }
//                });
//            } else {
//                // The ViewPropertyAnimator APIs are not available, so simply show
//                // and hide the relevant UI components.
//                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            }
    }

    /**
     * Xử lý các sự kiện khi load danh sách product lần đầu
     */
    public class LoadProductListener implements ControllerListener<Void, Void, List<Product>> {
        @Override
        public void onPreController(Controller controller) {
            showProgress(true);
        }

        @Override
        public void onPostController(Controller controller, List<Product> productList) {
            // Tất progress đi
            mLoadProduct = null;
            showProgress(false);

            // Hiển thị danh sách product
            setProductList(productList);
            if (mListProduct != null)
                mProductListRecyclerView.setAdapter(new SalesListActivity.ProductListRecyclerViewAdapter(mListProduct));

            // Tải anh và hiển thị luôn
            loadListProductImage();
        }

        @Override
        public void onCancelController(Controller controller, Exception exp) {
            mLoadProduct = null;
            showProgress(false);
        }

        @Override
        public void onProgressController(Controller controller, Void... progress) {

        }
    }

    public class LoadProductImageListener implements ControllerListener<Void, Product, Void> {

        @Override
        public void onPreController(Controller controller) {

        }

        @Override
        public void onPostController(Controller controller, Void aVoid) {

        }

        @Override
        public void onCancelController(Controller controller, Exception exp) {

        }

        @Override
        public void onProgressController(Controller controller, Product... progress) {

        }
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
            Bitmap bmp = (Bitmap) product.getRefer(LoadProductImageController.KEY_BITMAP);
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
                    mOrderListRecycleView.getAdapter().notifyDataSetChanged();
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
                    mOrderListRecycleView.getAdapter().notifyDataSetChanged();
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

        /**
         * Only if you need to restore open/close state when the orientation is changed.
         * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
         */
        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        /**
         * Only if you need to restore open/close state when the orientation is changed.
         * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
         */
        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
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

    public class ProductListRecyclerViewAdapter
            extends RecyclerView.Adapter<ProductListRecyclerViewAdapter.ProductListViewHolder> {

        private final List<Product> mValues;

        public ProductListRecyclerViewAdapter(List<Product> items) {
            mValues = items;
        }

        @Override
        public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sales_list_content, parent, false);
            return new ProductListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductListViewHolder holder, final int position) {
            Product product = mValues.get(position);
            holder.mItem = product;

            // Giữ tham chiếu imageview theo product
            product.setRefer(LoadProductImageController.KEY_IMAGEVIEW, holder.mImageView);

            // Đặt các trường text vào danh sách
            holder.mNameView.setText(product.getName());
            holder.mPriceView.setText(ConfigUtil.formatPrice(product.getPrice()));
            holder.mSKUView.setText(product.getSKU());
            holder.mAvaibleView.setText(R.string.avaibles);

            // Gán ảnh cho view
            Bitmap bmp = (Bitmap) product.getRefer(LoadProductImageController.KEY_BITMAP);
            if (bmp != null && !bmp.isRecycled()) holder.mImageView.setImageBitmap(bmp);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // gập nhật số lượng trên đơn hàng
                    Product product = mValues.get(position);
                    mOrderUseCase.addOrderItem(product, 1, product.getPrice());
                    mOrderItemListAdapter.notifyDataSetChanged();

                    // cập nhật tổng lên cuối order
                    updateTotalPrice();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ProductListViewHolder extends RecyclerView.ViewHolder {
            public final TextView mNameView;
            public final ImageView mImageView;
            public final TextView mPriceView;
            public final TextView mAvaibleView;
            public final TextView mSKUView;
            public Product mItem;
            public View mView;

            public ProductListViewHolder(View view) {
                super(view);
                mImageView = (ImageView) view.findViewById(R.id.product_image);
                mNameView = (TextView) view.findViewById(R.id.name);
                mSKUView = (TextView) view.findViewById(R.id.sku);
                mPriceView = (TextView) view.findViewById(R.id.price);
                mAvaibleView = (TextView) view.findViewById(R.id.avaiable);
                mView = view.findViewById(R.id.sales_product_list_card_view);
            }
        }
    }
}
