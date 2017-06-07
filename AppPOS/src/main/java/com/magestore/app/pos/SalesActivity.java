package com.magestore.app.pos;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.SubjectObserv;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.catalog.CategoryService;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.lib.service.plugins.PluginsService;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.controller.CategoryListController;
import com.magestore.app.pos.controller.CheckoutAddPaymentListController;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.PluginGiftCardController;
import com.magestore.app.pos.controller.ProductListController;
import com.magestore.app.pos.controller.SettingListController;
import com.magestore.app.pos.panel.CartItemDetailPanel;
import com.magestore.app.pos.panel.CartItemListPanel;
import com.magestore.app.pos.panel.CartOrderListPanel;
import com.magestore.app.pos.panel.CategoryListPanel;
import com.magestore.app.pos.panel.CheckoutAddPaymentPanel;
import com.magestore.app.pos.panel.CheckoutAddressListPanel;
import com.magestore.app.pos.panel.CheckoutCustomSalePanel;
import com.magestore.app.pos.panel.CheckoutDetailPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentCreditCardPanel;
import com.magestore.app.pos.panel.CheckoutPaymentListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentWebviewPanel;
import com.magestore.app.pos.panel.CheckoutShippingListPanel;
import com.magestore.app.pos.panel.CheckoutSuccessPanel;
import com.magestore.app.pos.panel.PaymentMethodListPanel;
import com.magestore.app.pos.panel.PluginGiftCardListPanel;
import com.magestore.app.pos.panel.PluginGiftCardPanel;
import com.magestore.app.pos.panel.PluginRewardPointPanel;
import com.magestore.app.pos.panel.PluginStoreCreditPanel;
import com.magestore.app.pos.panel.ProductListPanel;
import com.magestore.app.pos.panel.ProductOptionPanel;
import com.magestore.app.pos.panel.SpinnerListPanel;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.view.ui.PosUI;

/**
 * Quản lý các hoạt động salé
 * Tạo sales mới, hold và check out thanh toán
 */
