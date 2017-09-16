package com.magestore.app.pos.util;

public interface IStarIoExtFunction {
    byte[] createCommands();
    boolean onReceiveCallback(byte[] data);
}
