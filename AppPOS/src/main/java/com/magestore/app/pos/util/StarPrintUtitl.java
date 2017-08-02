package com.magestore.app.pos.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.pos.R;
import com.magestore.app.pos.adapter.StarPrintAdapter;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Johan on 7/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StarPrintUtitl {
    private static int printArea;

    public static void showSearchPrint(final Context context, final Activity activity, final Bitmap bitmap) {
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
                params.width = ConfigUtil.getStarPrintArea();
                dialogPrint.getWindow().setAttributes((WindowManager.LayoutParams) params);
                final EditText edt_port = (EditText) dialogPrint.findViewById(R.id.edt_port);
                SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
                edt_port.setText(pref.getString("portName", ""));
                Button bt_search = (Button) dialogPrint.findViewById(R.id.bt_search);
                Button bt_print = (Button) dialogPrint.findViewById(R.id.bt_print);
                Button bt_open_cash_drawer = (Button) dialogPrint.findViewById(R.id.bt_open_cash_drawer);
                final LinearLayout ll_device = (LinearLayout) dialogPrint.findViewById(R.id.ll_device);
                final LinearLayout ll_progessbar = (LinearLayout) dialogPrint.findViewById(R.id.ll_progessbar);
                final TextView tv_error_search = (TextView) dialogPrint.findViewById(R.id.tv_error_search);
                final Spinner sp_device = (Spinner) dialogPrint.findViewById(R.id.sp_device);

                Spinner spinner_tcp_port_number = (Spinner) dialogPrint.findViewById(R.id.spinner_tcp_port_number);
                SpinnerAdapter ad_tcp_port_number = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{"Standard", "9100", "9101", "9102", "9103", "9104", "9105", "9106", "9107", "9108", "9109"});
                spinner_tcp_port_number.setAdapter(ad_tcp_port_number);

                Spinner spinner_bluetooth_communication_type = (Spinner) dialogPrint.findViewById(R.id.spinner_bluetooth_communication_type);
                SpinnerAdapter ad_bluetooth_communication_type = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{"SSP", "PIN Code"});
                spinner_bluetooth_communication_type.setAdapter(ad_bluetooth_communication_type);
                final String item_list[] = new String[]{
                        context.getResources().getString(R.string.printArea3inch),
                        context.getResources().getString(R.string.printArea4inch),
                };
                Spinner spinner_print_area = (Spinner) dialogPrint.findViewById(R.id.spinner_print_area);
                spinner_print_area.setVisibility(View.GONE);
                SpinnerAdapter ad_print_area = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{context.getResources().getString(R.string.printArea3inch), context.getResources().getString(R.string.printArea4inch)});
                spinner_print_area.setAdapter(ad_print_area);
                printArea = ConfigUtil.getStarPrintArea();
                spinner_print_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
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
                            edt_port.setText(arrayDiscovery.get(0).getPortName());
                            SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_WORLD_READABLE | context.MODE_WORLD_WRITEABLE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("portName", edt_port.getText().toString());
                            editor.commit();

                            sp_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    edt_port.setText(arrayDiscovery.get(i).getPortName());
                                    SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_WORLD_READABLE | context.MODE_WORLD_WRITEABLE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("portName", edt_port.getText().toString());
                                    editor.commit();
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
                        ll_progessbar.setVisibility(View.VISIBLE);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getPortDiscovery(context, "All", edt_port, sp_device, ll_device, mHandler);
                            }
                        });
                        thread.start();
                    }
                });

                bt_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String port_name = edt_port.getText().toString();
