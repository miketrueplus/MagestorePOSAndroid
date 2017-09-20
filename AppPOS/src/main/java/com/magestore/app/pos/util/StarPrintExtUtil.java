package com.magestore.app.pos.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.pos.R;
import com.magestore.app.pos.adapter.StarPrintAdapter;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;
import com.starmicronics.starioextension.StarIoExtManager;
import com.starmicronics.starioextension.StarIoExtManagerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Johan on 7/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StarPrintExtUtil {
    private static int printArea;
    private static String print_copy;
    static String mPortName;
    static String mPortSettings;
    static String mMacAddress;
    static String mModelName;
    static StarIoExt.Emulation mEmulation;
    static Boolean mDrawerOpenStatus;
    private static StarIoExtManager mStarIoExtManager;
    private static Activity mActivity;
    private static Order mOrder;
    private static Bitmap mBitmap = null;

    public static void showSearchPrint(final Context context, final Activity activity, final Bitmap bitmap, Order order) {
        mActivity = activity;
        mOrder = order;
        PrinterSetting setting = new PrinterSetting(context);
        mPortName = setting.getPortName();
        mPortSettings = setting.getPortSettings();
        mMacAddress = setting.getMacAddress();
        mModelName = setting.getModelName();
        mEmulation = setting.getEmulation();
        mDrawerOpenStatus = setting.getCashDrawerOpenActiveHigh();
        mBitmap = bitmap;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialogPrint = new Dialog(context);
                dialogPrint.setCancelable(true);
                dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogPrint.setFeatureDrawableAlpha(1, 1);
                dialogPrint.setContentView(R.layout.popup_star_print_search);
                ViewGroup.LayoutParams params = dialogPrint.getWindow().getAttributes();
                params.width = ConfigUtil.getStarPrintArea() + 300;
                dialogPrint.getWindow().setAttributes((WindowManager.LayoutParams) params);
                final EditText edt_port = (EditText) dialogPrint.findViewById(R.id.edt_port);
                edt_port.setHint(context.getString(R.string.print_hint_port_ext));
                edt_port.setEnabled(false);
                edt_port.setText(mPortName);
                Button bt_search = (Button) dialogPrint.findViewById(R.id.bt_search);
                Button bt_print = (Button) dialogPrint.findViewById(R.id.bt_print);
                Button bt_open_cash_drawer = (Button) dialogPrint.findViewById(R.id.bt_open_cash_drawer);
                final LinearLayout ll_device = (LinearLayout) dialogPrint.findViewById(R.id.ll_device);
                final LinearLayout ll_progessbar = (LinearLayout) dialogPrint.findViewById(R.id.ll_progessbar);
                LinearLayout ll_bluetooth = (LinearLayout) dialogPrint.findViewById(R.id.ll_bluetooth);
                LinearLayout ll_port = (LinearLayout) dialogPrint.findViewById(R.id.ll_port);
                ll_bluetooth.setVisibility(View.GONE);
                ll_port.setVisibility(View.GONE);

                final TextView tv_error_search = (TextView) dialogPrint.findViewById(R.id.tv_error_search);
                final Spinner sp_device = (Spinner) dialogPrint.findViewById(R.id.sp_device);

                Spinner spinner_tcp_port_number = (Spinner) dialogPrint.findViewById(R.id.spinner_tcp_port_number);
                spinner_tcp_port_number.setVisibility(View.GONE);
                SpinnerAdapter ad_tcp_port_number = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{"Standard", "9100", "9101", "9102", "9103", "9104", "9105", "9106", "9107", "9108", "9109"});
                spinner_tcp_port_number.setAdapter(ad_tcp_port_number);

                Spinner spinner_copy = (Spinner) dialogPrint.findViewById(R.id.spinner_copy);
                SpinnerAdapter ad_copy = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3"});
                spinner_copy.setAdapter(ad_copy);
                SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
                print_copy = pref.getString("copy", "1");
                if (print_copy.equals("1")) {
                    spinner_copy.setSelection(0);
                } else if (print_copy.equals("2")) {
                    spinner_copy.setSelection(1);
                } else {
                    spinner_copy.setSelection(2);
                }
                spinner_copy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            print_copy = "1";
                        } else if (i == 1) {
                            print_copy = "2";
                        } else if (i == 2) {
                            print_copy = "3";
                        }
                        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_WORLD_READABLE | context.MODE_WORLD_WRITEABLE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("copy", print_copy);
                        editor.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                Spinner spinner_bluetooth_communication_type = (Spinner) dialogPrint.findViewById(R.id.spinner_bluetooth_communication_type);
                SpinnerAdapter ad_bluetooth_communication_type = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{"SSP", "PIN Code"});
                spinner_bluetooth_communication_type.setAdapter(ad_bluetooth_communication_type);
                final String item_list[] = new String[]{
                        context.getResources().getString(R.string.printArea2inch),
                        context.getResources().getString(R.string.printArea3inch),
                        context.getResources().getString(R.string.printArea4inch),
                };
                Spinner spinner_print_area = (Spinner) dialogPrint.findViewById(R.id.spinner_print_area);
                spinner_print_area.setVisibility(View.GONE);
                SpinnerAdapter ad_print_area = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{context.getResources().getString(R.string.printArea2inch), context.getResources().getString(R.string.printArea3inch), context.getResources().getString(R.string.printArea4inch)});
                spinner_print_area.setAdapter(ad_print_area);
                printArea = ConfigUtil.getStarPrintArea();
                spinner_print_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            printArea = 384;
                        } else if (i == 1) {
                            printArea = 576;
                        } else {
                            printArea = 832;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                final Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        ll_progessbar.setVisibility(View.GONE);
                        if (arrayDiscovery.size() > 0) {
                            tv_error_search.setVisibility(View.GONE);
                            StarPrintAdapter mAdapter = new StarPrintAdapter(context, arrayDiscovery);
                            ll_device.setVisibility(View.VISIBLE);
                            sp_device.setAdapter(mAdapter);
                            PortInfo portFirst = arrayDiscovery.get(0);
                            if (portFirst.getPortName().startsWith(PrinterSetting.IF_TYPE_BLUETOOTH)) {
                                mModelName = portFirst.getPortName().substring(PrinterSetting.IF_TYPE_BLUETOOTH.length());
                                mPortName = PrinterSetting.IF_TYPE_BLUETOOTH + portFirst.getMacAddress();
                                mMacAddress = portFirst.getMacAddress();
                            } else {
                                mModelName = portFirst.getModelName();
                                mPortName = portFirst.getPortName();
                                mMacAddress = portFirst.getMacAddress();
                            }

                            int model = ModelCapability.getModel(mModelName);
                            mPortSettings = ModelCapability.getPortSettings(model);
                            mEmulation = ModelCapability.getEmulation(model);
                            mDrawerOpenStatus = true;
                            registerPrinter(context);
                            edt_port.setText(portFirst.getPortName());

                            sp_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    edt_port.setText(arrayDiscovery.get(i).getPortName());
                                    PortInfo portFirst = arrayDiscovery.get(i);
                                    if (portFirst.getPortName().startsWith(PrinterSetting.IF_TYPE_BLUETOOTH)) {
                                        mModelName = portFirst.getPortName().substring(PrinterSetting.IF_TYPE_BLUETOOTH.length());
                                        mPortName = PrinterSetting.IF_TYPE_BLUETOOTH + portFirst.getMacAddress();
                                        mMacAddress = portFirst.getMacAddress();
                                    } else {
                                        mModelName = portFirst.getModelName();
                                        mPortName = portFirst.getPortName();
                                        mMacAddress = portFirst.getMacAddress();
                                    }

                                    int model = ModelCapability.getModel(mModelName);
                                    mPortSettings = ModelCapability.getPortSettings(model);
                                    mEmulation = ModelCapability.getEmulation(model);
                                    mDrawerOpenStatus = true;
                                    registerPrinter(context);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        } else {
                            tv_error_search.setVisibility(View.VISIBLE);
                        }
                    }
                };

                bt_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_error_search.setVisibility(View.GONE);
                        ll_progessbar.setVisibility(View.VISIBLE);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getPortDiscovery(context, "All", mHandler);
                            }
                        });
                        thread.start();
                    }
                });

                bt_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PrinterSetting setting = new PrinterSetting(context);
                        mStarIoExtManager = new StarIoExtManager(StarIoExtManager.Type.Standard, mPortName, mPortSettings, 10000, context);     // 10000mS!!!
                        mStarIoExtManager.setCashDrawerOpenActiveHigh(setting.getCashDrawerOpenActiveHigh());
                        PrintTask printTask = new PrintTask();
                        printTask.execute();
                        dialogPrint.dismiss();
                    }
                });

                bt_open_cash_drawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PrinterSetting setting = new PrinterSetting(context);
                        mStarIoExtManager = new StarIoExtManager(StarIoExtManager.Type.Standard, mPortName, mPortSettings, 10000, context);     // 10000mS!!!
                        mStarIoExtManager.setCashDrawerOpenActiveHigh(setting.getCashDrawerOpenActiveHigh());
                        OpenCashTask openCashTask = new OpenCashTask();
                        openCashTask.execute();
                    }
                });
                dialogPrint.show();
            }
        });
    }

    public static void autoPrint(final Context context, final Activity activity, Order order) {
        mActivity = activity;
        mOrder = order;
        PrinterSetting setting = new PrinterSetting(context);
        mPortName = setting.getPortName();
        mPortSettings = setting.getPortSettings();
        mMacAddress = setting.getMacAddress();
        mModelName = setting.getModelName();
        mEmulation = setting.getEmulation();
        mDrawerOpenStatus = setting.getCashDrawerOpenActiveHigh();
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        print_copy = pref.getString("copy", "1");
        printArea = ConfigUtil.getStarPrintArea();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (!StringUtil.isNullOrEmpty(mPortName)) {
            mStarIoExtManager = new StarIoExtManager(StarIoExtManager.Type.Standard, mPortName, mPortSettings, 10000, context);     // 10000mS!!!
            mStarIoExtManager.setCashDrawerOpenActiveHigh(setting.getCashDrawerOpenActiveHigh());
            PrintTask printTask = new PrintTask();
            printTask.execute();
        } else {
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (arrayDiscovery.size() > 0) {
                        PortInfo portFirst = arrayDiscovery.get(0);
                        if (portFirst.getPortName().startsWith(PrinterSetting.IF_TYPE_BLUETOOTH)) {
                            mModelName = portFirst.getPortName().substring(PrinterSetting.IF_TYPE_BLUETOOTH.length());
                            mPortName = PrinterSetting.IF_TYPE_BLUETOOTH + portFirst.getMacAddress();
                            mMacAddress = portFirst.getMacAddress();
                        } else {
                            mModelName = portFirst.getModelName();
                            mPortName = portFirst.getPortName();
                            mMacAddress = portFirst.getMacAddress();
                        }

                        int model = ModelCapability.getModel(mModelName);
                        mPortSettings = ModelCapability.getPortSettings(model);
                        mEmulation = ModelCapability.getEmulation(model);
                        mDrawerOpenStatus = true;
                        registerPrinter(context);

                        mStarIoExtManager = new StarIoExtManager(StarIoExtManager.Type.Standard, mPortName, mPortSettings, 10000, context);     // 10000mS!!!
                        mStarIoExtManager.setCashDrawerOpenActiveHigh(mDrawerOpenStatus);
                        PrintTask printTask = new PrintTask();
                        printTask.execute();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        dialog.setNegativeButton(mActivity.getString(R.string.ok), null);
                        AlertDialog alert = dialog.create();
                        alert.setMessage(context.getString(R.string.print_error_search));
                        alert.setCancelable(false);
                        alert.show();
                    }
                }
            };

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getPortDiscovery(context, "All", mHandler);
                }
            });
            thread.start();
        }
    }

    private static void registerPrinter(Context context) {
        PrinterSetting setting = new PrinterSetting(context);
        setting.write(mModelName, mPortName, mMacAddress, mPortSettings, mEmulation, mDrawerOpenStatus);
    }

    private static class PrintTask extends AsyncTask<Void, Void, Communication.Result> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Communication.Result doInBackground(Void... params) {
            byte[] data;

            PrinterSetting setting = new PrinterSetting(mActivity);
            StarIoExt.Emulation emulation = setting.getEmulation();

            ILocalizeReceipts localizeReceipts = ILocalizeReceipts.createLocalizeReceipts(0, printArea, mOrder, mActivity, mBitmap);

            data = PrinterFunctions.createTextReceiptData(emulation, localizeReceipts, true, print_copy);

            Communication.sendCommands(mStarIoExtManager, data, mPortName, mPortSettings, 10000, mActivity, mCallback);     // 10000mS!!!

            return null;
        }

        private Communication.SendCallback mCallback = new Communication.SendCallback() {
            @Override
            public void onStatus(boolean result, Communication.Result communicateResult) {
                String msg;

                switch (communicateResult) {
                    case Success:
                        msg = "1";
                        break;
                    case ErrorOpenPort:
                        msg = mActivity.getString(R.string.print_error_open_port);
                        break;
                    case ErrorBeginCheckedBlock:
                        msg = mActivity.getString(R.string.print_error_begin);
                        break;
                    case ErrorEndCheckedBlock:
                        msg = mActivity.getString(R.string.print_error_end);
                        break;
                    case ErrorReadPort:
                        msg = mActivity.getString(R.string.print_error_read);
                        break;
                    case ErrorWritePort:
                        msg = mActivity.getString(R.string.print_error_write);
                        break;
                    default:
                        msg = "Unknown error";
                        break;
                }
                if (!msg.equals("1")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                    dialog.setNegativeButton(mActivity.getString(R.string.ok), null);
                    AlertDialog alert = dialog.create();
                    alert.setMessage(msg);
                    alert.setCancelable(false);
                    alert.show();
                }
            }
        };
    }

    private static class OpenCashTask extends AsyncTask<Void, Void, Communication.Result> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Communication.Result doInBackground(Void... params) {
            byte[] data;

            PrinterSetting setting = new PrinterSetting(mActivity);
            StarIoExt.Emulation emulation = setting.getEmulation();

            data = PrinterFunctions.openCash(emulation);

            Communication.sendCommands(mStarIoExtManager, data, mPortName, mPortSettings, 10000, mActivity, mCallback);     // 10000mS!!!

            return null;
        }

        private Communication.SendCallback mCallback = new Communication.SendCallback() {
            @Override
            public void onStatus(boolean result, Communication.Result communicateResult) {
                String msg;

                switch (communicateResult) {
                    case Success:
                        msg = "1";
                        break;
                    case ErrorOpenPort:
                        msg = mActivity.getString(R.string.print_error_open_port);
                        break;
                    case ErrorBeginCheckedBlock:
                        msg = mActivity.getString(R.string.print_error_begin);
                        break;
                    case ErrorEndCheckedBlock:
                        msg = mActivity.getString(R.string.print_error_end);
                        break;
                    case ErrorReadPort:
                        msg = mActivity.getString(R.string.print_error_read);
                        break;
                    case ErrorWritePort:
                        msg = mActivity.getString(R.string.print_error_write);
                        break;
                    default:
                        msg = "Unknown error";
                        break;
                }
                if (!msg.equals("1")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                    dialog.setNegativeButton(mActivity.getString(R.string.ok), null);
                    AlertDialog alert = dialog.create();
                    alert.setMessage(msg);
                    alert.setCancelable(false);
                    alert.show();
                }
            }
        };
    }

    static ArrayList<PortInfo> arrayDiscovery;

    private static void getPortDiscovery(final Context context, String interfaceName, Handler mHandler) {
        List<PortInfo> BTPortList;
        List<PortInfo> TCPPortList;
        List<PortInfo> USBPortList;

        arrayDiscovery = new ArrayList<PortInfo>();

        try {
            if (true == interfaceName.equals("Bluetooth") || true == interfaceName.equals("All")) {
                BTPortList = StarIOPort.searchPrinter("BT:");

                for (PortInfo portInfo : BTPortList) {
                    arrayDiscovery.add(portInfo);
                }
            }
            if (true == interfaceName.equals("LAN") || true == interfaceName.equals("All")) {
                TCPPortList = StarIOPort.searchPrinter("TCP:");

                for (PortInfo portInfo : TCPPortList) {
                    arrayDiscovery.add(portInfo);
                }
            }
            if (true == interfaceName.equals("USB") || true == interfaceName.equals("All")) {
                USBPortList = StarIOPort.searchPrinter("USB:");

                for (PortInfo portInfo : USBPortList) {
                    arrayDiscovery.add(portInfo);
                }
            }
        } catch (StarIOPortException e) {
            e.printStackTrace();
        }

        mHandler.sendEmptyMessage(0);
    }

    private static String getPortSettingsOption(String portName, Dialog dialogPrint) {
        String portSettings = "";

        if (portName.toUpperCase().startsWith("TCP:")) {
            portSettings += getTCPPortSettings(dialogPrint);
        } else if (portName.toUpperCase().startsWith("BT:")) {
            portSettings += getBluetoothCommunicationType(dialogPrint);    // Bluetooth option of "portSettings" must be last.
        }

        return portSettings;
    }

    private static String getTCPPortSettings(Dialog dialogPrint) {
        String portSettings = "";

        Spinner spinner_tcp_port_number = (Spinner) dialogPrint.findViewById(R.id.spinner_tcp_port_number);
        switch (spinner_tcp_port_number.getSelectedItemPosition()) {
            case 0:
                portSettings = "";
                break;
            case 1:
                portSettings = "9100";
                break;
            case 2:
                portSettings = "9101";
                break;
            case 3:
                portSettings = "9102";
                break;
            case 4:
                portSettings = "9103";
                break;
            case 5:
                portSettings = "9104";
                break;
            case 6:
                portSettings = "9105";
                break;
            case 7:
                portSettings = "9106";
                break;
            case 8:
                portSettings = "9107";
                break;
            case 9:
                portSettings = "9108";
                break;
            case 10:
                portSettings = "9109";
                break;
        }

        return portSettings;
    }

    private static String getBluetoothCommunicationType(Dialog dialogPrint) {
        String portSettings = "";

        Spinner spinner_bluetooth_communication_type = (Spinner) dialogPrint.findViewById(R.id.spinner_bluetooth_communication_type);
        switch (spinner_bluetooth_communication_type.getSelectedItemPosition()) {
            case 0:
                portSettings = "";
                break;
            case 1:
                portSettings = ";p";
                break;
        }

        return portSettings;
    }
}
