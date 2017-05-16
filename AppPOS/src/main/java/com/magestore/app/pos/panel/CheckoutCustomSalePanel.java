package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.databinding.PanelCheckoutCustomSaleBinding;
import com.magestore.app.pos.model.config.PosConfigTaxClass;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextFloat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 3/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutCustomSalePanel extends AbstractDetailPanel<CartItem> {
    PanelCheckoutCustomSaleBinding mBinding;
    EditTextFloat mtxtPrice;
    EditText mtxtName;
    SimpleSpinner s_tax_class;
    private static String TAX_CLASS_TYPE = "PRODUCT";
    /**
     * Khởi tạo
     *
     * @param context
     */
    public CheckoutCustomSalePanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public CheckoutCustomSalePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CheckoutCustomSalePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Chuẩn bị layout
     */
    @Override
    protected void initLayout() {
        setLayoutPanel(R.layout.panel_checkout_custom_sale);
//        View view = inflate(getContext(), R.layout.panel_checkout_custom_sale, null);
//        addView(view);
        s_tax_class = (SimpleSpinner) getView().findViewById(R.id.s_tax_class);
        mtxtPrice = (EditTextFloat) getView().findViewById(R.id.id_txt_custom_sale_price);
        mtxtName = (EditText) getView().findViewById(R.id.id_txt_custom_sale_name);

        mBinding = DataBindingUtil.bind(getView());
        mBinding.setPanel(this);
    }

    /**
     * Chuyển từ dataset sang giao diện
     *
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);
        setDataTaxClass();
        mBinding.setCartItem(item);
        mBinding.setProduct(item.getProduct());
        if (StringUtil.isNullOrEmpty(mBinding.idTxtCustomSaleName.getText().toString().trim()))
            mBinding.idTxtCustomSaleName.setText(R.string.custom_sale);
    }

    public void setDataTaxClass() {
        List<ConfigTaxClass> listTaxClass = new ArrayList<>();
        // add none deafult
        ConfigTaxClass configTaxClass = new PosConfigTaxClass();
        configTaxClass.setID("0");
        configTaxClass.setClassName(getContext().getString(R.string.none));
        configTaxClass.setClassType(TAX_CLASS_TYPE);
        listTaxClass.add(configTaxClass);
        for (ConfigTaxClass taxClass : ConfigUtil.getConfigTaxClass()) {
            if (taxClass.getClassType().equals(TAX_CLASS_TYPE)) {
                listTaxClass.add(taxClass);
            }
        }
        s_tax_class.bind(listTaxClass.toArray(new ConfigTaxClass[0]));
    }

    /**
     * Chuyển từ giao diện sang data set
     *
     * @param item
     */
    @Override
    public void bind2Item(CartItem item) {
        item.setQuantity(1);
        item.setTypeCustom();
        item.setPrice(ConfigUtil.convertToBasePrice(mtxtPrice.getValueFloat()));
        item.setUnitPrice(ConfigUtil.convertToBasePrice(mtxtPrice.getValueFloat()));
        item.setCustomPrice(ConfigUtil.convertToBasePrice(mtxtPrice.getValueFloat()));
        item.setOriginalPrice(ConfigUtil.convertToBasePrice(mtxtPrice.getValueFloat()));
        item.setDefaultCustomPrice(ConfigUtil.convertToBasePrice(mtxtPrice.getValueFloat()));
        item.setShipable(mBinding.idSwitchCustomShipable.isChecked());
        item.setTaxClassId(s_tax_class.getSelection());
        String strName = mtxtName.getText().toString().trim();
        strName = (StringUtil.isNullOrEmpty(strName)) ? getResources().getString(R.string.sales_custom_sales_product) : strName;
        item.getProduct().setName(strName);

        super.bind2Item(item);
    }

    /**
     * Kiểm tra nhập vào
     *
     * @return
     */
    public boolean validateInput() {
//        if (mtxtName.getText().toString().trim().equals(StringUtil.STRING_EMPTY)) {
//            return false;
//        }
        return true;
    }

    /**
     * Nhất nút save
     *
     * @param v
     */
    public void onSaveClick(View v) {
        if (!validateInput()) {
            return;
        }
        ((CartItemListController) getController()).updateCustomeSaleToCart(bind2Item());
        ((CartItemListController) getController()).showSalesMenuToggle(false);
    }
}
