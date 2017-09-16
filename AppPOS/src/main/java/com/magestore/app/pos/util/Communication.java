package com.magestore.app.pos.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

@SuppressWarnings({"UnusedParameters", "UnusedAssignment", "WeakerAccess"})
public class Communication {
    @SuppressWarnings("unused")
    public enum Result {
        Success,
        ErrorUnknown,
        ErrorOpenPort,
        ErrorBeginCheckedBlock,
        ErrorEndCheckedBlock,
        ErrorWritePort,
        ErrorReadPort,
    }

    interface StatusCallback {
        void onStatus(StarPrinterStatus result);
    }

    interface SendCallback {
        void onStatus(boolean result, Communication.Result communicateResult);
    }


    public static void sendCommands(Object lock, byte[] commands, StarIOPort port, SendCallback callback) {
        SendCommandThread thread = new SendCommandThread(lock, commands, port, callback);
        thread.start();
    }

    public static void sendCommandsDoNotCheckCondition(Object lock, byte[] commands, StarIOPort port, SendCallback callback) {
        SendCommandDoNotCheckConditionThread thread = new SendCommandDoNotCheckConditionThread(lock, commands, port, callback);
        thread.start();
    }

    public static void sendCommands(Object lock, byte[] commands, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        SendCommandThread thread = new SendCommandThread(lock, commands, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void sendCommandsDoNotCheckCondition(Object lock, byte[] commands, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        SendCommandDoNotCheckConditionThread thread = new SendCommandDoNotCheckConditionThread(lock, commands, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void retrieveStatus(Object lock, String portName, String portSettings, int timeout, Context context, StatusCallback callback) {
        RetrieveStatusThread thread = new RetrieveStatusThread(lock, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void sendFunctionDoNotChecked(Object lock, IStarIoExtFunction function, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        SendFunctionDoNotCheckedThread thread = new SendFunctionDoNotCheckedThread(lock, function, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void sendFunctionDoNotChecked(Object lock, IStarIoExtFunction function, StarIOPort port, SendCallback callback) {
        SendFunctionDoNotCheckedThread thread = new SendFunctionDoNotCheckedThread(lock, function, port, callback);
        thread.start();
    }

}

class SendCommandThread extends Thread {
    private final Object mLock;
    private Communication.SendCallback mCallback;
    private byte[] mCommands;

    private StarIOPort mPort;

    private String mPortName = null;
    private String mPortSettings;
    private int     mTimeout;
    private Context mContext;

    SendCommandThread(Object lock, byte[] commands, StarIOPort port, Communication.SendCallback callback) {
        mCommands = commands;
        mPort     = port;
        mCallback = callback;
        mLock     = lock;
    }

    SendCommandThread(Object lock, byte[] commands, String portName, String portSettings, int timeout, Context context, Communication.SendCallback callback) {
        mCommands     = commands;
        mPortName     = portName;
        mPortSettings = portSettings;
        mTimeout      = timeout;
        mContext      = context;
        mCallback     = callback;
        mLock         = lock;
    }

    @Override
    public void run() {
        Communication.Result communicateResult = Communication.Result.ErrorOpenPort;
        boolean result = false;

        synchronized (mLock) {
            try {
                if (mPort == null) {

                    if (mPortName == null) {
                        resultSendCallback(false, communicateResult, mCallback);
                        return;
                    } else {
                        mPort = StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext);
                    }
                }
                if (mPort == null) {
                    communicateResult = Communication.Result.ErrorOpenPort;
                    resultSendCallback(false, communicateResult, mCallback);
                    return;
                }

//          // When using USB interface with mPOP(F/W Ver 1.0.1), you need to send the following data.
//          byte[] dummy = {0x00};
//          port.writePort(dummy, 0, dummy.length);

                StarPrinterStatus status;

                communicateResult = Communication.Result.ErrorBeginCheckedBlock;

                status = mPort.beginCheckedBlock();

                if (status.offline) {
                    throw new StarIOPortException("A printer is offline");
                }

                communicateResult = Communication.Result.ErrorWritePort;

                mPort.writePort(mCommands, 0, mCommands.length);

                communicateResult = Communication.Result.ErrorEndCheckedBlock;

                mPort.setEndCheckedBlockTimeoutMillis(30000);     // 30000mS!!!

                status = mPort.endCheckedBlock();

                if (status.coverOpen) {
                    throw new StarIOPortException("Printer cover is open");
                } else if (status.receiptPaperEmpty) {
                    throw new StarIOPortException("Receipt paper is empty");
                } else if (status.offline) {
                    throw new StarIOPortException("Printer is offline");
                }

                result = true;
                communicateResult = Communication.Result.Success;
            } catch (StarIOPortException e) {
                // Nothing
            }

            if (mPort != null && mPortName != null) {
                try {
                    StarIOPort.releasePort(mPort);
                } catch (StarIOPortException e) {
                    // Nothing
                }
                mPort = null;
            }

            resultSendCallback(result, communicateResult, mCallback);
        }
    }

    private static void resultSendCallback(final boolean result, final Communication.Result communicateResult, final Communication.SendCallback callback) {
        if (callback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onStatus(result, communicateResult);
                }
            });
        }
    }
}

class SendCommandDoNotCheckConditionThread extends Thread {
    private final Object mLock;
    private Communication.SendCallback mCallback;
    private byte[] mCommands;

    private StarIOPort mPort;

    private String mPortName = null;
    private String mPortSettings;
    private int     mTimeout;
    private Context mContext;

    SendCommandDoNotCheckConditionThread(Object lock, byte[] commands, StarIOPort port, Communication.SendCallback callback) {
        mLock     = lock;
        mCommands = commands;
        mPort     = port;
        mCallback = callback;
    }

    SendCommandDoNotCheckConditionThread(Object lock, byte[] commands, String portName, String portSettings, int timeout, Context context, Communication.SendCallback callback) {
        mLock         = lock;
        mCommands     = commands;
        mPortName     = portName;
        mPortSettings = portSettings;
        mTimeout      = timeout;
        mContext      = context;
        mCallback     = callback;
    }

    @Override
    public void run() {
        Communication.Result communicateResult = Communication.Result.ErrorOpenPort;
        boolean result = false;

        synchronized (mLock) {
            try {
                if (mPort == null) {

                    if (mPortName == null) {
                        resultSendCallback(false, communicateResult, mCallback);
                        return;
                    } else {
                        mPort = StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext);
                    }
                }
                if (mPort == null) {
                    communicateResult = Communication.Result.ErrorOpenPort;
                    resultSendCallback(false, communicateResult, mCallback);
                    return;
                }

//          // When using USB interface with mPOP(F/W Ver 1.0.1), you need to send the following data.
//          byte[] dummy = {0x00};
//          port.writePort(dummy, 0, dummy.length);

                StarPrinterStatus status;

                communicateResult = Communication.Result.ErrorWritePort;

                status = mPort.retreiveStatus();

                if (status.rawLength == 0) {
                    throw new StarIOPortException("A printer is offline");
                }

                communicateResult = Communication.Result.ErrorWritePort;

                mPort.writePort(mCommands, 0, mCommands.length);

//                if (status.coverOpen) {
//                    throw new StarIOPortException("Printer cover is open");
//                } else if (status.receiptPaperEmpty) {
//                    throw new StarIOPortException("Receipt paper is empty");
//                } else if (status.offline) {
//                    throw new StarIOPortException("Printer is offline");
//                }

                communicateResult = Communication.Result.Success;
                result = true;
            } catch (StarIOPortException e) {
                // Nothing
            }

            if (mPort != null && mPortName != null) {
                try {
                    StarIOPort.releasePort(mPort);
                } catch (StarIOPortException e) {
                    // Nothing
                }
                mPort = null;
            }

            resultSendCallback(result, communicateResult, mCallback);
        }
    }

    private static void resultSendCallback(final boolean result, final Communication.Result communicateResult, final Communication.SendCallback callback) {
        if (callback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onStatus(result, communicateResult);
                }
            });
        }
    }
}