public class SalesActivity extends AbstractActivity
        implements
        PosUI {
    MagestoreContext magestoreContext;
    ServiceFactory factory;
    public static int REQUEST_PERMISSON_CAMERA = 12;
    SpinnerListPanel mSpinnerListPanel;

    // 2 pane
    private boolean mTwoPane;

    // Panel chứa danh sách mặt hàng và đơn hàng
    private ProductListPanel mProductListPanel;
    private CheckoutListPanel mCheckoutListPanel;
    private CheckoutDetailPanel mCheckoutDetailPanel;
    private CheckoutPaymentWebviewPanel mCheckoutPaymentWebviewPanel;
    private CheckoutShippingListPanel mCheckoutShippingListPanel;
    private CheckoutPaymentListPanel mCheckoutPaymentListPanel;

    // cart item panel
    private CartItemListPanel mCartItemListPanel;
    private CartItemDetailPanel mCartItemDetailPanel;
    private ProductOptionPanel mProductOptionPanel;
    private CheckoutCustomSalePanel mCheckoutCustomSalePanel;

    private CategoryListPanel mCategoryListPanel;
    private PaymentMethodListPanel mPaymentMethodListPanel;
    private CheckoutAddPaymentPanel mCheckoutAddPaymentPanel;
    private CartOrderListPanel mCartOrderListPanel;
    private CheckoutAddressListPanel mCheckoutAddressListPanel;
    private CheckoutSuccessPanel mCheckoutSuccessPanel;
    private CheckoutPaymentCreditCardPanel mCheckoutPaymentCreditCardPanel;

    // controller cho danh sách mặt hàng và đơn hàng
    private ProductListController mProductListController;
    private CheckoutListController mCheckoutListController;
    private CartItemListController mCheckoutCartItemListController;
    private CategoryListController mCategoryListController;
    private CheckoutAddPaymentListController mCheckoutAddPaymentListController;
//    private ProductOptionController mProductOptionController;

    // plugin giftcard
    PluginGiftCardPanel mPluginGiftCardPanel;
    PluginGiftCardListPanel mPluginGiftCardListPanel;
    PluginGiftCardController mPluginGiftCardController;
    // plugin reward point
    PluginRewardPointPanel mPluginRewardPointPanel;
    // plugin store credit
    PluginStoreCreditPanel mPluginStoreCreditPanel;

    public static Order mOrder;

    // Toolbar Order
    Toolbar toolbar_order;
    RelativeLayout rl_customer;
    ImageButton rl_change_customer;
    CustomerService customerService = null;
    ConfigService configService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean redirect_register_shift = getIntent().getBooleanExtra("redirect_register_shift", false);
        if (redirect_register_shift) {
            super.navigationView(4);
        }

        setContentView(R.layout.sales_menu);
        // chuẩn bị control layout
        initLayout();

        // chuẩn bị các model
        initModel();

        // chuản bị các value trong layout
        initValue();

        super.setheader();
    }

    @Override
    protected void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category);
        toolbar_order = (Toolbar) findViewById(R.id.toolbar_order);
        rl_customer = (RelativeLayout) toolbar_order.findViewById(R.id.rl_customer);
        rl_change_customer = (ImageButton) toolbar_order.findViewById(R.id.rl_change_customer);
        initToolbarMenu(toolbar_order);

        // Nút tab để tạo đơn hàng mới
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("MagestoreStatementAction", null).show();
            }
        });

        // Xác định là 2 pane hay 1 pane
        if (findViewById(R.id.toolbar_category) != null) {
            mTwoPane = true;
        }

        // product list
        mProductListPanel = (ProductListPanel) findViewById(R.id.product_list_panel);

        // check out list & detail
        mCheckoutListPanel = (CheckoutListPanel) findViewById(R.id.checkout_list_panel);
        mCheckoutDetailPanel = (CheckoutDetailPanel) findViewById(R.id.checkout_detail_panel);

        // order success panel
        mCheckoutSuccessPanel = (CheckoutSuccessPanel) findViewById(R.id.checkout_success_panel);
        // checkout payment webview
        mCheckoutPaymentWebviewPanel = (CheckoutPaymentWebviewPanel) findViewById(R.id.checkout_webview_panel);

        // cart item panel
        mCartItemListPanel = (CartItemListPanel) mCheckoutListPanel.findViewById(R.id.order_item_panel);
        mCartItemDetailPanel = new CartItemDetailPanel(this.getContext());
        mProductOptionPanel = new ProductOptionPanel(getContext());
        mCheckoutCustomSalePanel = new CheckoutCustomSalePanel(getContext());

        mCartOrderListPanel = (CartOrderListPanel) mCheckoutListPanel.findViewById(R.id.checkout_item_panel);

        // shipping list panel
        mCheckoutShippingListPanel = (CheckoutShippingListPanel) mCheckoutDetailPanel.findViewById(R.id.checkout_shipping_list_panel);

        // payment list panel
        mCheckoutPaymentListPanel = (CheckoutPaymentListPanel) mCheckoutDetailPanel.findViewById(R.id.checkout_payment_list_panel);
        // payment thod panel
        mPaymentMethodListPanel = (PaymentMethodListPanel) mCheckoutDetailPanel.findViewById(R.id.payment_method_list_panel);
        // payment creditcard
        mCheckoutPaymentCreditCardPanel = (CheckoutPaymentCreditCardPanel) mCheckoutDetailPanel.findViewById(R.id.checkout_payment_credit_card);

        // shipping address panel
        mCheckoutAddressListPanel = (CheckoutAddressListPanel) mCheckoutDetailPanel.findViewById(R.id.address_list_panel);

        // add payment panel
        mCheckoutAddPaymentPanel = new CheckoutAddPaymentPanel(getContext());

        // category list panel
        mCategoryListPanel = (CategoryListPanel) mProductListPanel.findViewById(R.id.category);
        mSpinnerListPanel = (SpinnerListPanel) findViewById(R.id.panel_category);

        // plugins giftcard
        mPluginGiftCardPanel = (PluginGiftCardPanel) mCheckoutDetailPanel.findViewById(R.id.rl_gift_card);
        mPluginGiftCardListPanel = (PluginGiftCardListPanel) mPluginGiftCardPanel.findViewById(R.id.gift_card_list_panel);

        // plugin reward point
        mPluginRewardPointPanel = (PluginRewardPointPanel) mCheckoutDetailPanel.findViewById(R.id.rl_reward_point);
        // plugin store credit
        mPluginStoreCreditPanel = (PluginStoreCreditPanel) mCheckoutDetailPanel.findViewById(R.id.rl_store_credit);
        // config print
        ConfigUtil.setTypePrint(getString(R.string.print_type_receipt));
    }

    protected void initModel() {
        // Context sử dụng xuyên suốt hệ thống
        magestoreContext = new MagestoreContext();
        magestoreContext.setActivity(this);

        // subjectObserv
        SubjectObserv subjectObserv = new SubjectObserv();

        // chuẩn bị service
        ProductService productService = null;
        CheckoutService checkoutService = null;
        CartService cartService = null;
        CategoryService categoryService = null;
        ProductOptionService productOptionService = null;
        CustomerAddressService customerAddressService = null;
        PluginsService pluginsService = null;
        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            productService = factory.generateProductService();
            checkoutService = factory.generateCheckoutService();
            cartService = factory.generateCartService();
            categoryService = factory.generateCategoryService();
            configService = factory.generateConfigService();
            customerService = factory.generateCustomerService();
            productOptionService = factory.generateProductOptionService();
            customerAddressService = factory.generateCustomerAddressService();
            pluginsService = factory.generatePluginsService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // controller quản lý category
        mCategoryListController = new CategoryListController();
        mCategoryListController.setMagestoreContext(magestoreContext);
        //Felix Edit 27/4
//        mCategoryListController.setListPanel(mCategoryListPanel);
        mCategoryListController.setListPanel(mSpinnerListPanel);
        //End Felix Edit 27/4

        mCategoryListController.setCategoryService(categoryService);
        mCategoryListController.setSubject(subjectObserv);

        // controller quản lý check out
        mCheckoutListController = new CheckoutListController();
        mCheckoutListController.setSubject(subjectObserv);
        mCheckoutListController.setContext(getContext());
        mCheckoutListController.setMagestoreContext(magestoreContext);
        mCheckoutListController.setListService(checkoutService);
        mCheckoutListController.setConfigService(configService);
        mCheckoutListController.setCustomerAddressService(customerAddressService);
        mCheckoutListController.setListPanel(mCheckoutListPanel);
        mCheckoutListPanel.setController(mCheckoutListController);
        mCheckoutListController.setDetailPanel(mCheckoutDetailPanel);
        mCheckoutListController.setCheckoutShippingListPanel(mCheckoutShippingListPanel);
        mCheckoutListController.setCheckoutPaymentListPanel(mCheckoutPaymentListPanel);
        mCheckoutListController.setPaymentMethodListPanel(mPaymentMethodListPanel);
        mCheckoutListController.setCheckoutAddPaymentPanel(mCheckoutAddPaymentPanel);
        mCheckoutListController.setCartOrderListPanel(mCartOrderListPanel);
        mCheckoutListController.setCheckoutAddressListPanel(mCheckoutAddressListPanel);
        mCheckoutListController.setCheckoutSuccessPanel(mCheckoutSuccessPanel);
        mCheckoutListController.setCheckoutPaymentWebviewPanel(mCheckoutPaymentWebviewPanel);
        mCheckoutListController.setCheckoutPaymentCreditCardPanel(mCheckoutPaymentCreditCardPanel);
        mCheckoutListController.setCartItemDetailPanel(mCartItemDetailPanel);
        mCheckoutListController.setPluginsService(pluginsService);
        mCheckoutListController.setPluginRewardPointPanel(mPluginRewardPointPanel);
        mCheckoutListController.setPluginStoreCreditPanel(mPluginStoreCreditPanel);
        mCheckoutListController.setPluginGiftCardPanel(mPluginGiftCardPanel);

        // controller quản lý danh sách khách hàng
        mProductListController = new ProductListController();
        mProductListController.setSubject(subjectObserv);
        mProductListController.setMagestoreContext(magestoreContext);
        mProductListController.setProdcutService(productService);
        mProductListController.setListPanel(mProductListPanel);
        mProductListController.setSubject(subjectObserv);
        mCheckoutListController.setProductListController(mProductListController);
        mProductListPanel.setAllowShowWarningWhileEmptyList();

        // controller quản lý đơn hàng
        mCheckoutCartItemListController = new CartItemListController();
        mCheckoutCartItemListController.setSubject(subjectObserv);
        mCheckoutCartItemListController.setMagestoreContext(magestoreContext);
        mCheckoutCartItemListController.setAutoSelectFirstItem(false);
        mCheckoutCartItemListController.setListPanel(mCartItemListPanel);
        mCheckoutCartItemListController.setDetailPanel(mCartItemDetailPanel);
        mCheckoutCartItemListController.setProductOptionPanel(mProductOptionPanel);
        mCheckoutCartItemListController.setCustomSalePanel(mCheckoutCustomSalePanel);
        mCheckoutCartItemListController.setChildListService(cartService);
        mCheckoutCartItemListController.setProductOptionService(productOptionService);
        mCheckoutCartItemListController.setCheckoutListController(mCheckoutListController);

        mCheckoutListController.setCartItemListController(mCheckoutCartItemListController);
        mCheckoutAddPaymentListController = new CheckoutAddPaymentListController();
        mCheckoutAddPaymentListController.setMagestoreContext(magestoreContext);
        mCheckoutAddPaymentListController.setListPanel(mCheckoutAddPaymentPanel);
        mCheckoutAddPaymentListController.setCheckoutListController(mCheckoutListController);

        // controller plugin giftcard
        mPluginGiftCardController = new PluginGiftCardController();
        mPluginGiftCardController.setMagestoreContext(magestoreContext);
        mPluginGiftCardController.setPluginsService(pluginsService);
        mPluginGiftCardController.setPluginGiftCardListPanel(mPluginGiftCardListPanel);
        mPluginGiftCardController.setCheckoutListController(mCheckoutListController);
        mPluginGiftCardController.setView(mPluginGiftCardPanel);

        mPaymentMethodListPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutDetailPanel.setCheckoutPaymentListPanel(mCheckoutPaymentListPanel);
        mCheckoutDetailPanel.setPaymentMethodListPanel(mPaymentMethodListPanel);
        mCheckoutDetailPanel.setCheckoutShippingListPanel(mCheckoutShippingListPanel);
        mCheckoutDetailPanel.setCheckoutAddPaymentPanel(mCheckoutAddPaymentPanel);
        mCheckoutDetailPanel.setCheckoutPaymentCreditCardPanel(mCheckoutPaymentCreditCardPanel);

        mCheckoutListPanel.setToolbarOrder(toolbar_order);
        mCheckoutListPanel.setMagestoreContext(magestoreContext);
        mCheckoutListPanel.setCustomerService(customerService);

        mCheckoutShippingListPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutPaymentListPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutAddPaymentPanel.setCheckoutListController(mCheckoutListController);
        mCartOrderListPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutAddressListPanel.setController(mCheckoutListController);
        mCheckoutSuccessPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutPaymentWebviewPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutPaymentCreditCardPanel.setCheckoutListController(mCheckoutListController);

        mCartItemDetailPanel.setCheckoutListController(mCheckoutListController);

        // plugins
        mPluginGiftCardPanel.setVisibility(ConfigUtil.isEnableGiftCard() ? View.VISIBLE : View.GONE);
        mPluginRewardPointPanel.setVisibility(ConfigUtil.isEnableRewardPoint() ? View.VISIBLE : View.GONE);
        mPluginStoreCreditPanel.setVisibility(ConfigUtil.isEnableStoreCredit() ? View.VISIBLE : View.GONE);
        mPluginGiftCardPanel.setPluginGiftCardController(mPluginGiftCardController);
        mPluginGiftCardListPanel.setPluginGiftCardController(mPluginGiftCardController);
        mPluginRewardPointPanel.setCheckoutListController(mCheckoutListController);
        mPluginStoreCreditPanel.setCheckoutListController(mCheckoutListController);

        // TODO: clear quote
//        DataUtil.removeDataStringToPreferences(getContext(), DataUtil.QUOTE);

        mProductListPanel.initModel();
        mCheckoutListPanel.initModel();
        mCartItemDetailPanel.initModel();
        mCartItemListPanel.initModel();
        mCategoryListPanel.initModel();

        // map các observ
        /////////////////////////////////
        // update product controller mỗi khi category được chọn
        mProductListController
                .attachListenerObserve()
                .setMethodName("bindCategory")
                .setStateCode(GenericState.DEFAULT_STATE_CODE_ON_SELECT_ITEM)
                .setControllerState(mCategoryListController);
        mSpinnerListPanel.setProductListController(mProductListController);

        // bắt sự kiện nhấn nút custom sales


        // bắt sự kiện cho phép thay đổi cart item
        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("blnAlowRemoveCartItem")
                .setStateCode(CheckoutListController.STATE_ENABLE_CHANGE_CART_ITEM)
                .setControllerState(mCheckoutListController);

        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("blnAlowRemoveCartItem")
                .setStateCode(CheckoutListController.STATE_DISABLE_CHANGE_CART_ITEM)
                .setControllerState(mCheckoutListController);

        // mỗi khi 1 product trên product list được ấn chọn
        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("bindProduct")
                .setStateCode(GenericState.DEFAULT_STATE_CODE_ON_SELECT_ITEM)
                .setControllerState(mProductListController);

        // mỗi khi 1 product trên product list được ấn chọn
        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("showProductDetail")
                .setStateCode(GenericState.DEFAULT_STATE_CODE_ON_LONG_CLICK_ITEM)
                .setControllerState(mProductListController);

        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("addCustomSale")
                .setStateCode(CheckoutListController.STATE_ADD_CUSTOM_SALE)
                .setControllerState(mCheckoutListController);

        // sự kiện mỗi khi có 1 checkout được chọn
        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("bindParent")
                .setStateCode(GenericState.DEFAULT_STATE_CODE_ON_SELECT_ITEM)
                .setControllerState(mCheckoutListController);

        // sự kiện mỗi khi có 1 re-order
        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("reOrder")
                .setStateCode(CheckoutListController.STATE_CODE_REORDER)
                .setControllerState(mCheckoutListController);

//        // hiển thị production opption nếu product có option
//        mCheckoutCartItemListController
//                .attachListenerObserve()
//                .setMethodName("doShowProductOptionInput")
//                .setStateCode(CartItemListController.STATE_ON_SHOW_PRODUCT_OPTION)
//                .setControllerState(mCheckoutCartItemListController);

        // cập nhật giá tổng trên view mỗi khi có 1 cart item được thay đổi
        mCheckoutListController
                .attachListenerObserve()
                .setMethodName("updateTotalPrice")
                .setStateCode(CartItemListController.STATE_ON_UPDATE_CART_ITEM)
                .setControllerState(mCheckoutCartItemListController);
    }

    @Override
    protected void initValue() {
        // Load danh sách danh mục sản phẩm
        mCategoryListController.doRetrieve();
        // Load danh sách sản phẩm, không tự động chọn sản phẩm đầu tiên
        mProductListController.doRetrieve();
        // Load danh sách check out
        mCheckoutListController.doRetrieve();
        // load danh sách shipping
//        mCheckShippingListController.doRetrieve();

        // controller giftcard bind data
        mPluginGiftCardController.bindDataToGiftCardList();

        rl_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckoutListPanel.showPopUpAddCustomer(CheckoutListPanel.NO_TYPE, CheckoutListPanel.NO_TYPE);
            }
        });

        rl_change_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckoutListPanel.showPopUpAddCustomer(CheckoutListPanel.CHANGE_CUSTOMER, CheckoutListPanel.NO_TYPE);
            }
        });

        IntentFilter filter_setting = new IntentFilter(SettingListController.RESET_DATA_TO_SALE_ACTIVITY);
        IntentFilter filter_order = new IntentFilter(OrderHistoryListController.SEND_ORDER_TO_SALE_ACTIVITY);
        IntentFilter filter_payment_id = new IntentFilter(PaymentPayPalActivity.SEND_PAYMENT_ID_TO_SALE_ACTIVITY);
        IntentFilter filter_error_paypal = new IntentFilter(PaymentPayPalActivity.SEND_ERROR_TO_SALE_ACTIVITY);
        registerReceiver(receiver_data_setting, filter_setting);
        registerReceiver(receiver_data_order, filter_order);
        registerReceiver(receiver_data_payment_paypal, filter_payment_id);
        registerReceiver(receiver_data_error_paypal, filter_error_paypal);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }

