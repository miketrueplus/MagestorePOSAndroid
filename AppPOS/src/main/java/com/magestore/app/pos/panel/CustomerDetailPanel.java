package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.controller.CustomerAddressListController;
import com.magestore.app.pos.controller.CustomerListController;
import com.magestore.app.pos.databinding.PanelCustomerDetailBinding;
import com.magestore.app.pos.R;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.CustomerComplainListView;
import com.magestore.app.pos.view.MagestoreDialog;

import java.util.Map;

/**
 * Hiển thị và quản lý các thông tin chi tiết của 1 customer
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerDetailPanel extends AbstractDetailPanel<Customer> {
    // controller và view để quản lý địa chỉ của khách hàng
    CustomerAddressListPanel mCustomerAddressListPanel;
    CustomerAddressListController mCustomerAddressListController;

    // complain của khách hàng
    CustomerComplainListView mCustomerComplainListView;

    // các control button
    ImageButton mbtnEditCustomer;
    Button mbtnSaveCustomer;
    ImageButton mbtnCheckOut;
    ImageButton mbtnNewAddress;
    ImageButton mbtnNewComplain;

    // Edit text trên form
    EditText mtxtFirstName;
    EditText mtxtLastName;
    EditText mtxtEmail;
    SimpleSpinner mspinGroupID;

    EditText mtxtComplain;

    PanelCustomerDetailBinding mBinding;

    /**
     * Khởi tạo
     *
     * @param context
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public CustomerDetailPanel(Context context) throws InstantiationException, IllegalAccessException {
        super(context);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public CustomerDetailPanel(Context context, AttributeSet attrs) throws InstantiationException, IllegalAccessException {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public CustomerDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) throws InstantiationException, IllegalAccessException {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Khởi tạo các layout
     */
    @Override
    protected void initLayout() {
        // Load layout view thông tin khách hàng chi tiết
        mBinding = DataBindingUtil.bind(getView());

        // chuẩn bị panel view danh sách địa chỉ khách hàng
        mCustomerAddressListPanel = (CustomerAddressListPanel) findViewById(R.id.customer_address);

        // chuẩn bị panel complain của khách hàng
        mCustomerComplainListView = (CustomerComplainListView) findViewById(R.id.complain_list_panel);

        // các edit text
        mtxtFirstName = (EditText) findViewById(R.id.firstname);
        mtxtLastName = (EditText) findViewById(R.id.lastname);
        mtxtEmail = (EditText) findViewById(R.id.email);
        mspinGroupID = (SimpleSpinner) findViewById(R.id.spinner_group_id);

        // các button
        mbtnSaveCustomer = (Button) findViewById(R.id.btn_edit_save_customer);
        mbtnEditCustomer = (ImageButton) findViewById(R.id.btn_edit_customer);
        mbtnNewAddress = (ImageButton) findViewById(R.id.btn_new_address);
        mbtnCheckOut = (ImageButton) findViewById(R.id.btn_check_out);
        mbtnNewComplain = (ImageButton) findViewById(R.id.btn_new_complain);

        mbtnSaveCustomer.setVisibility(INVISIBLE);
        mbtnEditCustomer.setVisibility(VISIBLE);

        // xử lý khi ấn edit
        mbtnEditCustomer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiện nút save
                mbtnSaveCustomer.setVisibility(VISIBLE);
                mbtnEditCustomer.setVisibility(INVISIBLE);

                // cho phép edit các textbox
                mtxtFirstName.setEnabled(true);
                mtxtLastName.setEnabled(true);
                mtxtEmail.setEnabled(true);
                mspinGroupID.setEnabled(true);

//                onClickNewAddress(v);
            }
        });

        mbtnSaveCustomer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // dấu nút save
                mbtnSaveCustomer.setVisibility(INVISIBLE);
                mbtnEditCustomer.setVisibility(VISIBLE);

                // k0 cho phép edit các textbox
                mtxtFirstName.setEnabled(false);
                mtxtLastName.setEnabled(false);
                mtxtEmail.setEnabled(false);
                mspinGroupID.setEnabled(false);