class SendFunctionDoNotCheckedThread extends Thread {
    private final Object mLock;
    private IStarIoExtFunction mFunction;
    private StarIOPort mPort;
    private Communication.SendCallback mCallback;

    private String mPortName = null;
    private String mPortSettings;
    private int     mTimeout;
    private Context mContext;

    SendFunctionDoNotCheckedThread(Object lock, IStarIoExtFunction function, StarIOPort port, Communication.SendCallback callback) {
        mLock     = lock;
        mFunction = function;
        mPort     = port;
        mCallback = callback;
    }

    SendFunctionDoNotCheckedThread(Object lock, IStarIoExtFunction function, String portName, String portSettings, int timeout, Context context, Communication.SendCallback callback) {
        mLock         = lock;
        mFunction     = function;
        mPortName     = portName;
        mPortSettings = portSettings;
        mTimeout      = timeout;
        mContext      = context;
        mCallback     = callback;
    }

    @Override
    public void run() {
        Communication.Result communicateResult = Communication.Result.ErrorOpenPort;
        boolean result = false;

        synchronized (mLock) {
            try {
                if (mPort == null) {

                    if (mPortName == null) {
                        resultSendCallback(false, communicateResult, mCallback);
                        return;
                    } else {
                        mPort = StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext);
                    }
                }

//          // When using USB interface with mPOP(F/W Ver 1.0.1), you need to send the following data.
//          byte[] dummy = {0x00};
//          port.writePort(dummy, 0, dummy.length);

                StarPrinterStatus status;

                communicateResult = Communication.Result.ErrorWritePort;
                status = mPort.retreiveStatus();

                if (status.rawLength == 0) {
                    throw new StarIOPortException("A printer is offline");
                }

                communicateResult = Communication.Result.ErrorWritePort;
                byte[] sendData = mFunction.createCommands();
                mPort.writePort(sendData, 0, sendData.length);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Do nothing
                }

//          sendData = function.getSecondSendData();
//          port.writePort(sendData, 0, sendData.length);

                byte[] data = new byte[1024];
                int amount = 0;

                long start = System.currentTimeMillis();

                while (true) {
                    int receiveSize;
//              Log.d("Test", "readPort  " + amount + " " + data.length);
                    receiveSize = mPort.readPort(data, amount, data.length - amount);

                    if (0 < receiveSize) {
                        amount += receiveSize;
                    }

                    byte[] receiveData = new byte[amount];
                    System.arraycopy(data, 0, receiveData, 0, amount);

                    if (mFunction.onReceiveCallback(receiveData)) {
                        result = true;
                        communicateResult = Communication.Result.Success;
                        break;
                    }

                    if (10000 < (System.currentTimeMillis() - start)) {     // TODO:時間設定
                        communicateResult = Communication.Result.ErrorReadPort;
                        break;
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }
                }
            } catch (StarIOPortException e) {
                // Nothing
            }

