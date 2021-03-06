package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CustomerAddressListController;
import com.magestore.app.pos.controller.CustomerListController;
import com.magestore.app.pos.databinding.CardCustomerAddressContentBinding;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.DialogUtil;

/**
 * Panel hiển thị danh sách các địa chỉ của 1 khách hàng
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerAddressListPanel extends AbstractListPanel<CustomerAddress> {

    /**
     * Các hàm khởi tạo
     *
     * @param context
     */
    public CustomerAddressListPanel(Context context) {
        super(context);
    }

    public CustomerAddressListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerAddressListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, final CustomerAddress item, int position) {
        // Đặt các trường text vào danh sách
        CardCustomerAddressContentBinding binding = DataBindingUtil.bind(view);
        binding.setCustomerAddress(item);

        // bắt sự kiện ấn nút edit customerAddress
        ((ImageButton) view.findViewById(R.id.btn_adrress_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateItemInput(item);
            }
        });

        // bắt sự kiện ấn nút delete customerAddress
        ((ImageButton) view.findViewById(R.id.btn_adrress_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteItemInput(item);
            }
        });
    }


    /**
     * Hiển thị dialog confirm trước khi delete
     *
     * @param item
     */
    @Override
    public void showDeleteItemInput(final CustomerAddress item) {
        final Context context = getContext();
        // Tạo dialog và hiển thị
        DialogUtil.confirm(context,
                R.string.customer_delete_address,
                R.string.title_confirm_delete,
                R.string.yes,
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mController.doDelete(item);
                    }
                });
    }

    /**
     * Hiển thị dialog tạo mới địa chỉ
     */
    @Override
    public void showInsertItemInput() {
        // Chuẩn bị layout cho dialog
        final CustomerAddressDetailPanel panelAddress = new CustomerAddressDetailPanel(getContext());
        panelAddress.setController(mController);
        panelAddress.initValue();
        if (mController instanceof CustomerAddressListController)
            panelAddress.bindItem(((CustomerAddressListController) mController).createNewCustomerAddress());

        // khởi tạo và hiển thị dialog
        final MagestoreDialog dialog = com.magestore.app.pos.util.DialogUtil.dialog(getContext(),
                getContext().getString(R.string.customer_update_address),
                panelAddress);
        dialog.show();

        // Xử lý khi nhấn save trên dialog
        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!panelAddress.checkRequiedAddress()) {
                    return;
                }
                CustomerAddress item = ((CustomerAddressListController) mController).createNewCustomerAddress();
                panelAddress.bind2Item(item);
                mController.doInsert(item);
                dialog.dismiss();
            }
        });
    }

    /**
     * Hiển thị dialog edit địa chỉ
     *
     * @param item
     */
    @Override
    public void showUpdateItemInput(final CustomerAddress item) {
        // Chuẩn bị layout cho dialog customerAddress
        final CustomerAddressDetailPanel panelAddress = new CustomerAddressDetailPanel(getContext());
        panelAddress.setController(mController);
        panelAddress.initValue();
        panelAddress.bindItem(item);

        // khởi tạo dialog
        final MagestoreDialog dialog = com.magestore.app.pos.util.DialogUtil.dialog(getContext(),
                getContext().getString(R.string.customer_update_address),
                panelAddress);
        dialog.show();

        // Xử lý khi nhấn save trên dialog
        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!panelAddress.checkRequiedAddress()) {
                    return;
                }
                CustomerAddress newitem = ((CustomerAddressListController) mController).createNewCustomerAddress();
                panelAddress.bind2Item(newitem);
                mController.doUpdate(item, newitem);
                dialog.dismiss();
            }
        });
    }

    public void showToastNotify(){
        String message = getContext().getString(R.string.customer_create_success);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
