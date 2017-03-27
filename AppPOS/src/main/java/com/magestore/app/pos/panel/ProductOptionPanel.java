package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.lib.view.item.GenericModelView;
import com.magestore.app.lib.view.item.ModelView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.PanelProductOptionListBinding;
import com.magestore.app.pos.model.catalog.PosProductOption;
import com.magestore.app.pos.model.catalog.PosProductOptionBundle;
import com.magestore.app.pos.model.catalog.PosProductOptionBundleItem;
import com.magestore.app.pos.model.catalog.PosProductOptionConfigOption;
import com.magestore.app.pos.model.catalog.PosProductOptionCustom;
import com.magestore.app.pos.model.catalog.PosProductOptionCustomValue;
import com.magestore.app.pos.model.catalog.PosProductOptionGrouped;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextInteger;

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
    EditTextInteger mtxtCartItemQuantity;
    /**
     * Khởi tạo
     * @param context
     */
    public ProductOptionPanel(Context context) {
        super(context);
    }

    CheckoutListController mCheckoutListController;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     */
    public ProductOptionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
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

        // đặt giá trị min
        mtxtCartItemQuantity.setMinValue(item.getProduct().getQuantityIncrement());

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
        if (item.getProduct().getBitmap() != null)
            mImageProductDetail.setImageBitmap(item.getProduct().getBitmap());
    }

    /**
     * Save item vào cart
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
                for (OptionModelView optionModel : mModelViewList) {
                    CartItem item = bind2Item();
                    ((CartItemListController) getController()).updateToCart(item, optionModel.getModel().getID(), optionModel.title, optionModel.quantity * item.getQuantity(), optionModel.price);
                }
                ((CartItemListController) getController()).closeAllOpeningDialog();
            }
        }
        else // và không option thì add như 1 product
            ((CartItemListController) getController()).updateToCartNoOption(bind2Item());

        mCheckoutListController.showButtonDiscount(true);
    }

    /**
     * Nhấn nút trừ số lượng
     * @param view
     */
    public void onAddQuantity(View view) {
        mtxtCartItemQuantity.add(getItem().getProduct().getQuantityIncrement());
    }

    /**
     * Nhấn nút thêm số lượng
     * @param view
     */
    public void onSubstractQuantity(View view) {
        mtxtCartItemQuantity.substract(getItem().getProduct().getQuantityIncrement());
    }

    /**
     * So sánh cặp code value với cart item xem đã được chọn chưa
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
     * Tìm trong option đã chọn là cái option nào
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
            optionModelView.is_required = true;
            optionModelView.option_type = ProductOptionCustom.OPTION_TYPE_CUSTOM;
            optionModelView.input_type = optionCustom.getType();
            optionModelView.quantity = Integer.parseInt(getOptionValue(optionCustom.getID(), getItem().getBundleOptionQuantity(), StringUtil.STRING_ONE));
            optionModelView.setModel(optionCustom);

            // tạo model view tương ứng mỗi option value
            if (optionCustom.getOptionValueList() == null) continue;
            for (ProductOptionCustomValue optionValue : optionCustom.getOptionValueList()) {
                OptionValueModelView optionValueModelView = new OptionValueModelView();
                optionValueModelView.optionModelView = optionModelView;
                optionValueModelView.id = optionValue.getID();
                optionValueModelView.choose = isChooseValue(optionModelView.getModel().getID(), optionValueModelView.id, getItem().getOptions());
                optionValueModelView.title = optionValue.getTitle();
                optionValueModelView.price =
                        optionValueModelView.isPriceTypePercent()
                                ? "" + (Float.parseFloat(optionValue.getPrice()) * getItem().getProduct().getFinalPrice() / 100)
                                : optionValue.getPrice();

                optionValueModelView.price_type = optionValue.getPriceType();
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
                optionModelView.title = configOption.optionLabel;
                optionModelView.quantity = 1;
                optionModelView.is_required = true;
                optionModelView.option_type = ProductOptionCustom.OPTION_TYPE_CONFIG;
                optionModelView.input_type = ProductOptionCustom.TYPE_RADIO;
                optionModelView.quantity = Integer.parseInt(getOptionValue(configOption.getID(), getItem().getBundleOptionQuantity(), StringUtil.STRING_ONE));
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
                optionModelView.quantity = Integer.parseInt(getOptionValue(bundle.getID(), getItem().getBundleOptionQuantity(), StringUtil.STRING_ONE));
                optionModelView.setModel(bundle);

                if (bundle.getItems() == null) continue;
                for (PosProductOptionBundleItem bundleItem : bundle.getItems()) {
                    OptionValueModelView optionValueModelView = new OptionValueModelView();
                    optionValueModelView.optionModelView = optionModelView;
                    optionValueModelView.id = bundleItem.getEntityId();
                    optionValueModelView.title = bundleItem.getName();
                    optionValueModelView.price = bundleItem.getPrice();
                    optionValueModelView.choose = isChooseValue(optionModelView.getModel().getID(), optionValueModelView.id, getItem().getBundleOption());
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
// convert option theo bundle options sang 1 format thống nhất
        List<PosProductOptionGrouped> groupedList = getItem().getProduct().getProductOption().getGroupedOptions();
        if (groupedList != null) {
            for (PosProductOptionGrouped groupedOption : groupedList) {
                OptionModelView optionModelView = new OptionModelView();
                optionModelView.optionValueModelViewList = new ArrayList<>();
                optionModelView.title = groupedOption.getName();
                optionModelView.quantity = groupedOption.getDefaultQty();
                optionModelView.is_required = false;
                optionModelView.option_type = ProductOptionCustom.OPTION_TYPE_GROUPED;
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
//
//                if (bundle.getItems() == null) continue;
//                for (PosProductOptionBundleItem bundleItem : bundle.getItems()) {
//                    OptionValueModelView optionValueModelView = new OptionValueModelView();
//                    optionValueModelView.optionModelView = optionModelView;
//                    optionValueModelView.id = bundleItem.getEntityId();
//                    optionValueModelView.title = bundleItem.getName();
//                    optionValueModelView.price = bundleItem.getPrice();
//                    optionValueModelView.choose = isChooseValue(optionModelView.getModel().getID(), optionValueModelView.id, getItem().getBundleOption());
//                    optionValueModelView.setModel(bundleItem);
//                    optionModelView.optionValueModelViewList.add(optionValueModelView);
//                }
//
//                // thêm một list giả cuối để đặt số lượng
//                FakeValueModelView optionValueModelView = new FakeValueModelView();
//                optionValueModelView.optionModelView = optionModelView;
//                optionValueModelView.choose = false;
//                optionModelView.optionValueModelViewList.add(optionValueModelView);
//
//                // thêm vào danh sách option
//                mModelViewList.add(optionModelView);
            }
        }
    }

    /**
     * Tạo model view
     */
    public void createModelViewList() {
        mModelViewList = new ArrayList<OptionModelView>();

        // tạo model view list với custom option
        createModelViewListFromOptionCustom();

        // tạo model view từ config option
        createModelViewListFromOptionConfig();

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
        public int quantity;
        public float price;

        public boolean isCustomOption() {
            return (option_type == null) || ProductOptionCustom.OPTION_TYPE_CUSTOM.equals(option_type);
        }

        public boolean isConfigOption() {
            return ProductOptionCustom.OPTION_TYPE_CONFIG.equals(option_type);
        }

        public boolean isBundleOption() {
            return ProductOptionCustom.OPTION_TYPE_BUNDLE.equals(option_type);
        }

        public boolean isTypeSelectOne() {
            return ProductOptionCustom.TYPE_RADIO.equals(input_type) || ProductOptionCustom.TYPE_DROP_DOWN.equals(input_type);
        }

        public boolean isTypeSelectMultipe() {
            return ProductOptionCustom.TYPE_CHECKBOX.equals(input_type) || ProductOptionCustom.TYPE_MULTIPE.equals(input_type);
        }

        public boolean isTypeTime() {
            return ProductOptionCustom.TYPE_TIME.equals(input_type);
        }

        public boolean isTypeDate() {
            return ProductOptionCustom.TYPE_DATE.equals(input_type);
        }

        public boolean isTypeDateTime() {
            return ProductOptionCustom.TYPE_DATETIME.equals(input_type);
        }
    }

    /**
     * Model view giả cho phần nhập số lượng
     */
    class FakeValueModelView extends OptionValueModelView {
    }

    /**
     * View option tổng
     */
    class OptionValueModelView extends GenericModelView {
        public OptionValueModelViewHolder holder;
        public String title;
        public String price;
        public String price_type;
        public String id;
        public OptionModelView optionModelView;
        public boolean choose;

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

        // imageview
        mImageProductDetail = (ImageView) findViewById(R.id.id_img_product_detail_image);

        // button add to cart
        mbtnAddToCart = (Button) findViewById(R.id.id_btn_product_option_add_to_cart);

        // expan list view
        expandableListView = (ExpandableListView) findViewById(R.id.id_product_option_list);
        expandableListAdapter = new ProductOptionPanel.CustomExpandableListAdapter(getContext());
        expandableListView.setAdapter(expandableListAdapter);

        // text box số lượng
        mtxtCartItemQuantity = (EditTextInteger) findViewById(R.id.id_txt_product_option_cart_item_quantity);

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
        // duyệt tất cả các option custome để lấy option mà user đã chọn
        if (mModelViewList == null) return false;
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
                blnValid = false;
                optionModelView.holder.mtxtError.setText(getContext().getString(R.string.err_field_required));
                optionModelView.holder.mtxtError.setError(getContext().getString(R.string.err_field_required));
            } else {
                optionModelView.holder.mtxtError.setText(null);
                optionModelView.holder.mtxtError.setError(null);
            }
        }

        return blnValid;
    }

    @Override
    public void bind2Item(CartItem item) {
        if (expandableListAdapter == null) return;

        // đặt số lượng
        item.setQuantity(mtxtCartItemQuantity.getValueInteger());

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
                if (optionValueModelView instanceof FakeValueModelView) continue;
                if (optionValueModelView.choose) {
                    // điền description cho cart item
                    if (firstOptionValue) {
                        descriptionBuilder.append(firstOption ? StringUtil.STRING_EMPTY : StringUtil.STRING_COMMA_SPACE).append(optionModelView.title);
                        firstOption = false;
                    }
                    descriptionBuilder.append(firstOptionValue ? StringUtil.STRING_COLON_SPACE : StringUtil.STRING_COMMA_SPACE).append(optionValueModelView.title);
                    firstOptionValue = false;

                    // chèn các option do user chọn vào cart item
                    if (PosProductOptionCustom.OPTION_TYPE_CUSTOM.equals(optionModelView.option_type))
                        item.insertOption(optionModelView.getModel().getID(), optionValueModelView.getModel().getID());
                    else if (PosProductOptionCustom.OPTION_TYPE_CONFIG.equals(optionModelView.option_type))
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

        // duyệt tất cả các option custome để tính lại đơn giá
        for (OptionModelView optionModelView : mModelViewList) {
            if (optionModelView.optionValueModelViewList == null) continue;
            // khởi tạo danh sách option value
            for (OptionValueModelView optionValueModelView : optionModelView.optionValueModelViewList) {
                // nếu là loại chọn nhiều
                if (optionValueModelView.choose) {
                    price += Float.parseFloat(optionValueModelView.price) * optionModelView.quantity;
                }
            }
        }

        // cập nhật lại đơn giá
        getItem().setUnitPrice(price);
        getItem().setCustomPrice(price);
    }

    /**
     * Hiển thị progress
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
         * Chuẩn bị holder cho option value, với kiểu chọn nhiều
         *
         * @param convertView
         */
        private void initTypeChooseMultipeHolder(View convertView, OptionValueModelView optionModelView) {
            optionModelView.holder.mchkChoose = (CheckBox) convertView.findViewById(R.id.id_checkbox_product_option_checkbox);
        }

        /**
         * Chuẩn bị holder cho option value, với kiểu chọn date time
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
            optionModelView.quantity = holder.mtxtQuantity.getValueInteger();

            // tính toán cập nhật lại giá
            updateCartItemPrice();
            mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPrice(getItem().getPrice()));

            // cập nhật hiển thị option
            expandableListAdapter.notifyDataSetChanged();
//            mBinding.setCartItem(getItem());
        }

        /**
         * Khi click giảm trên 1 option
         */
        private void onSubtractOptionQuantity(OptionModelView optionModelView, OptionValueModelViewHolder holder) {
            optionModelView.quantity = holder.mtxtQuantity.getValueInteger();

            // tính toán cập nhật lại giá
            updateCartItemPrice();
            mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPrice(getItem().getPrice()));

            // cập nhật hiển thị option
            expandableListAdapter.notifyDataSetChanged();

//            mBinding.setCartItem(getItem());
        }


        /**
         * Khi click trên view
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
            mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPrice(getItem().getPrice()));
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
        public View getChildView(int listPosition, int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            // Tham chiếu option value
            final OptionModelView optionModelView = getGroup(listPosition);
            final OptionValueModelView optionValueModelView = getChild(listPosition, expandedListPosition);

            // nếu chưa thì phải khởi tạo holder và view
            if (optionValueModelView.holder == null) {
                // Khởi tạo view và holder
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                optionValueModelView.holder = new OptionValueModelViewHolder();

                // nếu là ô value giả để thêm số lượng
                if (optionValueModelView instanceof FakeValueModelView) {
                    convertView = layoutInflater.inflate(R.layout.card_product_option_item_quantity, null);
                    optionValueModelView.holder.mtxtQuantity = (EditTextInteger) convertView
                            .findViewById(R.id.id_txt_product_option_quantity);
                    optionValueModelView.holder.mtxtQuantity.setMinValue(1);

                    // khi thôi focus
                    optionValueModelView.holder.mtxtQuantity.setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                // lấy giá trị trong ô text, căn lại giữa max và min
                                optionModelView.quantity = ConfigUtil.parseInteger(ConfigUtil.truncateIntegerDigit(optionValueModelView.holder.mtxtQuantity.getText().toString()));
                                if (optionModelView.quantity < 1) optionModelView.quantity = 1;
                                updateCartItemPrice();
                                mBinding.idTxtProductOptionCartItemPrice.setText(ConfigUtil.formatPrice(getItem().getPrice()));
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
                            optionValueModelView.holder.mtxtQuantity.substract(1);
                            onSubtractOptionQuantity((OptionModelView) v.getTag(), optionValueModelView.holder);
                        }
                    });

                    // khi ấn tăng số lượng
                    View addView = (convertView.findViewById(R.id.id_txt_product_option_quantity_add));
                    addView.setTag(optionModelView);
                    addView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            optionValueModelView.holder.mtxtQuantity.add(1);
                            onAddOptionQuantity((OptionModelView) v.getTag(), optionValueModelView.holder);
                        }
                    });
                } else {
                    // nếu là kiểu radio
                    if (optionModelView.isTypeSelectMultipe()) {
                        convertView = layoutInflater.inflate(R.layout.card_product_option_item_checkbox, null);
                        initTypeChooseMultipeHolder(convertView, optionValueModelView);
                    }
                    // nếu kiểu checkbox chọn nhiều
                    else {
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
                if (!(optionValueModelView instanceof FakeValueModelView)) {
                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickView(v);
                        }
                    });
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
                optionValueModelView.holder.mtxtQuantity.setText("" + optionModelView.quantity);
            }
            if (optionValueModelView.holder.mradChoose != null) {
                optionValueModelView.holder.mradChoose.setChecked(optionValueModelView.choose);
                optionValueModelView.holder.mradChoose.setSelected(optionValueModelView.choose);
            }
            if (optionValueModelView.holder.mchkChoose != null) {
                optionValueModelView.holder.mchkChoose.setChecked(optionValueModelView.choose);
                optionValueModelView.holder.mchkChoose.setSelected(optionValueModelView.choose);
            }

            return optionValueModelView.holder.view;
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
        public EditTextInteger mtxtQuantity;
        public RadioButton mradChoose;
        public CheckBox mchkChoose;
        public DatePicker mdatePicker;
        public TimePicker mtimePicker;
//        public PosProductOptionCustomValue customValue;
    }
}
