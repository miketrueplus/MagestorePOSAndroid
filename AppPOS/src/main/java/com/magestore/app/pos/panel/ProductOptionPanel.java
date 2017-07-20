package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.lib.view.ExpandableHeightGridView;
import com.magestore.app.lib.view.item.GenericModelView;
import com.magestore.app.lib.view.item.ModelView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.PanelProductOptionListBinding;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.model.catalog.PosProductOption;
import com.magestore.app.pos.model.catalog.PosProductOptionBundle;
import com.magestore.app.pos.model.catalog.PosProductOptionBundleItem;
import com.magestore.app.pos.model.catalog.PosProductOptionConfigOption;
import com.magestore.app.pos.model.catalog.PosProductOptionCustom;
import com.magestore.app.pos.model.catalog.PosProductOptionCustomValue;
import com.magestore.app.pos.model.catalog.PosProductOptionGrouped;
import com.magestore.app.pos.model.catalog.PosProductOptionJsonConfigAttributes;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.task.LoadProductImageTask;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextDecimal;
import com.magestore.app.view.EditTextQuantity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 3/2/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionPanel extends AbstractDetailPanel<CartItem> {
    // binding option, product và cartitem
    PanelProductOptionListBinding mBinding;

    // list view để xem các option
    ExpandableListView expandableListView;

    // button add to card
    Button mbtnAddToCart;

    // adapter chuyển data set option sang list view
    ProductOptionPanel.CustomExpandableListAdapter expandableListAdapter;

    // danh sách các option
    List<OptionModelView> mModelViewList;

    // ảnh chi tiết product
    ImageView mImageProductDetail;

    // text box số lượng
    EditTextQuantity mtxtCartItemQuantity;

    LinearLayout ll_description, ll_sku;
    boolean isShowDetail;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public ProductOptionPanel(Context context) {
        super(context);
    }

    CheckoutListController mCheckoutListController;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public void resetAdapter() {
        if (expandableListAdapter != null) {
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    public void setShowDetail(boolean showDetail) {
        isShowDetail = showDetail;
        if (ll_description != null) {
            ll_description.setVisibility(showDetail ? VISIBLE : GONE);
        }
        if (ll_sku != null) {
            ll_sku.setVisibility(showDetail ? VISIBLE : GONE);
        }
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public ProductOptionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
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

        if(item.getProduct() != null){
            mtxtCartItemQuantity.setDecimal(item.getProduct().isDecimal());
        }

        // đặt giá trị min
        mtxtCartItemQuantity.setMinValue(item.getProduct().getQuantityIncrement());
        mtxtCartItemQuantity.setError(null);

        // tạo model view list
        createModelViewList();

        // cập nhật list option
        if (expandableListAdapter != null) {
            expandableListAdapter.setCartItem(item);
            expandableListAdapter.notifyDataSetChanged();
        }

        // cập nhật lại giá
        updateCartItemPrice();
        mBinding.setCartItem(item);
    }

    /**
     * Xử lý hiển thị thông tin product và cart item
     *
     * @param item
     */
    public void showCartItemInfo(CartItem item) {
        // đặt cart và product hiển thị lên giao diện
        mBinding.setCartItem(item);
        mBinding.setProduct(item.getProduct());

        // nếu k0 có option, panel được mở trực tiếp từ long click product
        if (!item.getProduct().haveProductOption())
            super.bindItem(item);

        // hiển thị ảnh product
        Picasso.with(getContext()).load(item.getProduct().getImage()).centerInside().resizeDimen(R.dimen.product_image_detail_width, R.dimen.product_image_detail_height).into(mImageProductDetail);
//        Glide.with(getContext()).load(item.getProduct().getImage()).centerCrop().into(mImageProductDetail);

//        ImageView productImgView = ((ImageView) item.getProduct().getRefer(LoadProductImageTask.KEY_IMAGEVIEW));
//        if (productImgView != null) mImageProductDetail.setImageDrawable(productImgView.getDrawable());

//        if (item.getProduct().getBitmap() != null)
//            mImageProductDetail.setImageBitmap(item.getProduct().getBitmap());
    }

    /**
     * Save item vào cart
     *
     * @param view
     */
    public void onAddToCart(View view) {
        // validate input trước
        if (!validateInput()) return;

        // chia làm có option
        if (getItem().getProduct().haveProductOption()) {
            // xem loại option là grouped option hoặc không phải groped option
            // nếu không phải là grouped option tạo thành 1 cart item
            if (getItem().getProduct().getProductOption().getGroupedOptions() == null)
                ((CartItemListController) getController()).updateToCart(bind2Item());
            else { // nếu là grouped option, thì chia làm 2 cart item
                CartItem item = bind2Item();
                for (OptionModelView optionModel : mModelViewList) {
                    if (optionModel.quantity > 0)
                        ((CartItemListController) getController()).updateToCart(item, optionModel.getModel().getID(), optionModel.title, optionModel.quantity * item.getQuantity(), optionModel.price, optionModel.quantity_increment);
                }
                ((CartItemListController) getController()).closeAllOpeningDialog();
            }
        } else // và không option thì add như 1 product
            ((CartItemListController) getController()).updateToCartNoOption(bind2Item());

        mCheckoutListController.showButtonDiscount(true);
    }

    /**
     * Nhấn nút trừ số lượng
     *
     * @param view
     */
    public void onAddQuantity(View view) {
        mtxtCartItemQuantity.add(getItem().getProduct().getQuantityIncrement());
    }

    /**
     * Nhấn nút thêm số lượng
     *
     * @param view
     */
    public void onSubstractQuantity(View view) {
        mtxtCartItemQuantity.substract(getItem().getProduct().getQuantityIncrement());
    }

    public String getChooseValue(String code, List<PosCartItem.OptionsValue> optionsValueList) {
        if (optionsValueList == null) return StringUtil.STRING_EMPTY;
        for (PosCartItem.OptionsValue optionsValue : optionsValueList) {
            if (code.equals(optionsValue.code)) return optionsValue.value;
        }
        return StringUtil.STRING_EMPTY;
    }

    /**
     * So sánh cặp code value với cart item xem đã được chọn chưa
     *
     * @param code
     * @param value
     * @param optionsValueList
     * @return
     */
    public boolean isChooseValue(String code, String value, List<PosCartItem.OptionsValue> optionsValueList) {
        if (optionsValueList == null) return false;
        for (PosCartItem.OptionsValue optionsValue : optionsValueList) {
            if (code.equals(optionsValue.code) && value.equals(optionsValue.value)) return true;
        }
        return false;
    }

    /**
     * So sánh cặp code value với cart item xem có cái nào được chọn chưa
     *
     * @param code
     * @param optionsValueList
     * @return
     */
    public boolean haveChooseValue(String code, List<PosCartItem.OptionsValue> optionsValueList) {
        if (optionsValueList == null) return false;
        for (PosCartItem.OptionsValue optionsValue : optionsValueList) {
            if (code.equals(optionsValue.code)) return true;
        }
        return false;
    }

    /**
     * Tìm trong option đã chọn là cái option nào
     *
     * @return
     */
    public String getOptionValue(String code, List<PosCartItem.OptionsValue> optionsValueList, String defaultReturn) {
        if (optionsValueList == null) return defaultReturn;
        for (PosCartItem.OptionsValue optionsValue : optionsValueList) {
            if (code.equals(optionsValue.code)) return optionsValue.value;
        }
        return defaultReturn;
    }

    /**
     * Tạo model view list từ option custom
     */
    private void createModelViewListFromOptionCustom() {
        // tạo model view tương ứng cho mỗi option custom
        for (ProductOptionCustom optionCustom : getItem().getProduct().getProductOption().getCustomOptions()) {
            // khởi tạo model view
            OptionModelView optionModelView = new OptionModelView();
            optionModelView.optionValueModelViewList = new ArrayList<>();
            optionModelView.title = optionCustom.getTitle();
            optionModelView.quantity = 1;
            optionModelView.is_required = optionCustom.isRequired();
            optionModelView.option_type = ProductOptionCustom.OPTION_TYPE_CUSTOM;
            optionModelView.input_type = optionCustom.getType();
            optionModelView.setModel(optionCustom);

            // tạo model view tương ứng mỗi option value
            if (optionCustom.getOptionValueList() == null) {
                if (optionModelView.isTypeField()) {
                    OptionValueModelView optionValueModelView = new FieldValueModelView();
                    optionValueModelView.id = getChooseValue(optionModelView.getModel().getID(), getItem().getOptions());
                    optionValueModelView.title = optionValueModelView.id;
                    optionValueModelView.choose = !StringUtil.isNullOrEmpty(optionValueModelView.id);
                    optionValueModelView.price_type = optionCustom.getPriceType();
                    optionValueModelView.price =
                            optionValueModelView.isPriceTypePercent()
                                    ? "" + (Float.parseFloat(optionCustom.getPrice()) * getItem().getProduct().getFinalPrice() / 100)
                                    : optionCustom.getPrice();
                    optionValueModelView.setModel(optionCustom);
                    optionModelView.optionValueModelViewList.add(optionValueModelView);
                }
                mModelViewList.add(optionModelView);
                continue;
            }
            for (ProductOptionCustomValue optionValue : optionCustom.getOptionValueList()) {
                OptionValueModelView optionValueModelView = new OptionValueModelView();
                optionValueModelView.optionModelView = optionModelView;
                optionValueModelView.id = optionValue.getID();
                optionValueModelView.choose = isChooseValue(optionModelView.getModel().getID(), optionValueModelView.id, getItem().getOptions());
                optionValueModelView.title = optionValue.getTitle();
                optionValueModelView.price_type = optionValue.getPriceType();
                optionValueModelView.price =
                        optionValueModelView.isPriceTypePercent()
                                ? "" + (Float.parseFloat(optionValue.getPrice()) * getItem().getProduct().getFinalPrice() / 100)
                                : optionValue.getPrice();
                optionValueModelView.setModel(optionValue);

                optionModelView.optionValueModelViewList.add(optionValueModelView);
            }
            mModelViewList.add(optionModelView);
        }
    }

    /**
     * Tạo model view từ config option
     */
    private void createModelViewListFromOptionConfig() {
        // tạo model view tương ứng cho mỗi option custom
        Map<String, PosProductOptionConfigOption> configOptionMap = getItem().getProduct().getProductOption().getConfigurableOptions();

        if (configOptionMap != null) {
            // với mỗi configurable trong option, tạo custom option tương ứng
            for (String optionCode : configOptionMap.keySet()) {
                PosProductOptionConfigOption configOption = configOptionMap.get(optionCode);
                OptionModelView optionModelView = new OptionModelView();
                optionModelView.optionValueModelViewList = new ArrayList<>();
                optionModelView.code = optionCode;
                optionModelView.title = configOption.optionLabel;
                optionModelView.quantity = 1;
                optionModelView.is_required = true;
                optionModelView.option_type = ProductOptionCustom.OPTION_TYPE_CONFIG;
                if (optionCode.equals("color")) {
                    optionModelView.input_type = ProductOptionCustom.TYPE_COLOR;
                } else if (optionCode.equals("size")) {
                    optionModelView.input_type = ProductOptionCustom.TYPE_SIZE;
                }
                optionModelView.quantity = Float.parseFloat(getOptionValue(configOption.getID(), getItem().getBundleOptionQuantity(), StringUtil.STRING_ONE));
                optionModelView.setModel(configOption);

                // tạo model view tương ứng mỗi option value
                if (configOption.optionValues == null) continue;
                for (String key : configOption.optionValues.keySet()) {
                    OptionValueModelView optionValueModelView = new OptionValueModelView();
                    optionValueModelView.optionModelView = optionModelView;
                    optionValueModelView.id = key;
                    optionValueModelView.title = configOption.optionValues.get(key);
                    optionValueModelView.price = StringUtil.STRING_ZERO;
                    optionValueModelView.choose = isChooseValue(optionModelView.getModel().getID(), optionValueModelView.id, getItem().getSuperAttribute());
                    for (PosProductOptionJsonConfigAttributes.Option attribute : getItem().getProduct().getProductOption().getJsonConfig().attributes.get(configOption.optionId).options) {
                        if (key.equals(attribute.id)) {
                            optionValueModelView.productList = attribute.products;
                            break;
                        }
                    }
                    optionModelView.optionValueModelViewList.add(optionValueModelView);
                }
                mModelViewList.add(optionModelView);
            }
        }
    }

    /**
     * Tạo model view list từ option bundle
     */
    private void createModelViewListFromOptionBundle() {
        // convert option theo bundle options sang 1 format thống nhất
        List<PosProductOptionBundle> bundleList = getItem().getProduct().getProductOption().getBundleOptions();
        if (bundleList != null) {
            for (PosProductOptionBundle bundle : bundleList) {
                OptionModelView optionModelView = new OptionModelView();
                optionModelView.optionValueModelViewList = new ArrayList<>();
                optionModelView.title = bundle.getTitle();
                optionModelView.is_required = bundle.isRequired();
                optionModelView.option_type = ProductOptionCustom.OPTION_TYPE_BUNDLE;
                optionModelView.input_type = bundle.getType();
                optionModelView.quantity = Float.parseFloat(getOptionValue(bundle.getID(), getItem().getBundleOptionQuantity(), StringUtil.STRING_ONE));
                optionModelView.setModel(bundle);

                if (bundle.getItems() == null) continue;
                boolean hasChooseValue = haveChooseValue(optionModelView.getModel().getID(), getItem().getBundleOption());
                for (PosProductOptionBundleItem bundleItem : bundle.getItems()) {
                    OptionValueModelView optionValueModelView = new OptionValueModelView();
                    optionValueModelView.optionModelView = optionModelView;
                    optionValueModelView.id = bundleItem.getSelectionId();
                    optionValueModelView.title = bundleItem.getName();
                    optionValueModelView.price = bundleItem.getPrice();
                    optionValueModelView.choose = isChooseValue(optionModelView.getModel().getID(), optionValueModelView.id, getItem().getBundleOption()) ?
                            true :
                            (hasChooseValue ? false : bundleItem.isDefault());// nếu không được chọn thì xem thử có phải là mặc định hay không
                    optionValueModelView.setModel(bundleItem);
                    optionModelView.optionValueModelViewList.add(optionValueModelView);
                }

                // thêm một list giả cuối để đặt số lượng
                FakeValueModelView optionValueModelView = new FakeValueModelView();
                optionValueModelView.optionModelView = optionModelView;
                optionValueModelView.choose = false;
                optionModelView.optionValueModelViewList.add(optionValueModelView);

                // thêm vào danh sách option
                mModelViewList.add(optionModelView);
            }
        }
    }

    /**
     * Tạo model view list từ option grouped
     */
    private void createModelViewListFromOptionGrouped() {
        // convert option theo group options sang 1 format thống nhất
        List<PosProductOptionGrouped> groupedList = getItem().getProduct().getProductOption().getGroupedOptions();
        if (groupedList != null) {
            findViewById(R.id.id_layout_product_option_cart_item_quantity).setVisibility(View.GONE);
            findViewById(R.id.id_layout_product_option_cart_item_quantity_label).setVisibility(View.GONE);

            for (PosProductOptionGrouped groupedOption : groupedList) {
                OptionModelView optionModelView = new OptionModelView();
                optionModelView.optionValueModelViewList = new ArrayList<>();
                optionModelView.title = groupedOption.getName();
                optionModelView.quantity = groupedOption.getDefaultQty();
                optionModelView.quantity_increment = groupedOption.getQuantityIncrement();
                optionModelView.is_required = false;
                optionModelView.option_type = ProductOptionCustom.OPTION_TYPE_GROUPED;
                optionModelView.input_type = ProductOptionCustom.TYPE_GROUP;
                optionModelView.price = groupedOption.getPrice();
                optionModelView.setModel(groupedOption);

                // thêm một list giả cuối để đặt số lượng
                FakeValueModelView optionValueModelView = new FakeValueModelView();
                optionValueModelView.optionModelView = optionModelView;
                optionValueModelView.choose = true;
                optionValueModelView.price = Float.toString(groupedOption.getPrice());
                optionModelView.optionValueModelViewList.add(optionValueModelView);

                // thêm vào danh sách option
                mModelViewList.add(optionModelView);
            }
        }
    }

    /**
     * Tạo model view
     */
    public void createModelViewList() {
        mModelViewList = new ArrayList<OptionModelView>();
        findViewById(R.id.id_layout_product_option_cart_item_quantity).setVisibility(View.VISIBLE);
        findViewById(R.id.id_layout_product_option_cart_item_quantity_label).setVisibility(View.VISIBLE);

        // tạo model view từ config option
        createModelViewListFromOptionConfig();

        // tạo model view list với custom option
        createModelViewListFromOptionCustom();

        // tạo model view từ config bundle
        createModelViewListFromOptionBundle();

        // tạo model view từ config grouped
        createModelViewListFromOptionGrouped();
    }

    /**
     * Model view cho mỗi option value
     */
    class OptionModelView extends GenericModelView {
        public OptionModelViewHolder holder;
        public String title;
        public String input_type;
        public String option_type;
        public boolean is_required;
        public List<OptionValueModelView> optionValueModelViewList;
        public float quantity;
        public float quantity_increment = 1;
        public float price;
        public String code;

        public boolean isCustomOption() {
            return (option_type == null) || ProductOptionCustom.OPTION_TYPE_CUSTOM.compareToIgnoreCase(option_type) == 0;
        }

        public boolean isConfigOption() {
            return ProductOptionCustom.OPTION_TYPE_CONFIG.compareToIgnoreCase(option_type) == 0;
        }

        public boolean isBundleOption() {
            return ProductOptionCustom.OPTION_TYPE_BUNDLE.compareToIgnoreCase(option_type) == 0;
        }

        public boolean isGroupOption() {
            return ProductOptionCustom.OPTION_TYPE_GROUPED.compareToIgnoreCase(option_type) == 0;
        }

        public boolean isTypeField() {
            return ProductOptionCustom.TYPE_FIELD.compareToIgnoreCase(input_type) == 0;
        }

        public boolean isTypeSelectOne() {
            return ProductOptionCustom.TYPE_RADIO.compareToIgnoreCase(input_type) == 0 || ProductOptionCustom.TYPE_DROP_DOWN.compareToIgnoreCase(input_type) == 0;
        }

        public boolean isTypeColor() {
            return ProductOptionCustom.TYPE_COLOR.compareToIgnoreCase(input_type) == 0;
        }

        public boolean isTypeSize() {
            return ProductOptionCustom.TYPE_SIZE.compareToIgnoreCase(input_type) == 0;
        }

        public boolean isTypeSelectMultipe() {
            return ProductOptionCustom.TYPE_CHECKBOX.compareToIgnoreCase(input_type) == 0 || ProductOptionCustom.TYPE_MULTIPE.compareToIgnoreCase(input_type) == 0 || ProductOptionCustom.TYPE_MULTI.compareToIgnoreCase(input_type) == 0;
        }

        public boolean isTypeTime() {
            return ProductOptionCustom.TYPE_TIME.compareToIgnoreCase(input_type) == 0;
        }

        public boolean isTypeDate() {
            return ProductOptionCustom.TYPE_DATE.compareToIgnoreCase(input_type) == 0;
        }

        public boolean isTypeDateTime() {
            return ProductOptionCustom.TYPE_DATETIME.compareToIgnoreCase(input_type) == 0;
        }
    }

    /**
     * Model view giả cho phần nhập số lượng
     */
    class FakeValueModelView extends OptionValueModelView {
    }

    /**
     * Model view giả cho phần nhập text file của option custom
     */
    class FieldValueModelView extends OptionValueModelView {
    }

    /**
     * View option tổng
     */
    class OptionValueModelView extends GenericModelView {
        public OptionValueModelViewHolder holder;
        public List<String> productList;
        public String title;
        public String price;
        public String price_type;
        public String id;
        public OptionModelView optionModelView;
        public boolean choose;
        public boolean disable;
        ExpandableHeightGridView gridView;

        public boolean isPriceTypeFixed() {
            return !ProductOptionCustom.PRICE_TYPE_PERCENT.equals(price_type);
        }

        public boolean isPriceTypePercent() {
            return ProductOptionCustom.PRICE_TYPE_PERCENT.equals(price_type);
        }
    }

    /**
     * Clear danh sách khi lần đầu hiện form
     */
    public void clearList() {
        if (expandableListAdapter != null) {
            expandableListAdapter.clearList();
            expandableListAdapter.setCartItem(null);
            expandableListAdapter.notifyDataSetChanged();
        }

        // reset adapter
        expandableListAdapter = new ProductOptionPanel.CustomExpandableListAdapter(getContext());
        expandableListView.setAdapter(expandableListAdapter);

        // clear list
        if (mModelViewList != null) mModelViewList.clear();
        mModelViewList = null;
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


        ll_description = (LinearLayout) findViewById(R.id.ll_description);
        ll_description.setVisibility(isShowDetail ? VISIBLE : GONE);

        ll_sku = (LinearLayout) findViewById(R.id.ll_sku);
        ll_sku.setVisibility(isShowDetail ? VISIBLE : GONE);

        // imageview
        mImageProductDetail = (ImageView) findViewById(R.id.id_img_product_detail_image);

        // button add to cart
        mbtnAddToCart = (Button) findViewById(R.id.id_btn_product_option_add_to_cart);

        // expan list view
        expandableListView = (ExpandableListView) findViewById(R.id.id_product_option_list);
        expandableListAdapter = new ProductOptionPanel.CustomExpandableListAdapter(getContext());
        expandableListView.setAdapter(expandableListAdapter);

        // text box số lượng
        mtxtCartItemQuantity = (EditTextQuantity) findViewById(R.id.id_txt_product_option_cart_item_quantity);
        mtxtCartItemQuantity.setProductDetail(true);

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
        // k0 validate nữa nếu là k0 có option
        if (!getItem().getProduct().haveProductOption()) return true;

        boolean blnValid = true;

        // kiểm tra số lượng zero
        if (mtxtCartItemQuantity.getValueFloat() < getItem().getProduct().getQuantityIncrement()) {
            blnValid = false;
            mtxtCartItemQuantity.setError(String.format(getResources().getString(R.string.err_field_must_greater_than), ConfigUtil.formatQuantity(getItem().getProduct().getQuantityIncrement())));
        }

        // duyệt tất cả các option custome để lấy option mà user đã chọn
        if (mModelViewList == null) return false;
        if (mModelViewList.size() == 0) return false;
        if (!mModelViewList.get(0).isGroupOption()) {
            boolean checkRequied = true;
            for (OptionModelView optionModelView : mModelViewList) {
                // check xem có option value nào được chọn không
                boolean haveChoose = false;
                if (optionModelView.optionValueModelViewList == null) continue;
                for (OptionValueModelView valueModelView : optionModelView.optionValueModelViewList) {
                    if (valueModelView.choose) {
                        haveChoose = true;
                        break;
                    }
                }

                // nếu k0 có thì báo lỗi thông báo lỗi chưa chọn option
                if (optionModelView.holder == null) continue;
                if (optionModelView.is_required && !haveChoose) {
                    checkRequied = false;
                    blnValid = false;
                    optionModelView.holder.mtxtError.setText(getContext().getString(R.string.err_field_required));
                    optionModelView.holder.mtxtError.setError(getContext().getString(R.string.err_field_required));
                } else {
                    optionModelView.holder.mtxtError.setText(null);
                    optionModelView.holder.mtxtError.setError(null);
                }
            }
            if (!checkRequied) {
                Toast.makeText(getContext(), getContext().getString(R.string.err_requied_options), Toast.LENGTH_SHORT).show();
            }
        } else {
            // với tình huống group không chấp nhận tất cả zero
            blnValid = false;
            for (OptionModelView optionModelView : mModelViewList) {
                if (optionModelView.quantity > 0) {
                    blnValid = true;
                    break;
                }
            }

            for (OptionModelView optionModelView : mModelViewList) {
                if (blnValid) {
                    optionModelView.holder.mtxtError.setText(null);
                    optionModelView.holder.mtxtError.setError(null);
                } else {
                    optionModelView.holder.mtxtError.setText(getContext().getString(R.string.err_field_quantity_not_specific));
                    optionModelView.holder.mtxtError.setError(getContext().getString(R.string.err_field_quantity_not_specific));
                }
            }
        }

        // nếu config option nhưng không có product do bị disable
        if (blnValid && StringUtil.STRING_EMPTY.equals(getConfigOptionProductID())) {
            blnValid = false;
            showErrMsgDialog(getContext().getString(R.string.err_cannot_choose_these_options));
        }

        return blnValid;
    }

    @Override
    public void bind2Item(CartItem item) {
        if (expandableListAdapter == null) return;

        // đặt số lượng
        item.setQuantity(mtxtCartItemQuantity.getValueFloat());

        // nếu k0 có option, tương đương product detail, k0 phải chuyển input từ option sang nữa
        if (!item.getProduct().haveProductOption()) return;

        // để tạo description cho product
        StringBuilder descriptionBuilder = new StringBuilder();

        // clear các option cũ
        item.clearOption();

        // duyệt tất cả các option để lấy option mà user đã chọn
        boolean firstOption = true;
        for (OptionModelView optionModelView : mModelViewList) {
            boolean firstOptionValue = true;
            for (OptionValueModelView optionValueModelView : optionModelView.optionValueModelViewList) {
                // với các trường hợp là ô giả để nhập text
                if (optionValueModelView instanceof FakeValueModelView) {
                    continue;
                }
                if (optionValueModelView.choose) {
                    // điền description cho cart item
                    if (firstOptionValue) {
                        descriptionBuilder.append(firstOption ? StringUtil.STRING_EMPTY : StringUtil.STRING_COMMA_SPACE).append(optionModelView.title);
                        firstOption = false;
                    }
                    descriptionBuilder.append(firstOptionValue ? StringUtil.STRING_COLON_SPACE : StringUtil.STRING_COMMA_SPACE).append(optionValueModelView.title);
                    firstOptionValue = false;

                    // chèn các option do user chọn vào cart item
                    if (PosProductOptionCustom.OPTION_TYPE_CUSTOM.equals(optionModelView.option_type)) {
                        item.insertOption(optionModelView.getModel().getID(), optionValueModelView.id);
                    } else if (PosProductOptionCustom.OPTION_TYPE_CONFIG.equals(optionModelView.option_type))
                        item.insertSuperAttribute(optionModelView.getModel().getID(), optionValueModelView.id);
                    else if (PosProductOptionCustom.OPTION_TYPE_BUNDLE.equals(optionModelView.option_type)) {
                        item.insertBundleOption(optionModelView.getModel().getID(), optionValueModelView.id);
                    }
                }
            }

            // bổ sung số lượng
            if (PosProductOptionCustom.OPTION_TYPE_BUNDLE.equals(optionModelView.option_type)) {
                item.insertBundleOptionQuantity(optionModelView.getModel().getID(), StringUtil.STRING_EMPTY + optionModelView.quantity);
            }
        }

        // trả lại cart item
        item.setItemDescription(descriptionBuilder.toString());
    }

    /**
     * Cập nhật giá khi thay đổi option
     */
    public void updateCartItemPrice() {
        float price = getItem().getProduct().getFinalPrice();
        float basePrice = getItem().getProduct().getFinalPrice();
        float addPrice = 0.0f;

        // sử dụng để tính price với config option
        List<String> productsList = null;

        // duyệt tất cả các option custome để tính lại đơn giá
        for (OptionModelView optionModelView : mModelViewList) {
            // với config option, price sẽ được tính căn cứ mô hình khác
            if (optionModelView.isConfigOption()) {
                for (OptionValueModelView optionValueModelView : optionModelView.optionValueModelViewList) {
                    // nếu là loại chọn nhiều
                    if (optionValueModelView.choose) {
                        if (productsList == null) {
                            productsList = new ArrayList<>();
                            productsList.addAll(optionValueModelView.productList);
                        } else productsList.retainAll(optionValueModelView.productList);
                    }
                }
                continue;
            }

            // các loại option khác, tính giá theo từng item đã được lựa chọn.
            if (optionModelView.optionValueModelViewList == null) continue;
            // khởi tạo danh sách option value
            for (OptionValueModelView optionValueModelView : optionModelView.optionValueModelViewList) {
                // nếu là loại chọn nhiều
                if (optionValueModelView.choose) {
                    addPrice += Float.parseFloat(optionValueModelView.price) * optionModelView.quantity;
                }
            }
        }

        // với price của option config thì chọn ra 1 product chung và tính lại price
        if (productsList != null && productsList.size() == 1) {
            String productID = productsList.get(0);
            try {
                float configPrice = getItem().getProduct().getProductOption().getJsonConfig().optionPrices.get(productID).getFinalPrice();
                price = configPrice;
            } catch (Exception exp) {
                price = basePrice;
            }
        }

        // cập nhật lại đơn giá
        price += addPrice;
        getItem().setUnitPrice(price);
        getItem().setCustomPrice(price);
        getItem().setDefaultCustomPrice(price);
        getItem().setPriceShowView(price);
    }

    private String getConfigOptionProductID() {
        if (getItem().getProduct().getProductOption().getJsonConfig() == null) return null;

        // sử dụng để tính price với config option
        List<String> productsList = null;

        // duyệt tất cả các option custome để tính lại đơn giá
        for (OptionModelView optionModelView : mModelViewList) {
            // với config option, price sẽ được tính căn cứ mô hình khác
            if (optionModelView.isConfigOption()) {
                for (OptionValueModelView optionValueModelView : optionModelView.optionValueModelViewList) {
                    // nếu là loại chọn nhiều
                    if (optionValueModelView.choose) {
                        if (productsList == null) {
                            productsList = new ArrayList<>();
                            productsList.addAll(optionValueModelView.productList);
                        } else productsList.retainAll(optionValueModelView.productList);
                    }
                }
                continue;
            }
        }

        if (productsList == null || productsList.size() <= 0) return StringUtil.STRING_EMPTY;
        return productsList.get(0);
    }

    /**
     * Hiển thị progress
     *
     * @param blnShow
     */
    @Override
    public void showProgress(boolean blnShow) {
        super.showProgress(blnShow);

        // k0 ấn add to cart được nếu vẫn đang load product option
        mbtnAddToCart.setEnabled(blnShow ? false : true);
    }

    /**
     * Tất cả các progress bar
     */
    @Override
    public void hideAllProgressBar() {
        super.hideAllProgressBar();

        // k0 ấn add to cart được nếu vẫn đang load product option
        mbtnAddToCart.setEnabled(true);
    }

    /**
     * Adapter nắm danh sách option để hiển thị
     */
    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private CartItem mCartItem;

        /**
         * Hàm khởi tạo
         *
         * @param context
         */
        public CustomExpandableListAdapter(Context context) {
            this.context = context;
        }

        public void clearList() {
        }

        /**
         * Đặt cartitem tham chiếu
         *
         * @param item
         */
        public void setCartItem(CartItem item) {
            mCartItem = item;
        }

        /**
         * Trả lại nút option value
         *
         * @param listPosition
         * @param expandedListPosition
         * @return
         */
        @Override
        public OptionValueModelView getChild(int listPosition, int expandedListPosition) {
            if (mModelViewList == null) return null;
            return mModelViewList.get(listPosition).optionValueModelViewList.get(expandedListPosition);
        }

        /**
         * Trả lại tham chiếu đến productoption custom
         *
         * @param listPosition
         * @return
         */
        @Override
        public OptionModelView getGroup(int listPosition) {
            if (mModelViewList == null) return null;
            return mModelViewList.get(listPosition);
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
         */
        private void initTypeChooseOneHolder(View convertView, OptionValueModelView optionModelView) {
            optionModelView.holder.mradChoose = (RadioButton) convertView.findViewById(R.id.id_radio_product_option_radio);
        }

        /**
         * Chuẩn bị holder cho option value, với kiểu text field
         *
         * @param convertView
         */
        private void initTypeField(View convertView, final OptionValueModelView optionValueModelView) {
            optionValueModelView.holder.mtxtField = (EditText) convertView.findViewById(R.id.id_txt_product_option_field);
            optionValueModelView.holder.mtxtField.setHint(String.format(getResources().getString(R.string.err_field_max_character), "67"));
            // khi thôi focus
            optionValueModelView.holder.mtxtField.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        optionValueModelView.title = optionValueModelView.holder.mtxtField.getText().toString().trim();
                        optionValueModelView.id = optionValueModelView.title;
                        optionValueModelView.choose = !StringUtil.STRING_EMPTY.equals(optionValueModelView.id);
                        updateCartItemPrice();
                        mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPriceProduct(getItem().getPrice()));
                    } else {
                        // fill giá trị vào, nguyên số để edit, bỏ ký tự tiền
//                        optionValueModelView.holder.mtxtField.selectAll();
                    }
                }
            });
        }

        /*
         * Chuẩn bị holder cho option value với kiểu quantity
         */
        private void initTypeQuantity(View convertView, final OptionValueModelView optionValueModelView) {
            optionValueModelView.holder.mtxtQuantity = (EditTextQuantity) convertView
                    .findViewById(R.id.id_txt_product_option_quantity);

            final OptionModelView optionModelView = optionValueModelView.optionModelView;
            optionValueModelView.holder.mtxtQuantity.setText(String.valueOf(optionModelView.quantity_increment));
            optionValueModelView.holder.mtxtQuantity.setMinValue(optionModelView.quantity_increment);

            // khi thôi focus
            optionValueModelView.holder.mtxtQuantity.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // lấy giá trị trong ô text, căn lại giữa max và min
                        optionModelView.quantity = ConfigUtil.parseFloat(ConfigUtil.truncateFloatDigit(optionValueModelView.holder.mtxtQuantity.getText().toString()));
                        if (optionModelView.quantity < 1) optionModelView.quantity = 1;
                        updateCartItemPrice();
                        mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPriceProduct(getItem().getPrice()));
