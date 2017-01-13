package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.task.Task;
import com.magestore.app.lib.task.TaskListener;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardProductListContentBinding;
import com.magestore.app.pos.task.LoadProductTask;
import com.magestore.app.pos.task.LoadProductImageTask;
import com.magestore.app.util.ConfigUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Giao diện quản lý danh mục sản phẩm
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class ProductListPanel extends AbstractListPanel<Product> {
    // Textbox search product
    AutoCompleteTextView mSearchProductTxt;

    // Task load danh sách khách hàng
//    LoadProductTask mLoadProductTask;
//    LoadProductImageTask mLoadImageTask;


    public ProductListPanel(Context context) {
        super(context);
    }

    public ProductListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Đặt sự kiện xử lý khi load Product
     * @param productListPanelListener
     */
//    public void setListener(ProductListPanelListener productListPanelListener) {
//        mProductListPanelListener = productListPanelListener;
//    }

    /**
     * Đặt số cột sản phẩm
     * @param column
     */
    public void setColumn(int column) {
        ((GridLayoutManager)(mRecycleView.getLayoutManager())).setSpanCount(column);
    }

    /**
     * Chuẩn bị layout control
     */
    @Override
    public void initLayout() {
        // Load layout view danh sách khách hàng
        View v = inflate(getContext(), R.layout.panel_product_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách khách hàng
        setLayoutItem(R.layout.card_product_list_content);

        // Chuẩn bị list danh sách khách hàng
        initRecycleView(R.id.product_list, new GridLayoutManager(this.getContext(), 1));

        mSearchProductTxt = (AutoCompleteTextView) findViewById(R.id.text_search_product);
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        String[] dogArr = getResources().getStringArray(R.array.dogs_list);
        List<String> dogList = Arrays.asList(dogArr);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), layoutItemId, dogList);
        mSearchProductTxt.setAdapter(adapter);
    }

    /**
     * Chuẩn bị layout giá trị
     */
    @Override
    public void initValue() {

    }

    @Override
    public void initModel() {

    }

    @Override
    protected void bindItem(View view, Product item, int position) {
        final CardProductListContentBinding binding = DataBindingUtil.bind(view);
        binding.setProduct(item);
    }

    /**
     * Trả lại danh sách khách hàng
     *
     * @return
     */
    public List<Product> getProductList() {
        return mList;
    }

    /**
     * Đặt danh sách khách hàng
     *
     * @param listProduct
     */
    public void setProductList(List<Product> listProduct) {
        mList = listProduct;
    }

    /**
     * Load danh sách sản phẩm
     */
//    public void loadProductList() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
//        {
//            mLoadProductTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else // Below Api Level 13
//        {
//            mLoadProductTask.execute();
//        }
//    }
//
//    /**
//     * Load ảnh các sản phẩm
//     */
//    public void loadProductImage() {
//        mLoadImageTask = new LoadProductImageTask(new ProductListPanel.LoadProductImageListener(), mListProduct);
//        if (mLoadImageTask != null)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
//            {
//                mLoadImageTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
//            } else // Below Api Level 13
//            {
//                mLoadImageTask.execute();
//            }
//    }
//
//    /**
//     * Xử lý các sự kiện khi load danh sách product lần đầu
//     */
//    public class LoadProductListener implements TaskListener<Void, Void, List<Product>> {
//        @Override
//        public void onPreController(Task task) {
//            showProgress(true);
//        }
//
//        @Override
//        public void onPostController(Task task, List<Product> productList) {
//            // Tất progress đi
//            mLoadProductTask = null;
//            showProgress(false);
//
//            // Hiển thị danh sách product
//            setProductList(productList);
//            if (mListProduct != null)
//                mProductListRecyclerView.setAdapter(new ProductListPanel.ProductListRecyclerViewAdapter(mListProduct));
//
//            // Tải ảnh và hiển thị luôn
//            loadProductImage();
//        }
//
//        @Override
//        public void onCancelController(Task task, Exception exp) {
//            mLoadProductTask = null;
//            showProgress(false);
//        }
//
//        @Override
//        public void onProgressController(Task task, Void... progress) {
//
//        }
//    }
//
//    /**
//     * Bắt các sự kiện load ảnh
//     */
//    public class LoadProductImageListener implements TaskListener<Void, Product, Void> {
//
//        @Override
//        public void onPreController(Task task) {
//
//        }
//
//        @Override
//        public void onPostController(Task task, Void aVoid) {
//
//        }
//
//        @Override
//        public void onCancelController(Task task, Exception exp) {
//
//        }
//
//        @Override
//        public void onProgressController(Task task, Product... progress) {
//
//        }
//    }
//
//    /**
//     * Chuyển đổi dataset product sang danh mục product
//     */
//    public class ProductListRecyclerViewAdapter
//            extends RecyclerView.Adapter<ProductListRecyclerViewAdapter.ProductListViewHolder> {
//        // đánh dấu vị trí đã chọn
//        private int selectedPos = 0;
//
//        // Danh sách product
//        private final List<Product> mListProduct;
//
//        /**
//         * Khởi tạo với danh sách product
//         * @param productList
//         */
//        public ProductListRecyclerViewAdapter(List<Product> productList) {
//            mListProduct = productList;
//        }
//
//        /**
//         * Tạo view cho từng product trong danh sách
//         * @param parent
//         * @param viewType
//         * @return
//         */
//        @Override
//        public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.card_product_list_content, parent, false);
//            return new ProductListViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ProductListViewHolder holder, final int position) {
//            Product product = mListProduct.get(position);
//            holder.mItem = product;
//
//            // Giữ tham chiếu imageview theo product
//            product.setRefer(LoadProductImageTask.KEY_IMAGEVIEW, holder.mImageView);
//
//            // Đặt các trường text vào danh sách
//            holder.mNameView.setText(product.getName());
//            holder.mPriceView.setText(ConfigUtil.formatPrice(product.getPrice()));
//            holder.mSKUView.setText(product.getSKU());
//            holder.mAvaibleView.setText(R.string.avaibles);
//
//            // highlight vị trí đã chọn
//            holder.itemView.setSelected(selectedPos == position);
//
//            // Gán ảnh cho view
//            Bitmap bmp = product.getBitmap();
//            if (bmp != null && !bmp.isRecycled()) holder.mImageView.setImageBitmap(bmp);
//
//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Update highlight sản phẩm đã chọn
//                    notifyItemChanged(selectedPos);
//                    selectedPos = position;
//                    notifyItemChanged(selectedPos);
//
//                    // gập nhật số lượng trên đơn hàng
//                    Product product = mListProduct.get(position);
//                    if (mProductListPanelListener != null)
//                        mProductListPanelListener.onSelectProduct(product);
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return mListProduct.size();
//        }
//
//        public class ProductListViewHolder extends RecyclerView.ViewHolder {
//            public final TextView mNameView;
//            public final ImageView mImageView;
//            public final TextView mPriceView;
//            public final TextView mAvaibleView;
//            public final TextView mSKUView;
//            public Product mItem;
//            public View mView;
//
//            public ProductListViewHolder(View view) {
//                super(view);
//                mImageView = (ImageView) view.findViewById(R.id.product_image);
//                mNameView = (TextView) view.findViewById(R.id.name);
//                mSKUView = (TextView) view.findViewById(R.id.sku);
//                mPriceView = (TextView) view.findViewById(R.id.price);
//                mAvaibleView = (TextView) view.findViewById(R.id.avaiable);
//                mView = view.findViewById(R.id.sales_product_list_card_view);
//            }
//        }
//    }
}