//                        PrintReceipt(context, port_name, getPortSettingsOption(port_name, dialogPrint), "Line", printArea, order);
                        PrintBitmap(context, port_name, getPortSettingsOption(port_name, dialogPrint), bitmap, printArea, false);
                        dialogPrint.dismiss();
                    }
                });

                bt_open_cash_drawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String port_name = edt_port.getText().toString();
                        OpenCashDrawer(context, port_name, getPortSettingsOption(port_name, dialogPrint));
                    }
                });
                dialogPrint.show();
            }
        });
    }

    public static void PrintBitmap(Context context, String portName, String portSettings, Bitmap source, int maxWidth, boolean compressionEnable) {
        ArrayList<Byte> commands = new ArrayList<Byte>();
        Byte[] tempList;

        RasterDocument rasterDoc = new RasterDocument(RasterDocument.RasSpeed.Medium, RasterDocument.RasPageEndMode.FeedAndFullCut, RasterDocument.RasPageEndMode.FeedAndFullCut, RasterDocument.RasTopMargin.Standard, 0, 0, 0);
        StarBitmap starbitmap = new StarBitmap(source, false, maxWidth);

        byte[] command = rasterDoc.BeginDocumentCommandData();
        tempList = new Byte[command.length];
        CopyArray(command, tempList);
        commands.addAll(Arrays.asList(tempList));

        command = starbitmap.getImageRasterDataForPrinting(compressionEnable);
        tempList = new Byte[command.length];
        CopyArray(command, tempList);
        commands.addAll(Arrays.asList(tempList));

        command = rasterDoc.EndDocumentCommandData();
        tempList = new Byte[command.length];
        CopyArray(command, tempList);
        commands.addAll(Arrays.asList(tempList));

        commands.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

        sendCommand(context, portName, portSettings, commands);
    }

    public static void OpenCashDrawer(Context context, String portName, String portSettings) {
        ArrayList<Byte> commands = new ArrayList<Byte>();
        byte openCashDrawer = 0x07;

        commands.add(openCashDrawer);

        sendCommand(context, portName, portSettings, commands);
    }

    static ArrayList<PortInfo> arrayDiscovery;

    private static void getPortDiscovery(final Context context, String interfaceName, final EditText edt_port, Spinner sp_device, LinearLayout ll_device, Handler mHandler) {
        List<PortInfo> BTPortList;
        List<PortInfo> TCPPortList;

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

    private static void PrintReceipt(Context context, String portName, String portSettings, String commandType, String strPrintArea, Order order) {
        if (commandType == "Line") {
            if (strPrintArea.equals("3inch (78mm)")) {
                byte[] data;
                ArrayList<Byte> list = new ArrayList<Byte>();

                Byte[] tempList;

                list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x01}));    // Alignment (center)

                // invoice header
                // Character expansion
                list.addAll(Arrays.asList(new Byte[]{0x06, 0x09, 0x1b, 0x69, 0x01, 0x01}));
                String title_invoice = "\n" + context.getString(R.string.order_detail_bottom_btn_invoice).toUpperCase() + "\r\n";
                data = title_invoice.getBytes();
                tempList = new Byte[data.length];
                CopyArray(data, tempList);
                list.addAll(Arrays.asList(tempList));

                data = "*** ***\r\n\r\n".getBytes();
                tempList = new Byte[data.length];
                CopyArray(data, tempList);
                list.addAll(Arrays.asList(tempList));
                list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x00, 0x00}));  //Cancel Character Expansion
                // order_id header
                String order_id = "#" + order.getIncrementId() + "\r\n";
                data = order_id.getBytes();
                tempList = new Byte[data.length];
                CopyArray(data, tempList);
                list.addAll(Arrays.asList(tempList));
                // date time header
                String date_time = ConfigUtil.formatDateTime(order.getCreatedAt()) + "\r\n";
                data = date_time.getBytes();
                tempList = new Byte[data.length];
                CopyArray(data, tempList);
                list.addAll(Arrays.asList(tempList));

                // cashier name
                if (ConfigUtil.getConfigPrint().getShowCashierName().equals("1") && !StringUtil.isNullOrEmpty(order.getWebposStaffName())) {
                    String cashier_name = context.getString(R.string.print_header_cashier) + ": " + order.getWebposStaffName() + "\r\n";
                    list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45})); // bold
                    data = cashier_name.getBytes();
                    tempList = new Byte[data.length];
                    CopyArray(data, tempList);
                    list.addAll(Arrays.asList(tempList));
                    list.addAll(Arrays.asList(new Byte[]{0x1b, 0x46})); // bolf off
                }

                // customer
                if (order.getBillingAddress() != null && !StringUtil.isNullOrEmpty(order.getBillingAddress().getName())) {
                    if (!order.getBillingAddress().getName().equals(ConfigUtil.getCustomerGuest().getName())) {
                        String customer_name = context.getString(R.string.print_header_customer) + ": " + order.getBillingAddress().getName() + "\r\n";
                        list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45})); // bold
                        data = customer_name.getBytes();
                        tempList = new Byte[data.length];
                        CopyArray(data, tempList);
                        list.addAll(Arrays.asList(tempList));
                        list.addAll(Arrays.asList(new Byte[]{0x1b, 0x46})); // bolf off
                    }
                }

                list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x00})); // Alignment
                list.addAll(Arrays.asList(new Byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00})); //Set horizontal tab

