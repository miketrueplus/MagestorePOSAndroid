package com.magestore.app.pos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.*;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.task.Task;
import com.magestore.app.lib.task.TaskListener;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.task.AssignPosTask;
import com.magestore.app.pos.task.ListStoreTask;
import com.magestore.app.pos.task.LoginTask;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.pos.ui.LoginUI;
import com.magestore.app.util.AndroidNetworkUtil;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.util.StringUtil;

import java.util.List;

/**
 * Màn hình login
 */
public class LoginActivity extends AbstractActivity implements LoginUI {
    /**
     * Tác vụ để login
     */
    private LoginTask mAuthTask = null;
    private ListStoreTask mStoreTask = null;
    private AssignPosTask mAssignPosTask = null;

    // UI references.
    private AutoCompleteTextView mDomainView;
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public static String STORE_ID = "";
    private RelativeLayout email_login_form, point_of_sales_form;
    private SimpleSpinner sp_pos;
    private List<PointOfSales> mListPos;
    private TextView error_pos;
    private Button mStartButton;
    private boolean mCheckLoginDemo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Tải view từ template xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        STORE_ID = DataUtil.getDataStringToPreferences(getContext(), DataUtil.STORE_ID);

        // cấu hình lại các layout control
        initControlLayout();

        // thiết lập các giá trị ban đầu
        initControlValue();

