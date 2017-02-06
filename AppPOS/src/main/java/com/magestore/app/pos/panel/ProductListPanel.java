package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.res.TypedArray;
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

import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardProductListContentBinding;
import com.magestore.app.pos.task.LoadProductImageTask;


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
     * Đặt số cột sản phẩm
     * @param column
     */
//    public void setColumn(int column) {
//        ((GridLayoutManager)(mRecycleView.getLayoutManager())).setSpanCount(column);
//    }

    /**
     * Chuẩn bị layout control
     */
    @Override
    public void initLayout() {
        // Chuẩn bị list danh sách khách hàng
//        initRecycleView(getListLayout(), new GridLayoutManager(this.getContext(), 1));

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

        // Hiện ảnh vào product nếu đã load được ảnh
        ImageView imageView = (ImageView) view.findViewById(R.id.product_image);
        Bitmap bmp = item.getBitmap();
        if (bmp != null && !bmp.isRecycled() && imageView != null) imageView.setImageBitmap(bmp);

        // Đặt tham chiếu imageview sang product
        item.setRefer(LoadProductImageTask.KEY_IMAGEVIEW, imageView);
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
