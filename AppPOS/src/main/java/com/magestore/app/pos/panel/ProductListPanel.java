package com.magestore.app.pos.panel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.panel.SearchAutoCompletePanel;
import com.magestore.app.lib.view.item.ModelView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.SalesActivity;
import com.magestore.app.pos.controller.CategoryListController;
import com.magestore.app.pos.controller.ProductListController;
import com.magestore.app.pos.databinding.CardProductListContentBinding;
import com.magestore.app.pos.task.LoadProductImageTask;
import com.magestore.app.util.ConfigUtil;
import com.squareup.picasso.Picasso;

/**
 * Giao diện quản lý danh mục sản phẩm
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class ProductListPanel extends AbstractListPanel<Product> {
    Toolbar toolbar_category, toolbar_barcode;
    ImageView im_category_arrow;
    FrameLayout fr_category;
    SearchAutoCompletePanel mSearchAutoCompletePanel;

    // list category
    CategoryListController mCategoryListController;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public ProductListPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public ProductListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ProductListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Chỉ định list category
     *
     * @param mCategoryListController
     */
    public void setCategoryListController(CategoryListController mCategoryListController) {
        this.mCategoryListController = mCategoryListController;
    }

    /**
     * Chuẩn bị layout control
     */
    @Override
    public void initLayout() {
        super.initLayout();

        // Chuẩn bị list danh sách khách hàng
        fr_category = (FrameLayout) findViewById(R.id.fr_category);
        toolbar_category = (Toolbar) findViewById(R.id.toolbar_category);
        im_category_arrow = (ImageView) findViewById(R.id.im_category_arrow);

        mSearchAutoCompletePanel = (SearchAutoCompletePanel) findViewById(R.id.panel_search_product);
        toolbar_barcode = (Toolbar) findViewById(R.id.toolbar_barcode);

        toolbar_category.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fr_category.getVisibility() == VISIBLE) {
                    im_category_arrow.setRotation(0);
                    fr_category.setVisibility(GONE);
//                    mController.doRetrieve();
                    ((ProductListController) mController).bindCategory((Category) null);
                } else {
                    im_category_arrow.setRotation(180);
                    fr_category.setVisibility(VISIBLE);
                }
            }
        });

        toolbar_barcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
    }

    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        String cameraPermission = Manifest.permission.CAMERA;
        int permissionCheck = ContextCompat.checkSelfPermission(((ProductListController) mController).getMagestoreContext().getActivity(), cameraPermission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            scanBarcode();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((ProductListController) mController).getMagestoreContext().getActivity().requestPermissions(new String[]{cameraPermission}, SalesActivity.REQUEST_PERMISSON_CAMERA);
            }
        }
    }

    public void scanBarcode(){
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(((ProductListController) mController).getMagestoreContext().getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
//                        mSearchAutoCompletePanel.actionSearch();
                        mSearchAutoCompletePanel.getAutoTextView().setText(barcode.rawValue);
                        mSearchAutoCompletePanel.actionSearch();
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    /**
     * Hold ayout view của iten, gán findview id vào các biến
     *
     * @param view
     * @return
     */
    @Override
    protected RecycleViewItemHolder holdItemView(View view) {
        RecycleViewItemHolder viewHolder = new RecycleViewProductHolder(view);
        viewHolder.holdView(view);
        return viewHolder;
    }

    /**
     * Hold layout và nội dung các item trong view
     */
    public class RecycleViewProductHolder extends RecycleViewItemHolder {
        CardProductListContentBinding binding;
        TextView txtProductName;
        TextView txtSKU;
        TextView txtPrice;
        ImageView imgBitmap;
        ImageView im_stock;

        public RecycleViewProductHolder(View view) {
            super(view);
        }

        /**
         * Hold layout item trong view
         *
         * @param view
         */
        @Override
        public void holdView(View view) {
            super.holdView(view);
//            binding = DataBindingUtil.bind(view);

            txtProductName = ((TextView) view.findViewById(R.id.txt_product_name));
            txtPrice = ((TextView) view.findViewById(R.id.price));
            txtSKU = ((TextView) view.findViewById(R.id.sku));
            imgBitmap = (ImageView) view.findViewById(R.id.product_image);
            im_stock = (ImageView) view.findViewById(R.id.im_stock);
        }

        /**
         * Đặt nội dung item trong view
         *
         * @param item
         * @param position
         */
        public void setItem(ModelView item, int position) {
            super.setItem(item, position);

            // lấy product
            Product product = (Product) item.getModel();
            if (product == null) return;

            // gán giá trị của product vào
            txtProductName.setText(product.getName());
            txtPrice.setText(ConfigUtil.formatPriceProduct(product.getFinalPrice()));
//            txtSKU.setText(product.getSKU());
            im_stock.setVisibility(product.isInStock() ? GONE : VISIBLE);

            // gán ảnh vào
            Picasso.with(getContext()).load(product.getImage()).centerInside().resizeDimen(R.dimen.product_image_width, R.dimen.product_image_height).into(imgBitmap);
//            Glide.with(getContext()).load(product.getImage()).fitCenter().override(120, 120).into(imgBitmap);

            // Đặt tham chiếu imageview sang product
            product.setRefer(LoadProductImageTask.KEY_IMAGEVIEW, imgBitmap);
        }
    }
}