//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean navMenu = super.onNavigationItemSelected(item);

        // close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return navMenu;
    }

    @Override
    public void onBackPressed() {
        // nếu đang checkout thì back to home
        if (mCheckoutSuccessPanel.getVisibility() == View.VISIBLE || mCheckoutDetailPanel.getVisibility() == View.VISIBLE) {
            // nếu đã order success thì tạo new order
            if (mCheckoutListController.getSelectedItem().getOrderSuccess() != null) {
                mCheckoutListController.actionNewOrder();
                return;
            }

            // nếu đã check out xong thì new order
            mCheckoutListController.onBackTohome();
            return;
        }

        // thoát màn hình
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.ask_are_you_sure_to_close)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    // nhận data trả về từ setting
    BroadcastReceiver receiver_data_setting = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mCheckoutSuccessPanel.getVisibility() == View.VISIBLE || mCheckoutDetailPanel.getVisibility() == View.VISIBLE) {
                mCheckoutListController.onBackTohome();
            }
            mProductListController.bindCategory((Category) null);
            mCheckoutListController.getView().notifyDataSetChanged();
            mCheckoutListController.updateTotal();
            mProductListPanel.notifyDataSetChanged();
            mCartItemListPanel.notifyDataSetChanged();
            mCheckoutListPanel.notifyDataSetChanged();
        }
    };

    // nhận data trả về từ order history
    BroadcastReceiver receiver_data_order = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mOrder == null) return;
            //  nhận sự kiện khi click vào re-order trong order history, mOorder là static đã được gán ở bên OrderHistoryController
            // checkout controller -> tạo new order
            mCheckoutListController.reOrder(mOrder);
        }
    };

    // nhận data trả về từ Payment Paypal
    BroadcastReceiver receiver_data_payment_paypal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String payment_id = intent.getStringExtra("payment_id");
                mCheckoutListController.doInputApprovedPaymentPaypal(payment_id);
            }
        }
    };

    // nhận thông báo lỗi trả về từ Payment Paypal
    BroadcastReceiver receiver_data_error_paypal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String message = intent.getStringExtra("message");
                DialogUtil.confirm(getContext(), message, R.string.done);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (requestCode == REQUEST_PERMISSON_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mProductListPanel.scanBarcode();
                }
            }
        }
    }
}