//                                expandableListAdapter.notifyDataSetChanged();
//                                mBinding.setCartItem(getItem());
//                                optionValueModelView.holder.mtxtQuantity.setText(ConfigUtil.formatNumber(optionModelView.quantity));
                        clearFocus();
                    } else {
                        // fill giá trị vào, nguyên số để edit, bỏ ký tự tiền
                        optionValueModelView.holder.mtxtQuantity.selectAll();
                    }
                }
            });

            // khi ấn trừ số lượng
            View subView = (convertView.findViewById(R.id.id_txt_product_option_quantity_substract));
            subView.setTag(optionModelView);
            subView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionValueModelView.holder.mtxtQuantity.substract(optionModelView.quantity_increment);
                    onSubtractOptionQuantity((OptionModelView) v.getTag(), optionValueModelView.holder);
                }
            });

            // khi ấn tăng số lượng
            View addView = (convertView.findViewById(R.id.id_txt_product_option_quantity_add));
            addView.setTag(optionModelView);
            addView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionValueModelView.holder.mtxtQuantity.add(optionModelView.quantity_increment);
                    onAddOptionQuantity((OptionModelView) v.getTag(), optionValueModelView.holder);
                }
            });
        }

        /**
         * Chuẩn bị holder cho option value, với kiểu chọn nhiều
         *
         * @param convertView
         */
        private void initTypeChooseMultipeHolder(View convertView, OptionValueModelView optionModelView) {
            optionModelView.holder.mchkChoose = (CheckBox) convertView.findViewById(R.id.id_checkbox_product_option_checkbox);
        }

        /**
         * Chuẩn bị holder cho option value, với kiểu chọn date time
         *
         * @param convertView
         */
        private void initTypeDateTimeHolder(View convertView, OptionValueModelView optionModelView) {
            optionModelView.holder.mdatePicker = (DatePicker) convertView.findViewById(R.id.id_datepicker_product_option_date);
            optionModelView.holder.mtimePicker = (TimePicker) convertView.findViewById(R.id.id_timepicker_product_option_time);
        }

        /**
         * Khi click tăng trên 1 option
         */
        private void onAddOptionQuantity(OptionModelView optionModelView, OptionValueModelViewHolder holder) {
            optionModelView.quantity = holder.mtxtQuantity.getValueFloat();

            // tính toán cập nhật lại giá
            updateCartItemPrice();
            mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPriceProduct(getItem().getPrice()));

            // cập nhật hiển thị option
            expandableListAdapter.notifyDataSetChanged();
