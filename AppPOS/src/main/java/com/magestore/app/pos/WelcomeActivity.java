package com.magestore.app.pos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.util.DataUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class WelcomeActivity extends AbstractActivity {
    MagestoreContext magestoreContext;
    ServiceFactory factory;
    UserService userService = null;
    SimpleSpinner sp_store, sp_cash_drawer;
    List<Store> listStore;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_menu);

        // chuẩn bị control layout
        initLayout();

        // chuẩn bị các model
        initModel();

        // chuản bị các value trong layout
        initValue();
    }

    @Override
    protected void initLayout() {
        sp_store = (SimpleSpinner) findViewById(R.id.sp_store);
        sp_cash_drawer = (SimpleSpinner) findViewById(R.id.sp_cash_drawer);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initModel() {
        // Context sử dụng xuyên suốt hệ thống
        magestoreContext = new MagestoreContext();
        magestoreContext.setActivity(this);

        // chuẩn bị service
        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            userService = factory.generateUserService();
            listStore = userService.getListStore();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initValue() {
        if(listStore != null && listStore.size() == 1){
            Store store = listStore.get(0);
            DataUtil.saveDataStringToPreferences(getContext(), DataUtil.STORE_ID, store.getID());
            DataUtil.saveDataStringToPreferences(getContext(), DataUtil.STORE_NAME, store.getName());
            navigationToSalesActivity();
            DataUtil.saveDataBooleanToPreferences(getContext(), DataUtil.CHOOSE_STORE, true);
        }

        if (listStore != null) {
            sp_store.bind(listStore.toArray(new Store[0]));
        }

        sp_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DataUtil.saveDataStringToPreferences(getContext(), DataUtil.STORE_ID, sp_store.getSelection());
                DataUtil.saveDataStringToPreferences(getContext(), DataUtil.STORE_NAME, sp_store.getSelectionValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationToSalesActivity();
                DataUtil.saveDataBooleanToPreferences(getContext(), DataUtil.CHOOSE_STORE, true);
            }
        });
    }

    private void navigationToSalesActivity() {
        // Đăng nhập thành công, mở sẵn form sales
        Intent intent = new Intent(getContext(), SalesActivity.class);
        startActivity(intent);
        finish();
    }
}