//                // title item content
//                String item_title = context.getString(R.string.items).toUpperCase() + " ";
//                data = item_title.getBytes();
//                tempList = new Byte[data.length];
//                CopyArray(data, tempList);
//                list.addAll(Arrays.asList(tempList));
//
//                list.addAll(Arrays.asList(new Byte[]{0x12}));
//
//                String title_all = "               " + context.getString(R.string.order_detail_content_item_qty).toUpperCase() + "   " + context.getString(R.string.price).toUpperCase() + "\u0009" + context.getString(R.string.order_detail_content_item_subtotal).toUpperCase() + "\r\n";
//                data = title_all.getBytes();
//                tempList = new Byte[data.length];
//                CopyArray(data, tempList);
//                list.addAll(Arrays.asList(tempList));
//
//                // items content
//                List<CartItem> items = order.getOrderItems();
//                if (items != null && items.size() > 0) {
//                    for (CartItem item : items) {
//                        if (item.getOrderParentItem() == null) {
//                            String item_data = item.getName() + "\u0009" + item.getQtyOrdered() + "\u0009" + ConfigUtil.convertToPrice(item.getBasePrice()) + "\u0009" + ConfigUtil.convertToPrice(item.getBaseSubTotal()) + "\r\n";
//                            data = item_data.getBytes();
//                            tempList = new Byte[data.length];
//                            CopyArray(data, tempList);
//                            list.addAll(Arrays.asList(tempList));
//                        }
//                    }
//                }

                // subtotal
                String sutotal = context.getString(R.string.order_detail_bottom_tb_subtotal);
                data = sutotal.getBytes();
                tempList = new Byte[data.length];
                CopyArray(data, tempList);
                list.addAll(Arrays.asList(tempList));
                list.addAll(Arrays.asList(new Byte[]{' ', 0x09, ' '}));   //Moving Horizontal Tab

                String sutotal_price = ConfigUtil.convertToPrice(order.getBaseSubtotalInclTax()) + "\r\n------------------------------------------------\r\n\r\n";
                data = sutotal_price.getBytes();
                tempList = new Byte[data.length];
                CopyArray(data, tempList);
                list.addAll(Arrays.asList(tempList));

                list.addAll(Arrays.asList(new Byte[]{0x09}));

                list.addAll(Arrays.asList(new Byte[]{0x1b, 0x64, 0x02})); // Cut
                list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer
                sendCommand(context, portName, portSettings, list);
            }
        }
    }

    private static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
        for (int index = 0; index < cpyArray.length; index++) {
            cpyArray[index] = srcArray[index];
        }
    }

    private static byte[] convertFromListByteArrayTobyteArray(List<Byte> ByteArray) {
        byte[] byteArray = new byte[ByteArray.size()];
        for (int index = 0; index < byteArray.length; index++) {
            byteArray[index] = ByteArray.get(index);
        }

        return byteArray;
    }

    private static void sendCommand(Context context, String portName, String portSettings, ArrayList<Byte> byteList) {
        StarIOPort port = null;
        try {
            /*
                using StarIOPort3.1.jar (support USB Port)
				Android OS Version: upper 2.2
			*/
            port = StarIOPort.getPort(portName, portSettings, 10000);
            /*
                using StarIOPort.jar
				Android OS Version: under 2.1
				port = StarIOPort.getPort(portName, portSettings, 10000);
			*/
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

			/*
			   Using Begin / End Checked Block method
               When sending large amounts of raster data,
               adjust the value in the timeout in the "StarIOPort.getPort"
               in order to prevent "timeout" of the "endCheckedBlock method" while a printing.

               *If receipt print is success but timeout error occurs(Show message which is "There was no response of the printer within the timeout period."),
                 need to change value of timeout more longer in "StarIOPort.getPort" method. (e.g.) 10000 -> 30000
			 */
            StarPrinterStatus status = port.beginCheckedBlock();

            if (true == status.offline) {
                throw new StarIOPortException("A printer is offline");
            }

            byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
            port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

            port.setEndCheckedBlockTimeoutMillis(30000);//Change the timeout time of endCheckedBlock method.
            status = port.endCheckedBlock();

            if (true == status.coverOpen) {
                throw new StarIOPortException("Printer cover is open");
            } else if (true == status.receiptPaperEmpty) {
                throw new StarIOPortException("Receipt paper is empty");
            } else if (true == status.offline) {
                throw new StarIOPortException("Printer is offline");
            }
        } catch (StarIOPortException e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setNegativeButton("Ok", null);
            AlertDialog alert = dialog.create();
            alert.setTitle("Failure");
            alert.setMessage(e.getMessage());
            alert.setCancelable(false);
            alert.show();
        } finally {
            if (port != null) {
                try {
                    StarIOPort.releasePort(port);
                } catch (StarIOPortException e) {
                }
            }
        }
    }
}
