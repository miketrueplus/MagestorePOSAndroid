package com.magestore.app.pos.panel;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.view.item.ModelView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CategoryListController;
import com.magestore.app.pos.controller.ProductListController;
import com.magestore.app.pos.databinding.CardProductListContentBinding;
import com.magestore.app.pos.task.LoadProductImageTask;
import com.magestore.app.util.ConfigUtil;

/**
 * Giao diện quản lý danh mục sản phẩm
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class ProductListPanel extends AbstractListPanel<Product> {
    Toolbar toolbar_category;
    ImageView im_category_arrow;
    FrameLayout fr_category;
    CategoryListController mCategoryListController;

    public ProductListPanel(Context context) {
        super(context);
    }

    public ProductListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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

        toolbar_category.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fr_category.getVisibility() == VISIBLE){
                    im_category_arrow.setRotation(0);
                    fr_category.setVisibility(GONE);
                    ((ProductListController) mController).selectCategoryChild(null);
                }else{
                    im_category_arrow.setRotation(180);
                    fr_category.setVisibility(VISIBLE);
                }
            }
        });
    }

    /**
     * Hold ayout view của iten, gán findview id vào các biến
     * @param view
     * @return
     */
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

        public RecycleViewProductHolder(View view) {
            super(view);
        }

        /**
         * Hold layout item trong view
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
        }

        /**
         * Đặt nội dung item trong view
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
            txtPrice.setText(ConfigUtil.formatPrice(product.getPrice()));
//            txtSKU.setText(product.getSKU());

            // gán ảnh vào
            Bitmap bmp = product.getBitmap();
            if (bmp != null && !bmp.isRecycled() && imgBitmap != null) imgBitmap.setImageBitmap(bmp);

            // Đặt tham chiếu imageview sang product
            product.setRefer(LoadProductImageTask.KEY_IMAGEVIEW, imgBitmap);
        }
    }
}
