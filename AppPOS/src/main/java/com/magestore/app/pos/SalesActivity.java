package com.magestore.app.pos;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.magestore.app.lib.context.MagestoreContext;
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
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.controller.CategoryListController;
import com.magestore.app.pos.controller.CheckoutAddPaymentListController;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.controller.ProductListController;
import com.magestore.app.pos.controller.ProductOptionController;
import com.magestore.app.pos.panel.CartItemDetailPanel;
import com.magestore.app.pos.panel.CartItemListPanel;
import com.magestore.app.pos.panel.CartOrderListPanel;
import com.magestore.app.pos.panel.CategoryListPanel;
import com.magestore.app.pos.panel.CheckoutAddPaymentPanel;
import com.magestore.app.pos.panel.CheckoutAddressListPanel;
import com.magestore.app.pos.panel.CheckoutDetailPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentListPanel;
import com.magestore.app.pos.panel.CheckoutShippingListPanel;
import com.magestore.app.pos.panel.PaymentMethodListPanel;
import com.magestore.app.pos.panel.ProductListPanel;
import com.magestore.app.pos.panel.ProductOptionPanel;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.view.ui.PosUI;

/**
 * Quản lý các hoạt động salé
 * Tạo sales mới, hold và check out thanh toán
 */
public class SalesActivity extends AbstractActivity
        implements
        PosUI {
    MagestoreContext magestoreContext;

    // 2 pane
    private boolean mTwoPane;

    // Panel chứa danh sách mặt hàng và đơn hàng
    private ProductListPanel mProductListPanel;
    private CheckoutListPanel mCheckoutListPanel;
    private CheckoutDetailPanel mCheckoutDetailPanel;
    private CheckoutShippingListPanel mCheckoutShippingListPanel;
    private CheckoutPaymentListPanel mCheckoutPaymentListPanel;

    // item panel
    private CartItemListPanel mCartItemListPanel;
    private CartItemDetailPanel mCartItemDetailPanel;

    private CategoryListPanel mCategoryListPanel;
    private PaymentMethodListPanel mPaymentMethodListPanel;
    private CheckoutAddPaymentPanel mCheckoutAddPaymentPanel;
    private CartOrderListPanel mCartOrderListPanel;
    private CheckoutAddressListPanel mCheckoutAddressListPanel;
    private ProductOptionPanel mPanelProductOption;

    // controller cho danh sách mặt hàng và đơn hàng
    private ProductListController mProductListController;
    private CheckoutListController mCheckoutListController;
    private CartItemListController mCheckoutCartItemListController;
    private CategoryListController mCategoryListController;
    private CheckoutAddPaymentListController mCheckoutAddPaymentListController;
    private ProductOptionController mProductOptionController;

    // Toolbar Order
    Toolbar toolbar_order;
    RelativeLayout rl_customer;
    ImageButton rl_change_customer;
    CustomerService customerService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_menu);
        // chuẩn bị control layout
        initLayout();

        // chuẩn bị các model
        initModel();

        // chuản bị các value trong layout
        initValue();
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
                        .setAction("Action", null).show();
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

        // cart item panel
        mCartItemListPanel = (CartItemListPanel) mCheckoutListPanel.findViewById(R.id.order_item_panel);
        mCartItemDetailPanel = new CartItemDetailPanel(this.getContext());

        mCartOrderListPanel = (CartOrderListPanel) mCheckoutListPanel.findViewById(R.id.checkout_item_panel);

        // shipping list panel
        mCheckoutShippingListPanel = (CheckoutShippingListPanel) mCheckoutDetailPanel.findViewById(R.id.checkout_shipping_list_panel);

        // payment list panel
        mCheckoutPaymentListPanel = (CheckoutPaymentListPanel) mCheckoutDetailPanel.findViewById(R.id.checkout_payment_list_panel);
        // payment thod panel
        mPaymentMethodListPanel = (PaymentMethodListPanel) mCheckoutDetailPanel.findViewById(R.id.payment_method_list_panel);

        // shipping address panel
        mCheckoutAddressListPanel = (CheckoutAddressListPanel) mCheckoutDetailPanel.findViewById(R.id.address_list_panel);

        // add payment panel
        mCheckoutAddPaymentPanel = new CheckoutAddPaymentPanel(getContext());

        // category list panel
        mCategoryListPanel = (CategoryListPanel) mProductListPanel.findViewById(R.id.category);

        // panel hiển thị product option
        mPanelProductOption = new ProductOptionPanel(getContext());