        showProgress(false);
    }

    /**
     * Cấu hình lại các control layout
     */
    protected void initControlLayout() {
        // Tham chiếu đến các trường và điền các giá trị vào
        mDomainView = (AutoCompleteTextView) findViewById(R.id.domain);
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.user_name);

        // sự kiện nhấn next ở bàn phím
        mDomainView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    mUserNameView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        // sự kiện nhấn next ở bàn phím
        mUserNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    mPasswordView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        // Sự kiện nhấn enter ở ô password, thực hiện login luôn
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // Sự kiện nút login được nhấn
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mDemoButton = (Button) findViewById(R.id.demo_button);
        mDemoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLoginDemo();
            }
        });

        // Tham chiếu đến form và progress
        email_login_form = (RelativeLayout) findViewById(R.id.email_login_form);
        point_of_sales_form = (RelativeLayout) findViewById(R.id.point_of_sales_form);
        sp_pos = (SimpleSpinner) findViewById(R.id.sp_pos);
        error_pos = (TextView) findViewById(R.id.error_pos);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        sp_pos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PointOfSales pos = getPointOfSales(sp_pos.getSelection());
                LoginActivity.STORE_ID = pos.getStoreId();
                DataUtil.saveDataStringToPreferences(getContext(), DataUtil.STORE_ID, pos.getStoreId());
                ConfigUtil.setPointOfSales(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStartButton.getText().toString().equals(getContext().getString(R.string.start))) {
                    if (!sp_pos.getSelection().equals("")) {
                        navigationToSalesActivity();
                        assigPos(sp_pos.getSelection());
                    } else {
                        DialogUtil.confirm(getContext(), getContext().getString(R.string.notify_select_pos), R.string.ok);
                    }
                } else {
                    email_login_form.setVisibility(View.VISIBLE);
                    point_of_sales_form.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Tự động điền giá trị cho các trường trong form
     */
    protected void initControlValue() {
        // Lấy domain là domain của lần đăng nhập cuối mà thành công
        mDomainView.setText(getSharedValue("login_activity_domain", "").trim());
        mUserNameView.setText(getSharedValue("login_activity_username", "").trim());
        mPasswordView.setText(getSharedValue("login_activity_password", "").trim());
    }

    /**
     * Kiểm tra giá trịc ác control trước khi login
     *
     * @return
     */
    protected boolean validControlValue() {
        // Kiểm tra kết nối mạng trước, nếu không có thì báo lỗi
        if (!AndroidNetworkUtil.isNetworkAvaiable(this)) {
            showErrorMsg(getApplicationContext().getString(R.string.err_no_network_connection));
        }

        // lấy giá trị khi nhấn login
        String domain = mDomainView.getText().toString().trim();
        String username = mUserNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Kiểm tra đã nhập domain chưa
        if (TextUtils.isEmpty(domain)) {
            mDomainView.setError(getString(R.string.err_field_required));
            mDomainView.requestFocus();
            return false;
        }

        // Kiểm tra đã nhập user name chưa
        else if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.err_field_required));
            mUserNameView.requestFocus();
            return false;
        }

        // Kiểm tra đã nhập password chưa
        else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.login_error_invalid_password));
            mPasswordView.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Dựng base url từ domain do user nhập
     *
     * @param strDomain
     * @return
     */
    public String buildPOSBaseURL(String strDomain) {
        StringBuilder stringBuilder = new StringBuilder();
        String strFinalDomain = strDomain;
        if (strFinalDomain == null || strFinalDomain.trim().equals(StringUtil.STRING_EMPTY))
            strFinalDomain = BuildConfig.REST_BASE_URL;

        int lastIndexOfApp = strFinalDomain.lastIndexOf(StringUtil.STRING_SLASH);

        if (!strFinalDomain.startsWith(StringUtil.STRING_HTTP) && !strFinalDomain.startsWith(StringUtil.STRING_HTTPS)) {
            if (BuildConfig.REST_BASE_URL.startsWith(StringUtil.STRING_HTTPS))
                stringBuilder.append(StringUtil.STRING_HTTPS);
            else
                stringBuilder.append(StringUtil.STRING_HTTP);
            stringBuilder.append(strFinalDomain);
            if (lastIndexOfApp < 0)
                stringBuilder.append(StringUtil.STRING_SLASH).append(BuildConfig.REST_BASE_PAGE);
            if (lastIndexOfApp == strFinalDomain.length() - 1)
                stringBuilder.append(BuildConfig.REST_BASE_PAGE);
        } else {
            stringBuilder.append(strFinalDomain);
            if (lastIndexOfApp == strFinalDomain.indexOf("://") + 2)
                stringBuilder.append(StringUtil.STRING_SLASH).append(BuildConfig.REST_BASE_PAGE);
            if (lastIndexOfApp == strFinalDomain.length() - 1)
                stringBuilder.append(BuildConfig.REST_BASE_PAGE);
        }
        return stringBuilder.toString();
    }

    /**
     * Được gọi khi nút login được nhấn hoặc người dùng ấn enter lúc password
     */
    private void attemptLogin() {
        // Nếu đang có task thực thi thì bỏ qua việc login
        if (mAuthTask != null) {
            return;
        }

        // Khởi tạo lại các thông báo lỗi
        mDomainView.setError(null);
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Kiểm tra giá trị các control
        boolean valid = validControlValue();

        if (valid) {
            // Hiện progress bar
            showProgress(true);
            // check login không phải là demo
            mCheckLoginDemo = false;
            // lấy giá trị khi nhấn login
            String domain = mDomainView.getText().toString().trim();
            String username = mUserNameView.getText().toString().trim();
            String password = mPasswordView.getText().toString().trim();

            String strFinalDomain = buildPOSBaseURL(domain);
            // Bắt đầu login task
            mAuthTask = new LoginTask(new LoginListener(), strFinalDomain, username, password);
//            mAuthTask.execute();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            {
                mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else // Below Api Level 13
            {
                mAuthTask.execute();
            }
        }
    }

    private void attemptLoginDemo() {
        // check login là demo thì không lại thông tin khách hàng nhập
        mCheckLoginDemo = true;
        String domain = BuildConfig.REST_BASE_URL + "/pos-app/02";
        String username = "ravi";
        String password = "ravi123";

        String strFinalDomain = buildPOSBaseURL(domain);
        // Bắt đầu login task
        mAuthTask = new LoginTask(new LoginListener(), strFinalDomain, username, password);
//            mAuthTask.execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
        {
            mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else // Below Api Level 13
        {
            mAuthTask.execute();
        }
    }

    private void assigPos(String pos_id) {
        mAssignPosTask = new AssignPosTask(null, pos_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
        {
            mAssignPosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else // Below Api Level 13
        {
            mAssignPosTask.execute();
        }
    }

    /**
     * Hiển thị progress bar lúc login
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class LoginListener implements TaskListener<Void, Void, Boolean> {

        @Override
        public void onPreController(Task task) {
            showProgress(true);
        }

        @Override
        public void onPostController(Task task, Boolean success) {
            mAuthTask = null;
            if (success) {
                // check active key
                if (ConfigUtil.isCheckActiveKey()) {
                    // Đăng nhập thành công, lưu domain lại để lần sau không phải nhập
                    if (!mCheckLoginDemo) {
                        saveSharedValue("login_activity_domain", mDomainView.getText().toString().trim());
                        saveSharedValue("login_activity_username", mUserNameView.getText().toString().trim());
                        saveSharedValue("login_activity_password", mPasswordView.getText().toString().trim());
                    }
//                boolean isChooseStore = DataUtil.getDataBooleanToPreferences(getContext(), DataUtil.CHOOSE_STORE);
//                if (isChooseStore) {
//                    navigationToSalesActivity();
//                } else {
//                    mStoreTask = new ListStoreTask(new StoreListener());
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
//                    {
//                        mStoreTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    } else // Below Api Level 13
//                    {
//                        mStoreTask.execute();
//                    }
//                }

                    mStoreTask = new ListStoreTask(getContext(), new StoreListener());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
                    {
                        mStoreTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else // Below Api Level 13
                    {
                        mStoreTask.execute();
                    }
                } else {
                    DialogUtil.confirm(getContext(), getString(R.string.err_active_key), R.string.ok);
                    showProgress(false);
                }
            } else {
                // Đăng nhập không thành công, báo lỗi và yêu cầu nhập lại
//                mPasswordView.setError(getString(R.string.login_error_incorrect_password));
//                mPasswordView.requestFocus();
                DialogUtil.confirm(getContext(), getString(R.string.login_error_incorrect_password), R.string.ok);
                showProgress(false);
            }
        }

        @Override
        public void onCancelController(Task task, Exception exp) {
            mAuthTask = null;
            showProgress(false);
            if (exp != null) showErrorMsg(exp);
        }

        @Override
        public void onProgressController(Task task, Void... progress) {

        }
    }

    public class StoreListener implements TaskListener<Void, Void, List<PointOfSales>> {

        @Override
        public void onPreController(Task task) {

        }

        @Override
        public void onPostController(Task task, List<PointOfSales> listPos) {
            if (listPos != null) {
                if (ConfigUtil.isEnableSession()) {
                    mListPos = listPos;
                    sp_pos.bind(listPos.toArray(new PointOfSales[0]));
                    navigationToSalesActivity();
                } else {
                    email_login_form.setVisibility(View.GONE);
                    point_of_sales_form.setVisibility(View.VISIBLE);
                    if (listPos.size() > 0) {
                        sp_pos.setVisibility(View.VISIBLE);
                        error_pos.setVisibility(View.GONE);
                        mStartButton.setText(getContext().getString(R.string.start));
                        mListPos = listPos;
                        sp_pos.bind(listPos.toArray(new PointOfSales[0]));
                    } else {
                        error_pos.setVisibility(View.VISIBLE);
                        sp_pos.setVisibility(View.GONE);
                        mStartButton.setText(getContext().getString(R.string.logout));
                    }
                }
                // TODO: Check config session
//                navigationToSessionActivity();
                showProgress(false);
            } else {
                showProgress(false);
                showAlertRespone();
            }
        }

        @Override
        public void onCancelController(Task task, Exception exp) {
            showProgress(false);
            showAlertRespone();
        }

        @Override
        public void onProgressController(Task task, Void... progress) {

        }
    }

    private PointOfSales getPointOfSales(String posID) {
        for (PointOfSales pos : mListPos) {
            if (pos.getID().equals(posID)) {
                return pos;
            }
        }
        return null;
    }

    private void navigationToSalesActivity() {
        // Đăng nhập thành công, mở sẵn form sales
        Intent intent = new Intent(getContext(), SalesActivity.class);
        if (ConfigUtil.isEnableSession()) {
            intent.putExtra("redirect_register_shift", true);
        }
        startActivity(intent);
//        finish();
    }

    private void navigationToWelcomeActivity() {
        // Đăng nhập thành công, mở sẵn form sales
        Intent intent = new Intent(getContext(), WelcomeActivity.class);
        startActivity(intent);
//        finish();
    }

    private void navigationToSessionActivity() {
        // Đăng nhập thành công, mở sẵn form sales
        Intent intent = new Intent(getContext(), SessionActivity.class);
        startActivity(intent);
//        finish();
    }

    public void showAlertRespone() {
        String message = getContext().getString(R.string.err_request);

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }

    @Override
    protected void onResume() {
        super.onResume();
        email_login_form.setVisibility(View.VISIBLE);
        point_of_sales_form.setVisibility(View.GONE);
        showProgress(false);
    }
}