            if (mPort != null && mPortName != null) {
                try {
                    StarIOPort.releasePort(mPort);
                } catch (StarIOPortException e) {
                    // Nothing
                }
                mPort = null;
            }

            resultSendCallback(result, communicateResult, mCallback);
        }
    }

    private static void resultSendCallback(final boolean result, final Communication.Result communicateResult, final Communication.SendCallback callback) {
        if (callback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onStatus(result, communicateResult);
                }
            });
        }
    }
}

class RetrieveStatusThread extends Thread {
    private final Object mLock;
    private Communication.StatusCallback mCallback;

    private StarIOPort mPort;

    private String mPortName = null;
    private String mPortSettings;
    private int     mTimeout;
    private Context mContext;

    @SuppressWarnings("unused")
    RetrieveStatusThread(Object lock, StarIOPort port, Communication.StatusCallback callback) {
        mPort     = port;
        mCallback = callback;
        mLock     = lock;
    }

    RetrieveStatusThread(Object lock, String portName, String portSettings, int timeout, Context context, Communication.StatusCallback callback) {
        mPortName     = portName;
        mPortSettings = portSettings;
        mTimeout      = timeout;
        mContext      = context;
        mCallback     = callback;
        mLock         = lock;
    }

    @Override
    public void run() {

        synchronized (mLock) {
            StarPrinterStatus status = null;

            try {
                if (mPort == null) {

                    if (mPortName == null) {
                        resultSendCallback(null, mCallback);
                        return;
                    } else {
                        mPort = StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext);
                    }
                }
                if (mPort == null) {
                    resultSendCallback(null, mCallback);
                    return;
                }

//          // When using USB interface with mPOP(F/W Ver 1.0.1), you need to send the following data.
//          byte[] dummy = {0x00};
//          port.writePort(dummy, 0, dummy.length);

                status = mPort.retreiveStatus();

            } catch (StarIOPortException e) {
                // Nothing
            }

            if (mPort != null && mPortName != null) {
                try {
                    StarIOPort.releasePort(mPort);
                } catch (StarIOPortException e) {
                    // Nothing
                }
                mPort = null;
            }

            resultSendCallback(status, mCallback);
        }
    }

    private static void resultSendCallback(final StarPrinterStatus status, final Communication.StatusCallback callback) {
        if (callback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onStatus(status);
                }
            });
        }
    }
}