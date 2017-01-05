package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.magestore.app.lib.entity.Address;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.usecase.CustomerUseCase;
import com.magestore.app.lib.usecase.UseCaseFactory;
import com.magestore.app.pos.R;
import com.magestore.app.pos.util.DialogUtil;

import java.util.List;

/**
 * Hiển thị và quản lý các thông tin chi tiết của 1 customer
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerDetailPanel extends FrameLayout {
    // View quản lý danh sách địa chỉ
    RecyclerView mCustomerAddressRecyclerView;

    // Listner
    CustomerDetailPanelListener mCustomerListPanelListener;

    // khách hàng
    Customer mCustomer;
    CustomerUseCase mCustomerUsecase;

    // các control
    ImageButton mbtnCheckOut;
    ImageButton mbtnNewAddress;
    TextView mtxtFirstName;
    TextView mtxtLastName;
    TextView mtxtEmail;
    TextView mtxtGroup;

    public CustomerDetailPanel(Context context) {
        super(context);
        init();
    }

    public CustomerDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initControlLayout();
        initControlValue();
        initTask();
    }

    private void initControlLayout() {
        // Load layout view thông tin khách hàng chi tiết
        View v = inflate(getContext(), R.layout.panel_customer_detail, null);
        addView(v);

        // load các control vào các biến
        mtxtFirstName = (TextView) findViewById(R.id.txt_first_name);
        mtxtLastName = (TextView) findViewById(R.id.txt_last_name);
        mtxtEmail = (TextView) findViewById(R.id.txt_email);
        mtxtGroup = (TextView) findViewById(R.id.txt_group);
        mbtnCheckOut = (ImageButton) findViewById(R.id.btn_check_out);

        // danh mục địa chỉ
        mCustomerAddressRecyclerView = (RecyclerView) findViewById(R.id.customer_address);
        mCustomerAddressRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        mCustomerAddressRecyclerView.setAdapter(new CustomerDetailPanel.CustomerAddressRecyclerViewAdapter());

        // các button
        mbtnNewAddress = (ImageButton) findViewById(R.id.btn_new_address);
        mbtnCheckOut = (ImageButton) findViewById(R.id.btn_check_out);

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
    }

    /**
     * Khởi tạo các value cho các control
     */
    private void initControlValue() {
        // Khởi tạo customer use case
        mCustomerUsecase = UseCaseFactory.generateCustomerUseCase(null, null);
    }

    private void initTask() {

    }

    /**
     * Sự kiện khi ấn nút checkout
     * @param v
     */
    public void onClickCheckout(View v) {

    }

    /**
     * Sự kiện khi ấn nút new address
     * @param v
     */
    public void onClickNewAddress(View v) {
        // Chuẩn bị layout cho dialog address
        final CustomerAddressPanel panelAddress = new CustomerAddressPanel(getContext());

        // Khởi tạo sẵn address mới
        Address address = mCustomerUsecase.createAddress();
        panelAddress.setAddress(address);

        // Tạo dialog và hiển thị
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("New address")
                .setView(panelAddress)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mCustomerUsecase.newAddress(panelAddress.getAddress());
                        mCustomerAddressRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    /**
     * Sự kiện khi ấn nút new address
     * @param v
     */
    public void onClickDeleteAddress(View v, final Address address, int position) {
        DialogUtil.confirmDelete(getContext(), address.getName(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa trong use case
                mCustomerUsecase.deleteAddress(address);

                // Báo view cập nhật
                mCustomerAddressRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });

    }

    /**
     * Sự kiện khi ấn nút checkout
     * @param v
     */
    public void onClickEditAddress(View v, Address address, int position) {
        // Chuẩn bị layout cho dialog address
        final CustomerAddressPanel panelAddress = new CustomerAddressPanel(getContext());
        panelAddress.setAddress(address);

        // Tạo dialog và hiển thị
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Update address")
                .setView(panelAddress)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mCustomerUsecase.updateAddress(panelAddress.getAddress());
                        mCustomerAddressRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    /**
     * Listener bắt sự kiện
     * @param customerPanelListener
     */
    public void setListener(CustomerDetailPanelListener customerPanelListener) {
        mCustomerListPanelListener = customerPanelListener;
    }

    /**
     * Đặt customer
     * @param customer
     */
    public void setCustomer(Customer customer) {
        mCustomer = customer;
        mCustomerUsecase.setCustomer(mCustomer);

        // gán các text box
        mtxtFirstName.setText(getContext().getText(R.string.first_name) + ": " + customer.getFirstName());
        mtxtLastName.setText(getContext().getText(R.string.last_name) + ": " +customer.getLastName());
        mtxtEmail.setText(getContext().getText(R.string.email) + ": " +customer.getEmail());
        mtxtGroup.setText(getContext().getText(R.string.group) + ": " +customer.getGroupID());

        // cập nhật địa chỉ
        ((CustomerDetailPanel.CustomerAddressRecyclerViewAdapter) mCustomerAddressRecyclerView.getAdapter()).setCustomer(mCustomer);
    }

    /**
     * AdapterView2Entity map Customer Address sang view
     */
    public class CustomerAddressRecyclerViewAdapter
            extends RecyclerView.Adapter<CustomerDetailPanel.CustomerAddressRecyclerViewAdapter.CustomerAddressViewHolder> {

        // đánh dấu vị trí đã chọn
        private int selectedPos = 0;

        // Danh sách khách hàng của view
        private List<Address> mAddressList = null;

        public CustomerAddressRecyclerViewAdapter() {
        }

        public void setCustomer(Customer customer) {
            mAddressList = customer.getAddress();
            notifyDataSetChanged();
        }

        /**
         * Khởi tạo danh sách địa chỉ
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public CustomerDetailPanel.CustomerAddressRecyclerViewAdapter.CustomerAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_customer_address_content, parent, false);
            return new CustomerDetailPanel.CustomerAddressRecyclerViewAdapter.CustomerAddressViewHolder(view);
        }

        /**
         * Map dataset sang view
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final CustomerDetailPanel.CustomerAddressRecyclerViewAdapter.CustomerAddressViewHolder holder, final int position) {
            final Address address = mAddressList.get(position);
            holder.mAddress = address;

            // Đặt các trường text vào danh sách
            holder.mtxtAddressName.setText(address.getName());
            holder.mtxtAddressFull.setText(address.getFullAddress());
            holder.mtxtAddressPostCode.setText(address.getPostCode());
            holder.mtxtAddressTelephone.setText(address.getTelephone());

            // bắt sự kiện ấn nút edit address
            holder.mbtnEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEditAddress(v, address, position);
                }
            });

            // bắt sự kiện ấn nút delete address
            holder.mbtnDeleteAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDeleteAddress(v, address, position);
                }
            });
        }

        /**
         * Đếm số bản ghi trong view
         *
         * @return
         */
        @Override
        public int getItemCount() {
            if (mAddressList == null) return 0;
            return mAddressList.size();
        }

        /**
         * Nắm giữ từng view con trong list
         */
        public class CustomerAddressViewHolder extends RecyclerView.ViewHolder {
            public final TextView mtxtAddressName;
            public final TextView mtxtAddressFull;
            public final TextView mtxtAddressPostCode;
            public final TextView mtxtAddressTelephone;
            public Address mAddress;
            public View mView;

            // các button edit và delete
            public final ImageButton mbtnDeleteAddress;
            public final ImageButton mbtnEditAddress;

            public CustomerAddressViewHolder(View view) {
                super(view);
                mView = view;

                // Tham chiếu các edit text
                mtxtAddressName = (TextView) view.findViewById(R.id.txt_adrress_name);
                mtxtAddressFull = (TextView) view.findViewById(R.id.txt_adrress_full);
                mtxtAddressPostCode = (TextView) view.findViewById(R.id.txt_adrress_postcode);
                mtxtAddressTelephone = (TextView) view.findViewById(R.id.txt_adrress_telephone);

                // Tham chiếu các button
                mbtnEditAddress = (ImageButton) view.findViewById(R.id.btn_adrress_edit);
                mbtnDeleteAddress = (ImageButton) view.findViewById(R.id.btn_adrress_delete);
            }
        }
    }
}
