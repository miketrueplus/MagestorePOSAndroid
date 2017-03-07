package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.PanelProductOptionListBinding;
import com.magestore.app.pos.model.catalog.PosProductOptionCustom;
import com.magestore.app.pos.model.catalog.PosProductOptionCustomValue;
import com.magestore.app.util.ConfigUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 3/2/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionPanel extends AbstractDetailPanel<CartItem> {
    PanelProductOptionListBinding mBinding;

    ExpandableListView expandableListView;
    ProductOptionPanel.CustomExpandableListAdapter expandableListAdapter;

    ImageView mImageProductDetail;

    public ProductOptionPanel(Context context) {
        super(context);
    }

    public ProductOptionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductOptionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Đưa item vào giao diện
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);

        if (expandableListAdapter != null) {
            expandableListAdapter.setProduct(item.getProduct());
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Xử lý hiển thị thông tin product và cart item
     * @param item
     */
    public void showCartItemInfo(CartItem item) {
        mBinding.setCartItem(item);
        mBinding.setProduct(item.getProduct());
        if (item.getProduct().getBitmap() != null) mImageProductDetail.setImageBitmap(item.getProduct().getBitmap());
    }

    /**
     * Save item vào cart
     * @param view
     */
    public void onAddToCart(View view) {

    }

    public void onAddQuantity(View view) {
    }

    public void onSubstractQuantity(View view) {

    }

    /**
     * Clear danh sách khi lần đầu hiện form
     */
    public void clearList() {
        if (expandableListAdapter != null) {
            expandableListAdapter.setProduct(null);
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Khởi tạo layout
     */
    @Override
    public void initLayout() {
        // đặt layout chung cả panel
//        super.initLayout();
        setLayoutPanel(R.layout.panel_product_option_list);

        // progress bar
        setProgressBar(R.id.id_product_option_progress);
        setTextViewMsg(R.id.id_product_option_msg);

        // imageview
        mImageProductDetail = (ImageView) findViewById(R.id.id_img_product_detail_image);

        // expan list view
        expandableListView = (ExpandableListView) findViewById(R.id.id_product_option_list);
        expandableListAdapter = new ProductOptionPanel.CustomExpandableListAdapter(getContext());
        expandableListView.setAdapter(expandableListAdapter);

        // binding
        mBinding = DataBindingUtil.bind(getView());
        mBinding.setPanel(this);
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private Product mProduct;
        public Map<PosProductOptionCustom, ProductOptionCustomHolder> mProductOptionCustomHolderMap;

        public CustomExpandableListAdapter(Context context) {
            this.context = context;
            mProductOptionCustomHolderMap = new HashMap<PosProductOptionCustom, ProductOptionCustomHolder>();
        }

        public void setProduct(Product product) {
            mProduct = product;
        }

        @Override
        public PosProductOptionCustomValue getChild(int listPosition, int expandedListPosition) {
            return mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList().get(expandedListPosition);
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        private void initTypeChooseOneHolder(View convertView, final ProductOptionCustom productOptionCustom, final ProductOptionCustomValueHolder viewHolder) {
            viewHolder.mradChoose = (RadioButton) convertView.findViewById(R.id.id_radio_product_option_radio);
            viewHolder.mradChoose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // tìm tất cả các radio trong cùng product option
                    for (ProductOptionCustomValueHolder eachViewHolder: mProductOptionCustomHolderMap.get(productOptionCustom).mProductOptionCustomValueHolderList) {
                        eachViewHolder.mradChoose.setSelected(eachViewHolder == viewHolder);
                        eachViewHolder.mradChoose.setChecked(eachViewHolder == viewHolder);
                    }
                }
            });
        }

        private void initTypeChooseMultipeHolder(View convertView, ProductOptionCustom productOptionCustomer, ProductOptionCustomValueHolder viewHolder) {
            viewHolder.mchkChoose = (CheckBox) convertView.findViewById(R.id.id_checkbox_product_option_checkbox);
        }

        private void initTypeDateTimeHolder(View convertView, ProductOptionCustom productOptionCustomer, ProductOptionCustomValueHolder viewHolder) {
            viewHolder.mdatePicker = (DatePicker) convertView.findViewById(R.id.id_datepicker_product_option_date);
            viewHolder.mtimePicker = (TimePicker) convertView.findViewById(R.id.id_timepicker_product_option_time);
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            // Tham chiếu option value
            PosProductOptionCustom productOptionCustom = getGroup(listPosition);
            PosProductOptionCustomValue optionValue = getChild(listPosition, expandedListPosition);
            ProductOptionCustomValueHolder viewHolder;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // Khởi tạo holder
                viewHolder = new ProductOptionCustomValueHolder();
                mProductOptionCustomHolderMap.get(productOptionCustom).mProductOptionCustomValueHolderList.add(viewHolder);

                // Xem kiểu option là gì để lựa chọn layout tương ứng
                ProductOptionCustom productOption = mProduct.getProductOption().getCustomOptions().get(listPosition);
                if (productOption.isTypeSelectMultipe()) {
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_checkbox, null);
                    initTypeChooseMultipeHolder(convertView, productOption, viewHolder);
                }
                if (productOption.isTypeDate()
                        || productOption.isTypeDateTime()
                        || productOption.isTypeTime()) {
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_datetime, null);
                    initTypeDateTimeHolder(convertView, productOption, viewHolder);
                }
                else {
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_radio, null);
                    initTypeChooseOneHolder(convertView, productOption, viewHolder);
                }

                // đặt giá trị cho hiển thị tên option và giá cả
                viewHolder.mtxtDisplay = (TextView) convertView
                        .findViewById(R.id.id_txt_product_option_display);
                viewHolder.mtxtPrice = (TextView) convertView
                        .findViewById(R.id.id_txt_product_option_price);

                // lưu tag lại với view
                convertView.setTag(viewHolder);
            }

            // lấy lại layout đã có trước đó
            if (convertView == null) return convertView;
            viewHolder = (ProductOptionCustomValueHolder) convertView.getTag();

            // bind giá trị vào
            viewHolder.mtxtDisplay.setText(optionValue.getDisplayContent());
            viewHolder.mtxtPrice.setText(ConfigUtil.formatPrice(optionValue.getPrice()));
            viewHolder.customerValue = optionValue;

            // return view
            return convertView;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            if (mProduct == null || mProduct.getProductOption() == null || mProduct.getProductOption().getCustomOptions() == null)
                return 0;
            if (mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList() == null)
                return 0;
            return mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList().size();
        }

        @Override
        public PosProductOptionCustom getGroup(int listPosition) {
            return mProduct.getProductOption().getCustomOptions().get(listPosition);
        }

        @Override
        public int getGroupCount() {
            if (mProduct == null || mProduct.getProductOption() == null || mProduct.getProductOption().getCustomOptions() == null)
                return 0;
            return mProduct.getProductOption().getCustomOptions().size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // chuẩn bị holder
            ProductOptionCustomHolder viewHolder;
            if (convertView == null) {
                // khởi tạo view
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.card_product_option_group, null);

                // khởi tạo view holder
                viewHolder = new ProductOptionCustomHolder();
                viewHolder.view = convertView;
                viewHolder.productOptionCustom = mProduct.getProductOption().getCustomOptions().get(listPosition);
                viewHolder.mProductOptionCustomValueHolderList = new ArrayList<>();
                viewHolder.mtxtTitle = (TextView) convertView.findViewById(R.id.listTitle);
                viewHolder.mtxtTitle.setTypeface(null, Typeface.BOLD);

                // lưu hash map cho view holder này
                mProductOptionCustomHolderMap.put(viewHolder.productOptionCustom, viewHolder);

                // lưu view holder lại
                convertView.setTag(viewHolder);
            }
            if (convertView == null) return null;

            // gán giá trị cho view holder text view
            String listTitle = getGroup(listPosition).getTitle();
            viewHolder = (ProductOptionCustomHolder) convertView.getTag();
            viewHolder.mtxtTitle.setText(listTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
    }

    /**
     * Nắm ngữ view và product option custom  tương ứng
     */
    public class ProductOptionCustomHolder {
        public View view;
        public List<ProductOptionCustomValueHolder> mProductOptionCustomValueHolderList;
        public PosProductOptionCustom productOptionCustom;
        public TextView mtxtTitle;
    }

    /**
     * Nắm ngữ view và product option custom value tương ứng
     */
    public class ProductOptionCustomValueHolder {
        public View view;
        public TextView mtxtDisplay;
//        public TextView mtxtDisplaySub;
        public TextView mtxtPrice;
        public RadioButton mradChoose;
        public CheckBox mchkChoose;
        public DatePicker mdatePicker;
        public TimePicker mtimePicker;
        public PosProductOptionCustomValue customerValue;
    }
}
