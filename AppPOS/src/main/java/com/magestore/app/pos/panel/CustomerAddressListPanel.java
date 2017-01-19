package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardCustomerAddressContentBinding;
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
     * @param context
     */
    public CustomerAddressListPanel(Context context) {
        super(context);
    }
    public CustomerAddressListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomerAddressListPanel(Context context, AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);}

    /**
     * Chuẩn bị layout
     */
    @Override
    public void initLayout() {

        // Load layout view danh sách địa chỉ của khách hàng
//        View v = inflate(getContext(), R.layout.panel_customer_address_list, null);
//        addView(v);

        // Chuẩn bị layout từng item trong danh sách khách hàng
//        setLayoutItem(R.layout.card_customer_address_content);

        // Chuẩn bị list danh sách khách hàng
//        mRecycleView = (RecyclerView) findViewById(getListLayout());
//        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
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
                showUpdateItem(item);
            }
        });

        // bắt sự kiện ấn nút delete customerAddress
        ((ImageButton) view.findViewById(R.id.btn_adrress_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteItem(item);
            }
        });
    }

    /**
     * Hiển thị dialog confirm trước khi delete
     * @param item
     */
    public void showDeleteItem(final CustomerAddress item) {
        final Context context = getContext();
        // Tạo dialog và hiển thị
        DialogUtil.confirm(context,
                R.string.customer_delete_address,
                R.string.title_confirm_delete,
                R.string.yes,
                R.string.no,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        mController.doDeleteItem(item);
                        notifyDatasetChanged();
                    }
                });
    }

    /**
     * Hiển thị dialog edit địa chỉ
     * @param item
     */
    public void showUpdateItem(final CustomerAddress item) {
        // Chuẩn bị layout cho dialog customerAddress
        final CustomerAddressDetailPanel panelAddress = new CustomerAddressDetailPanel(getContext());
        panelAddress.setLayoutPanel(R.layout.panel_customer_address_detail);
        panelAddress.bindItem(item);
        panelAddress.setController(mController);

        final Context context = getContext();
        // Tạo dialog và hiển thị
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(context.getString(R.string.customer_update_address))
                .setView(panelAddress)
                .setPositiveButton(context.getString(R.string.save), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        panelAddress.bind2Item();
                        mController.doUpdateItem(item);
                        notifyDatasetChanged();
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                }).create();
        dialog.show();
    }
}
