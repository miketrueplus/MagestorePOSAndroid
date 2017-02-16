package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CategoryListController;
import com.magestore.app.pos.controller.ProductListController;
import com.magestore.app.pos.databinding.CardProductListContentBinding;
import com.magestore.app.pos.task.LoadProductImageTask;
import com.magestore.app.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Giao diện quản lý danh mục sản phẩm
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class ProductListPanel extends AbstractListPanel<Product> {
    Toolbar toolbar_category;
    ImageButton im_category_arrow;
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
        // Chuẩn bị list danh sách khách hàng
        fr_category = (FrameLayout) findViewById(R.id.fr_category);
        toolbar_category = (Toolbar) findViewById(R.id.toolbar_category);
        im_category_arrow = (ImageButton) findViewById(R.id.im_category_arrow);
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
        if (item == null) return;
//        final CardProductListContentBinding binding = DataBindingUtil.bind(view);
//        binding.setProduct(item);
        ((TextView) view.findViewById(R.id.txt_product_name)).setText(item.getName());
        ((TextView) view.findViewById(R.id.price)).setText(ConfigUtil.formatPrice(item.getPrice()));
        ((TextView) view.findViewById(R.id.sku)).setText(item.getSKU());

        // Hiện ảnh vào product nếu đã load được ảnh
        ImageView imageView = (ImageView) view.findViewById(R.id.product_image);
        Bitmap bmp = item.getBitmap();
        if (bmp != null && !bmp.isRecycled() && imageView != null) imageView.setImageBitmap(bmp);

        // Đặt tham chiếu imageview sang product
        item.setRefer(LoadProductImageTask.KEY_IMAGEVIEW, imageView);

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
}