//        mPanelProductOption.setLayoutPanel(R.layout.panel_product_option_list);

    }

    protected void initModel() {
        // Context sử dụng xuyên suốt hệ thống
        magestoreContext = new MagestoreContext();
        magestoreContext.setActivity(this);

        // subjectObserv
        SubjectObserv subjectObserv = new SubjectObserv();

        // chuẩn bị service
        ServiceFactory factory;
        ProductService productService = null;
        CheckoutService checkoutService = null;
        CartService cartService = null;
        CategoryService categoryService = null;
        ConfigService configService = null;
        ProductOptionService productOptionService = null;
        CustomerAddressService customerAddressService = null;
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // controller quản lý category
        mCategoryListController = new CategoryListController();
        mCategoryListController.setMagestoreContext(magestoreContext);
        mCategoryListController.setListPanel(mCategoryListPanel);
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

        // controller quản lý danh sách khách hàng
        mProductListController = new ProductListController();
        mProductListController.setSubject(subjectObserv);
        mProductListController.setMagestoreContext(magestoreContext);
        mProductListController.setProdcutService(productService);
        mProductListController.setListPanel(mProductListPanel);
        mProductListController.setCheckoutListController(mCheckoutListController);
        mProductListController.setCategoryListController(mCategoryListController);
        mProductListController.setSubject(subjectObserv);
        mCheckoutListController.setProductListController(mProductListController);
        mCategoryListController.setProductListController(mProductListController);

        // controller quản lý đơn hàng
        mCheckoutCartItemListController = new CartItemListController();
        mCheckoutCartItemListController.setSubject(subjectObserv);
        mCheckoutCartItemListController.setMagestoreContext(magestoreContext);
        mCheckoutCartItemListController.setListPanel(mCartItemListPanel);
        mCheckoutCartItemListController.setDetailPanel(mCartItemDetailPanel);
        mCheckoutCartItemListController.setChildListService(cartService);
        mCheckoutCartItemListController.setSubject(subjectObserv);

        mCheckoutListController.setCartItemListController(mCheckoutCartItemListController);

        mCheckoutAddPaymentListController = new CheckoutAddPaymentListController();
        mCheckoutAddPaymentListController.setMagestoreContext(magestoreContext);
        mCheckoutAddPaymentListController.setListPanel(mCheckoutAddPaymentPanel);
        mCheckoutAddPaymentListController.setCheckoutListController(mCheckoutListController);

        mProductOptionController = new ProductOptionController();
        mProductOptionController.setSubject(subjectObserv);
        mProductOptionController.setView(mPanelProductOption);
        mProductOptionController.setService(productOptionService);


        mPaymentMethodListPanel.setCheckoutListController(mCheckoutListController);

        mCheckoutDetailPanel.setCheckoutPaymentListPanel(mCheckoutPaymentListPanel);
        mCheckoutDetailPanel.setPaymentMethodListPanel(mPaymentMethodListPanel);
        mCheckoutDetailPanel.setCheckoutShippingListPanel(mCheckoutShippingListPanel);
        mCheckoutDetailPanel.setCheckoutAddPaymentPanel(mCheckoutAddPaymentPanel);

        mCheckoutListPanel.setToolbarOrder(toolbar_order);
        mCheckoutListPanel.setMagestoreContext(magestoreContext);
        mCheckoutListPanel.setCustomerService(customerService);

        mCheckoutShippingListPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutPaymentListPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutAddPaymentPanel.setCheckoutListController(mCheckoutListController);
        mCartOrderListPanel.setCheckoutListController(mCheckoutListController);
        mCheckoutAddressListPanel.setCheckoutListController(mCheckoutListController);

        // TODO: clear quote
//        DataUtil.removeDataStringToPreferences(getContext(), DataUtil.QUOTE);

        mProductListPanel.initModel();
        mCheckoutListPanel.initModel();
        mCartItemDetailPanel.initModel();
        mCartItemListPanel.initModel();
        mCategoryListPanel.initModel();

        // map các observ
        /////////////////////////////////
        // mỗi khi 1 product trên product list được ấn chọn
        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("bindProduct")
                .setStateCode(GenericState.DEFAULT_STATE_CODE_ON_SELECT_ITEM)
                .setControllerState(mProductListController);

        // sự kiện mỗi khi có 1 checkout được chọn
        mCheckoutCartItemListController
                .attachListenerObserve()
                .setMethodName("bindParent")
                .setStateCode(GenericState.DEFAULT_STATE_CODE_ON_SELECT_ITEM)
                .setControllerState(mCheckoutListController);

        // hiển thị production opption nếu product có option
        mProductOptionController
                .attachListenerObserve()
                .setMethodName("doShowProductOptionInput")
                .setStateCode(CartItemListController.STATE_ON_SHOW_PRODUCT_OPTION)
                .setControllerState(mCheckoutCartItemListController);

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

        mCheckoutListController.doInputGuestCheckout();

        rl_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckoutListPanel.showPopUpAddCustomer(1);
            }
        });

        rl_change_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckoutListPanel.showPopUpAddCustomer(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
}