//                Customer customer = mController.createItem();
                bind2Item(mController.getSelectedItem());
                mController.doUpdate(mController.getSelectedItem(), mController.getSelectedItem());
            }
        });

        // Xử lý sự kiện các button new address
        mbtnNewAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNewAddress(v);
            }
        });

        // Xử lý sự kiện button checkout
        mbtnCheckOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCheckout(v);
            }
        });

        // tạo mới 1 complain
        mbtnNewComplain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNewComplain(v);
            }
        });
    }

    /**
     * Chuẩn bị các model, controller
     */
    @Override
    public void initModel() {
        // Lấy lại customer service từ controller của panel này và đặt cho controlller địa chỉ
        Controller controller = getController();

        // Chuẩn bị controller quản lý danh sách địa chỉ khách hàng
        mCustomerAddressListController = new CustomerAddressListController();
        mCustomerAddressListController.setView(mCustomerAddressListPanel);
        mCustomerAddressListController.setMagestoreContext(controller.getMagestoreContext());

        // xác định controller là customer list
        if (controller instanceof CustomerListController)
            mCustomerAddressListController.setCustomerService(((CustomerListController) controller).getCustomerService());
    }

    /**
     * Sự kiện khi ấn nút checkout
     *
     * @param v
     */
    public void onClickCheckout(View v) {

    }

    /**
     * Sự kiện thêm 1 complain mới
     *
     * @param v
     */
    public void onClickNewComplain(View v) {
        // chuẩn bị layout cho dialog hiển thị
        View panelComplain = inflate(getContext(), R.layout.panel_customer_input_complain, null);
        final EditText txtComplain = (EditText) panelComplain.findViewById(R.id.text_input_complain);

        // khởi tạo dialog
        final MagestoreDialog dialog = DialogUtil.dialog(getContext(),
                getContext().getString(R.string.complain),
                panelComplain);
        dialog.show();

        // Xử lý khi nhấn save trên dialog
        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String strComplain = txtComplain.getText().toString().trim();
                if (!(TextUtils.isEmpty(strComplain))) {
                    CustomerListController controller = (CustomerListController) CustomerDetailPanel.this.getController();
                    controller.doInputNewComplain(strComplain);
                    dialog.dismiss();
                } else {
                    txtComplain.setError(getContext().getString(R.string.err_field_required));
                }
            }
        });
    }

    /**
     * Sự kiện khi ấn nút new address
     *
     * @param v
     */
    public void onClickNewAddress(View v) {
        // Chuẩn bị layout cho dialog customerAddress
        mCustomerAddressListPanel.showNewItem();
    }

    /**
     * Gán giá trị customer group cho spinner
     * @param customerGroupDataSet
     */
    public void setCustomerGroupDataSet(Map<String, String> customerGroupDataSet) {
        mspinGroupID.bind(customerGroupDataSet);
    }

    /**
     * Chỉ định customer được chọn, cập nhật view
     *
     * @param item
     */
    @Override
    public void bindItem(Customer item) {
        super.bindItem(item);

        // map thông tin customer lên panel
        mBinding.setCustomerDetail(item);
        mspinGroupID.setSelection(item.getGroupID());

        // chỉ định adress controller hiển thị các address lên
        mCustomerAddressListController.bindCustomer(item);

        // chỉ định complain list cho hiển thị
        if (item.getComplain() != null) mCustomerComplainListView.bindList(item.getComplain());
    }

    /**
     * Cập nhật view khi dữ liệu được thanh đổi
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mCustomerAddressListPanel.notifyDataSetChanged();
        mCustomerComplainListView.notifyDataSetChanged();
    }

    @Override
    public void bind2Item(Customer item) {
        item.setEmail(mtxtEmail.getText().toString().trim());
        item.setFirstName(mtxtFirstName.getText().toString().trim());
        item.setLastName(mtxtLastName.getText().toString().trim());
        item.setGroupID(mspinGroupID.getSelection());
    }
}