//            mBinding.setCartItem(getItem());
        }

        /**
         * Khi click giảm trên 1 option
         */
        private void onSubtractOptionQuantity(OptionModelView optionModelView, OptionValueModelViewHolder holder) {
            optionModelView.quantity = holder.mtxtQuantity.getValueFloat();

            // tính toán cập nhật lại giá
            updateCartItemPrice();
            mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPriceProduct(getItem().getPrice()));

            // cập nhật hiển thị option
            expandableListAdapter.notifyDataSetChanged();

//            mBinding.setCartItem(getItem());
        }


        /**
         * Khi click trên view
         *
         * @param v
         */
        private void onClickView(View v) {
            // tìm tất cả các radio trong cùng product option
            OptionValueModelView optionValueModelView = (OptionValueModelView) v.getTag();
            // clear hết chọn nếu là loại chọn 1
            if (optionValueModelView.optionModelView.isTypeSelectOne()) {
                if (optionValueModelView.optionModelView.optionValueModelViewList == null) return;
                for (OptionValueModelView modelView : optionValueModelView.optionModelView.optionValueModelViewList) {
                    modelView.choose = false;
                }
            }

            // đảo lại giá trị được chọn
            optionValueModelView.choose = (!optionValueModelView.choose);
            expandableListAdapter.notifyDataSetChanged();

            // cập nhật lại giá
            updateCartItemPrice();
            mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPriceProduct(getItem().getPrice()));
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
        public View getChildView(final int listPosition, int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            // Tham chiếu option value
            final OptionModelView optionModelView = getGroup(listPosition);
            final OptionValueModelView optionValueModelView = getChild(listPosition, expandedListPosition);
            // nếu kiểu color, size
            GridViewHolder holder;
            if (optionModelView.isTypeColor() || optionModelView.isTypeSize()) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.card_product_option_item_color, null);
                holder = new GridViewHolder();
                holder.mGridView = (ExpandableHeightGridView) convertView.findViewById(R.id.grid_item);
                final CustomColorGridAdapter mCustomColorGridAdapter = new CustomColorGridAdapter(optionModelView.code, context, mModelViewList.get(listPosition).optionValueModelViewList, 0);
                holder.mGridView.setAdapter(mCustomColorGridAdapter);
                holder.mGridView.setExpanded(true);
                holder.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        OptionValueModelView modelView = mModelViewList.get(listPosition).optionValueModelViewList.get(i);
                        if (!modelView.disable) {
                            onClickViewChild(modelView, mCustomColorGridAdapter);
                        }
                    }
                });
                return convertView;
            } else {

                // nếu chưa thì phải khởi tạo holder và view
                if (optionValueModelView.holder == null) {
                    // Khởi tạo view và holder
                    LayoutInflater layoutInflater = (LayoutInflater) this.context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    optionValueModelView.holder = new OptionValueModelViewHolder();

                    // nếu là ô value giả để thêm số lượng
                    if (optionValueModelView instanceof FakeValueModelView) {
                        convertView = layoutInflater.inflate(R.layout.card_product_option_item_quantity, null);
                        initTypeQuantity(convertView, optionValueModelView);
                    } else {
                        // nếu là kiểu text field
                        // nếu là kiểu text field
                        if (optionModelView.isTypeField()) {
                            convertView = layoutInflater.inflate(R.layout.card_product_option_item_field, null);
                            initTypeField(convertView, optionValueModelView);
                        }
                        // nếu là kiểu radio
                        else if (optionModelView.isTypeSelectMultipe()) {
                            convertView = layoutInflater.inflate(R.layout.card_product_option_item_checkbox, null);
                            initTypeChooseMultipeHolder(convertView, optionValueModelView);
                            // nếu kiểu checkbox chọn nhiều
                        } else {
                            // còn lại quy về kiểu chọn 1
                            convertView = layoutInflater.inflate(R.layout.card_product_option_item_radio, null);
                            initTypeChooseOneHolder(convertView, optionValueModelView);
                        }

                    }

                    // gán convert view vào holder và modelview
                    optionValueModelView.holder.view = convertView;
                    convertView.setTag(optionValueModelView);

                    // tham chiếu holder sang các control
                    optionValueModelView.holder.mtxtDisplay = (TextView) optionValueModelView.holder.view
                            .findViewById(R.id.id_txt_product_option_display);
                    optionValueModelView.holder.mtxtPrice = (TextView) optionValueModelView.holder.view
                            .findViewById(R.id.id_txt_product_option_price);

                    // sự kiện click
                    if (!optionModelView.isTypeColor() && !optionModelView.isTypeSize()) {
                        if (!(optionValueModelView instanceof FakeValueModelView)) {
                            convertView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onClickView(v);
                                }
                            });
                        }
                    }


                }
                // bind giá trị vào
                optionValueModelView.holder.mtxtDisplay.setText(optionValueModelView.title);
                if (optionModelView.isConfigOption()) {
                    optionValueModelView.holder.mtxtPrice.setText(StringUtil.STRING_EMPTY);
                } else {
                    optionValueModelView.holder.mtxtPrice.setText(ConfigUtil.formatPrice(optionValueModelView.price));
                }
                if (optionValueModelView.holder.mtxtQuantity != null) {
                    optionValueModelView.holder.mtxtQuantity.setText(ConfigUtil.formatQuantity(optionModelView.quantity));
                }
                if (optionValueModelView.holder.mradChoose != null) {
                    optionValueModelView.holder.mradChoose.setChecked(optionValueModelView.choose);
                    optionValueModelView.holder.mradChoose.setSelected(optionValueModelView.choose);
                }
                if (optionValueModelView.holder.mchkChoose != null) {
                    optionValueModelView.holder.mchkChoose.setChecked(optionValueModelView.choose);
                    optionValueModelView.holder.mchkChoose.setSelected(optionValueModelView.choose);
                }
                if (optionValueModelView.holder.mtxtField != null) {
                    optionValueModelView.holder.mtxtField.setText(optionValueModelView.id);
                }

                return optionValueModelView.holder.view;
            }
        }

        /**
         * Đếm số nút option value
         *
         * @param listPosition
         * @return
         */
        @Override
        public int getChildrenCount(int listPosition) {
            if (mModelViewList == null) return 0;
            if (mModelViewList.get(listPosition).isTypeColor() || mModelViewList.get(listPosition).isTypeSize()) {
                return 1;
            }
            return mModelViewList.get(listPosition).optionValueModelViewList.size();
        }

        /**
         * Đếm số nút option custom
         *
         * @return
         */
        @Override
        public int getGroupCount() {
            if (mModelViewList == null) return 0;
            return mModelViewList.size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // chuẩn bị holder
            OptionModelView optionModelView = getGroup(listPosition);
            if (optionModelView.holder == null) {
                // khởi tạo view
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.card_product_option_group, null);
                optionModelView.holder = new OptionModelViewHolder();
                optionModelView.holder.view = convertView;

                // khởi tạo view holder
                optionModelView.holder.mtxtTitle = (TextView) optionModelView.holder.view.findViewById(R.id.listTitle);
                optionModelView.holder.mtxtError = (TextView) optionModelView.holder.view.findViewById(R.id.listError);
            }

            // gán giá trị lên giao diện
            if (optionModelView.is_required)
                optionModelView.holder.mtxtTitle.setText(optionModelView.title);
            else
                optionModelView.holder.mtxtTitle.setText(optionModelView.title + StringUtil.STRING_SPACE + getResources().getString(R.string.field_optional));

            if (ProductOptionCustom.OPTION_TYPE_GROUPED.equals(optionModelView.option_type))
                optionModelView.holder.mtxtTitle.setText(optionModelView.title + StringUtil.STRING_SPACE + getResources().getString(R.string.field_optional) + StringUtil.STRING_SPACE + ConfigUtil.formatPrice(optionModelView.price));

            // luôn mở expand
            ExpandableListView mExpandableListView = (ExpandableListView) parent;
            mExpandableListView.expandGroup(listPosition);

            // return
            return optionModelView.holder.view;
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

    public class GridViewHolder {
        ExpandableHeightGridView mGridView;
    }

    /**
     * Nắm ngữ view và product option custom  tương ứng
     */
    public class OptionModelViewHolder {
        public View view;
        //        public List<ProductOptionCustomValueHolder> mProductOptionCustomValueHolderList;
//        public OptionModelView optionModelView;
        public TextView mtxtTitle;
        public TextView mtxtError;
    }

    /**
     * Nắm ngữ view và product option custom value tương ứng
     */
    public class OptionValueModelViewHolder {
        //        ProductOptionCustom mProductOptionCustom;
        public View view;
        public TextView mtxtDisplay;
        //        public TextView mtxtDisplaySub;
        public TextView mtxtPrice;
        public EditTextQuantity mtxtQuantity;
        public RadioButton mradChoose;
        public CheckBox mchkChoose;
        public EditText mtxtField;
        public DatePicker mdatePicker;
        public TimePicker mtimePicker;
    }

    public class OptionColorViewHolder {
        public View view;
        public RelativeLayout mrlColor, rl_select_color, rl_select_size;
        public ImageView mImageColor, im_disable_color, im_disable_size;
        public TextView mTextSize;
        public RelativeLayout ll_color;
    }

    private class CustomColorGridAdapter extends BaseAdapter {
        Context context;
        List<OptionValueModelView> optionValueModelViewList;
        int type = 0;
        String code;
        int position;

        public int getPosition() {
            return position;
        }

        public List<OptionValueModelView> getOptionValueModelViewList() {
            return optionValueModelViewList;
        }

        public CustomColorGridAdapter(String code, Context context, List<OptionValueModelView> optionValueModelViewList, int type) {
            this.context = context;
            this.optionValueModelViewList = optionValueModelViewList;
            this.type = type;
            this.code = code;
        }

        @Override
        public int getCount() {
            return optionValueModelViewList.size();
        }

        @Override
        public Object getItem(int i) {
            return optionValueModelViewList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            final OptionValueModelView optionValueModelView = optionValueModelViewList.get(i);
            position = i;
            // nếu chưa thì phải khởi tạo holder và view
            OptionColorViewHolder mOptionColorViewHolder;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mOptionColorViewHolder = new OptionColorViewHolder();
                if (optionValueModelView.optionModelView.isTypeColor()) {
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_child_color, null);
                    mOptionColorViewHolder.mImageColor = (ImageView) convertView.findViewById(R.id.im_color);
                    mOptionColorViewHolder.rl_select_color = (RelativeLayout) convertView.findViewById(R.id.rl_select_color);
                    mOptionColorViewHolder.ll_color = (RelativeLayout) convertView.findViewById(R.id.ll_color);
                    // gán convert view vào holder và modelview
                    mOptionColorViewHolder.mrlColor = (RelativeLayout) convertView.findViewById(R.id.rl_color_content);
                    mOptionColorViewHolder.im_disable_color = (ImageView) convertView.findViewById(R.id.im_disable_color);
                } else {
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_child_size, null);
                    mOptionColorViewHolder.mTextSize = (TextView) convertView.findViewById(R.id.tv_size);
                    mOptionColorViewHolder.rl_select_size = (RelativeLayout) convertView.findViewById(R.id.rl_select_size);
                    mOptionColorViewHolder.im_disable_size = (ImageView) convertView.findViewById(R.id.im_disable_size);
                }

                convertView.setTag(mOptionColorViewHolder);

            } else {
                mOptionColorViewHolder = (OptionColorViewHolder) convertView.getTag();
            }

            if (optionValueModelView.optionModelView.isTypeColor()) {
                mOptionColorViewHolder.mImageColor.getDrawable().setColorFilter(Color.parseColor(ConfigUtil.getValueColorSwatch(code, optionValueModelView.id)), PorterDuff.Mode.MULTIPLY);
                mOptionColorViewHolder.rl_select_color.setVisibility(optionValueModelView.choose ? VISIBLE : GONE);
            }

            if (optionValueModelView.optionModelView.isTypeSize()) {
                mOptionColorViewHolder.mTextSize.setText(ConfigUtil.getValueColorSwatch(code, optionValueModelView.id));
                mOptionColorViewHolder.rl_select_size.setBackground(optionValueModelView.choose ? getResources().getDrawable(R.drawable.border_select_size) : null);
            }

            if (optionValueModelView.optionModelView.isTypeColor()) {
                mOptionColorViewHolder.im_disable_color.setVisibility(optionValueModelView.disable ? VISIBLE : GONE);
            }

            if (optionValueModelView.optionModelView.isTypeSize()) {
                mOptionColorViewHolder.im_disable_size.setVisibility(optionValueModelView.disable ? VISIBLE : GONE);
            }

            return convertView;
        }
    }

    private void onClickViewChild(OptionValueModelView optionValueModelView, CustomColorGridAdapter adapter) {
        // tìm tất cả các radio trong cùng product option
        // clear hết chọn nếu là loại chọn 1
        if (optionValueModelView.optionModelView.isTypeSize() || optionValueModelView.optionModelView.isTypeColor()) {
            if (optionValueModelView.optionModelView.optionValueModelViewList == null) return;
            for (OptionValueModelView modelView : optionValueModelView.optionModelView.optionValueModelViewList) {
                modelView.choose = false;
            }
        }

        // đảo lại giá trị được chọn
        optionValueModelView.choose = (!optionValueModelView.choose);
        checkDisableColor(optionValueModelView);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        expandableListAdapter.notifyDataSetChanged();

        // cập nhật lại giá
        updateCartItemPrice();
        mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPriceProduct(getItem().getPrice()));
    }

    private void checkDisableColor(OptionValueModelView optionValueModelViewChoose) {
        if (getItem().getProduct().getProductOption().getJsonConfig() == null) return;
        List<String> listChoose = new ArrayList<>();
        List<String> listChooseCompare = new ArrayList<>();
        if (optionValueModelViewChoose.productList != null && optionValueModelViewChoose.productList.size() > 0) {
            listChoose.addAll(optionValueModelViewChoose.productList);
            for (OptionModelView optionModelView : mModelViewList) {
                if (optionModelView.isConfigOption()) {
                    if (optionValueModelViewChoose.optionModelView.isTypeColor()) {
                        if (optionModelView.isTypeSize()) {
                            for (OptionValueModelView childSize : optionModelView.optionValueModelViewList) {
                                if (childSize.productList != null && childSize.productList.size() > 0) {
                                    listChooseCompare.clear();
                                    listChooseCompare.addAll(listChoose);
                                    listChooseCompare.retainAll(childSize.productList);
                                    if (listChooseCompare.size() > 0) {
                                        childSize.disable = false;
                                    } else {
                                        childSize.disable = true;
                                    }
                                } else {
                                    childSize.disable = true;
                                }
                            }
                        }
                    } else if (optionValueModelViewChoose.optionModelView.isTypeSize()) {
                        if (optionModelView.isTypeColor()) {
                            for (OptionValueModelView childColor : optionModelView.optionValueModelViewList) {
                                if (childColor.productList != null && childColor.productList.size() > 0) {
                                    listChooseCompare.clear();
                                    listChooseCompare.addAll(listChoose);
                                    listChooseCompare.retainAll(childColor.productList);
                                    if (listChooseCompare.size() > 0) {
                                        childColor.disable = false;
                                    } else {
                                        childColor.disable = true;
                                    }
                                } else {
                                    childColor.disable = true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (OptionModelView optionModelView : mModelViewList) {
                if (optionModelView.isConfigOption()) {
                    if (optionValueModelViewChoose.optionModelView.isTypeColor()) {
                        if (optionModelView.isTypeSize()) {
                            for (OptionValueModelView childSize : optionModelView.optionValueModelViewList) {
                                childSize.disable = true;
                            }
                        }
                    } else if (optionValueModelViewChoose.optionModelView.isTypeSize()) {
                        if (optionModelView.isTypeColor()) {
                            for (OptionValueModelView childColor : optionModelView.optionValueModelViewList) {
                                childColor.disable = true;
                            }
                        }
                    }
                }
            }
        }
    }
}
