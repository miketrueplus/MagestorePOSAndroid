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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.databinding.PanelProductOptionListBinding;
import com.magestore.app.pos.model.catalog.PosProductOptionCustom;
import com.magestore.app.pos.model.catalog.PosProductOptionCustomValue;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
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
     *
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
     *
     * @param item
     */
    public void showCartItemInfo(CartItem item) {
        mBinding.setCartItem(item);
        mBinding.setProduct(item.getProduct());
        if (item.getProduct().getBitmap() != null)
            mImageProductDetail.setImageBitmap(item.getProduct().getBitmap());
    }

    /**
     * Save item vào cart
     *
     * @param view
     */
    public void onAddToCart(View view) {
        ((CartItemListController) getController()).updateToCart(bind2Item());
    }

    /**
     * Nhấn nút trừ số lượng
     *
     * @param view
     */
    public void onAddQuantity(View view) {
        ((CartItemListController) getController()).addQuantity(getItem());
        mBinding.setCartItem(getItem());
    }

    /**
     * Nhấn nút thêm số lượng
     *
     * @param view
     */
    public void onSubstractQuantity(View view) {
        ((CartItemListController) getController()).substractQuantity(getItem());
        mBinding.setCartItem(getItem());
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

        initValue();
    }

    @Override
    public void initValue() {
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    /**
     * Kiểm tra đã nhập liệu chưa
     *
     * @return
     */
    public boolean validateInput() {
        // duyệt tất cả các option custome để lấy option mà user đã chọn
        for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
            ProductOptionCustom productOptionCustom = expandableListAdapter.getGroup(i);
            for (int j = 0; j < expandableListAdapter.getChildrenCount(i); j++) {
                ProductOptionCustomValue productOptionCustomValue = expandableListAdapter.getChild(i, j);
                ProductOptionCustomValueHolder productOptionCustomValueViewHolder = expandableListAdapter.mProductOptionCustomHolderMap.get(productOptionCustom).mProductOptionCustomValueHolderList.get(j);
            }
        }

        return true;
    }

    @Override
    public CartItem bind2Item() {
        // lấy item tham chiếu
        CartItem item = super.bind2Item();
        if (expandableListAdapter == null) return item;

        // khởi tạo danh sách option được chọn
        Map<ProductOptionCustom, PosCartItem.ChooseProductOption> chooseProductOptionMap = item.getChooseProductOptions();
        if (chooseProductOptionMap == null) {
            chooseProductOptionMap = new HashMap<ProductOptionCustom, PosCartItem.ChooseProductOption>();
            item.setChooseProductOptions(chooseProductOptionMap);
        }

        // để tạo description cho product
        StringBuilder descriptionBuilder = new StringBuilder();
        // cập nhật giá cho item
        float unitPrice = getItem().getProduct().getFinalPrice();
        float basePrice = getItem().getProduct().getFinalPrice();
        // duyệt tất cả các option custome để lấy option mà user đã chọn
        for (int i = 0; i < getItem().getProduct().getProductOption().getCustomOptions().size(); i++) {
            // khởi tạo danh sách option value
            ProductOptionCustom productOptionCustom = getItem().getProduct().getProductOption().getCustomOptions().get(i);
            if (productOptionCustom.getOptionValueList() == null) continue;

            // thêm option được chọn vào
            PosCartItem.ChooseProductOption chooseProductOption = item.createChooseProductOption();
            chooseProductOptionMap.put(productOptionCustom, chooseProductOption);
            descriptionBuilder.append(i == 0 ? "" : ". ").append(productOptionCustom.getDisplayContent()).append(": ");

            boolean firstCustomValue = true;
            for (int j = 0; j < productOptionCustom.getOptionValueList().size(); j++) {
                ProductOptionCustomValue productOptionCustomValue = productOptionCustom.getOptionValueList().get(j);
//                ProductOptionCustomValueHolder productOptionCustomValueViewHolder = expandableListAdapter.mProductOptionCustomHolderMap.get(productOptionCustom).mProductOptionCustomValueHolderList.get(j);
                // nếu là loại chọn nhiều
                if (productOptionCustomValue.isChosen()) {
                    chooseProductOption.productOptionCustomValueList.add(productOptionCustomValue);
                    unitPrice += (productOptionCustom.isPriceTypePercent() ? basePrice : 1) * Float.parseFloat(productOptionCustomValue.getPrice());
                    descriptionBuilder.append(!firstCustomValue ? ", " : "").append(productOptionCustomValue.getDisplayContent());
                    firstCustomValue = false;
                }
            }
        }

        // trả lại cart item
        item.setUnitPrice(unitPrice);
        item.setItemDescription(descriptionBuilder.toString());
        return item;
    }

    /**
     * Cập nhật giá khi thay đổi option
     */
    public void updateCartItemPrice() {
        float price = getItem().getProduct().getFinalPrice();
        float basePrice = getItem().getProduct().getFinalPrice();

        // duyệt tất cả các option custome để tính lại đơn giá
        for (ProductOptionCustom productOptionCustom : getItem().getProduct().getProductOption().getCustomOptions()) {
            if (productOptionCustom.getOptionValueList() == null) continue;
            // khởi tạo danh sách option value
            for (ProductOptionCustomValue productOptionCustomValue : productOptionCustom.getOptionValueList()) {
                // nếu là loại chọn nhiều
                if (productOptionCustomValue.isChosen()) {
                    price += (productOptionCustom.isPriceTypePercent() ? basePrice : 1) * Float.parseFloat(productOptionCustomValue.getPrice());
                }
            }
        }
        getItem().setUnitPrice(price);
    }

    /**
     * Adapter nắm danh sách option để hiển thị
     */
    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private Product mProduct;
        private CartItem mCartItem;

        public Map<PosProductOptionCustom, ProductOptionCustomHolder> mProductOptionCustomHolderMap;

        /**
         * Hàm khởi tạo
         *
         * @param context
         */
        public CustomExpandableListAdapter(Context context) {
            this.context = context;
            mProductOptionCustomHolderMap = new HashMap<PosProductOptionCustom, ProductOptionCustomHolder>();
        }

        /**
         * Đặt product tham chiến
         *
         * @param product
         */
        public void setProduct(Product product) {
            mProduct = product;
        }

        /**
         * Đặt cartitem tham chiếu
         *
         * @param item
         */
        public void setCartItem(CartItem item) {
            mCartItem = item;
            mProduct = item.getProduct();
        }

        /**
         * Trả lại nút option value
         *
         * @param listPosition
         * @param expandedListPosition
         * @return
         */
        @Override
        public PosProductOptionCustomValue getChild(int listPosition, int expandedListPosition) {
            return mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList().get(expandedListPosition);
        }

        /**
         * ID của 1 nút option value
         *
         * @param listPosition
         * @param expandedListPosition
         * @return
         */
        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        /**
         * Chuẩn bị holder cho option value, với kiểu chọn 1
         *
         * @param convertView
         * @param productOptionCustom
         * @param viewHolder
         */
        private void initTypeChooseOneHolder(View convertView, ProductOptionCustom productOptionCustom, ProductOptionCustomValueHolder viewHolder) {
            viewHolder.mradChoose = (RadioButton) convertView.findViewById(R.id.id_radio_product_option_radio);
            viewHolder.mProductOptionCustom = productOptionCustom;
        }

        /**
         * Chuẩn bị holder cho option value, với kiểu chọn nhiều
         *
         * @param convertView
         * @param productOptionCustomer
         * @param viewHolder
         */
        private void initTypeChooseMultipeHolder(View convertView, ProductOptionCustom productOptionCustomer, ProductOptionCustomValueHolder viewHolder) {
            viewHolder.mchkChoose = (CheckBox) convertView.findViewById(R.id.id_checkbox_product_option_checkbox);
        }

        /**
         * Chuẩn bị holder cho option value, với kiểu chọn date time
         *
         * @param convertView
         * @param productOptionCustomer
         * @param viewHolder
         */
        private void initTypeDateTimeHolder(View convertView, ProductOptionCustom productOptionCustomer, ProductOptionCustomValueHolder viewHolder) {
            viewHolder.mdatePicker = (DatePicker) convertView.findViewById(R.id.id_datepicker_product_option_date);
            viewHolder.mtimePicker = (TimePicker) convertView.findViewById(R.id.id_timepicker_product_option_time);
        }

        /**
         * Khi click trên view
         *
         * @param v
         */
        private void onClickView(View v) {
            // tìm tất cả các radio trong cùng product option
            ProductOptionCustomValueHolder cviewHolder = (ProductOptionCustomValueHolder) v.getTag();
            if (cviewHolder.mProductOptionCustom.isTypeSelectOne())
                for (ProductOptionCustomValueHolder eachViewHolder : mProductOptionCustomHolderMap.get(cviewHolder.mProductOptionCustom).mProductOptionCustomValueHolderList) {
                    eachViewHolder.customValue.setChosen(false);
                }
            cviewHolder.customValue.setChosen(!cviewHolder.customValue.isChosen());
            expandableListAdapter.notifyDataSetChanged();

            // cập nhật lại giá
            updateCartItemPrice();
            mBinding.setCartItem(getItem());
        }

        /**
         * Tạo view với 1 option value
         *
         * @param listPosition
         * @param expandedListPosition
         * @param isLastChild
         * @param convertView
         * @param parent
         * @return
         */
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
                viewHolder.customValue = optionValue;
                viewHolder.mProductOptionCustom = productOptionCustom;

                // Xem kiểu option là gì để lựa chọn layout tương ứng
                ProductOptionCustom productOption = mProduct.getProductOption().getCustomOptions().get(listPosition);
                if (productOption.isTypeSelectMultipe()) {
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_checkbox, null);
                    initTypeChooseMultipeHolder(convertView, productOption, viewHolder);
                } else {
                    // còn lại quy về kiểu chọn 1
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_radio, null);
                    initTypeChooseOneHolder(convertView, productOption, viewHolder);
                }

                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickView(v);
                    }
                });

                // đặt giá trị cho hiển thị tên option và giá cả
                viewHolder.mtxtDisplay = (TextView) convertView
                        .findViewById(R.id.id_txt_product_option_display);
                viewHolder.mtxtPrice = (TextView) convertView
                        .findViewById(R.id.id_txt_product_option_price);


                convertView.setTag(viewHolder);
            }

            // lấy lại layout đã có trước đó
            if (convertView == null) return convertView;
            viewHolder = (ProductOptionCustomValueHolder) convertView.getTag();

            // bind giá trị vào
            viewHolder.mtxtDisplay.setText(optionValue.getDisplayContent());
            viewHolder.mtxtPrice.setText(ConfigUtil.formatPrice(optionValue.getPrice()));
            if (viewHolder.mradChoose != null) {
                viewHolder.mradChoose.setChecked(optionValue.isChosen());
                viewHolder.mradChoose.setSelected(optionValue.isChosen());
            }
            if (viewHolder.mchkChoose != null) {
                viewHolder.mchkChoose.setChecked(optionValue.isChosen());
                viewHolder.mchkChoose.setSelected(optionValue.isChosen());
            }

            // return view
            return convertView;
        }

        /**
         * Đếm số nút option value
         *
         * @param listPosition
         * @return
         */
        @Override
        public int getChildrenCount(int listPosition) {
            if (mProduct == null || mProduct.getProductOption() == null || mProduct.getProductOption().getCustomOptions() == null)
                return 0;
            if (mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList() == null)
                return 0;
            return mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList().size();
        }

        /**
         * Trả lại tham chiếu đến productoption custom
         *
         * @param listPosition
         * @return
         */
        @Override
        public PosProductOptionCustom getGroup(int listPosition) {
            return mProduct.getProductOption().getCustomOptions().get(listPosition);
        }

        /**
         * Đếm số nút option custom
         *
         * @return
         */
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
            ProductOptionCustomHolder viewHolder = null;
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

                // lưu hash map cho view holder này
                mProductOptionCustomHolderMap.put(viewHolder.productOptionCustom, viewHolder);

                // lưu view holder lại
                convertView.setTag(viewHolder);
            }
            if (convertView == null) return null;

            // gán giá trị cho view holder text view
            viewHolder = (ProductOptionCustomHolder) convertView.getTag();
            viewHolder.mtxtTitle.setText(getGroup(listPosition).getDisplayContent());

            ExpandableListView mExpandableListView = (ExpandableListView) parent;
            mExpandableListView.expandGroup(listPosition);

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
        boolean mblnChoosed = false;
        ProductOptionCustom mProductOptionCustom;
        public View view;
        public TextView mtxtDisplay;
        //        public TextView mtxtDisplaySub;
        public TextView mtxtPrice;
        public RadioButton mradChoose;
        public CheckBox mchkChoose;
        public DatePicker mdatePicker;
        public TimePicker mtimePicker;
        public PosProductOptionCustomValue customValue;
    }
}
