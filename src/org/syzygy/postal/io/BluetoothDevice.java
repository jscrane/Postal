package org.syzygy.postal.io;

import javax.bluetooth.RemoteDevice;

public final class BluetoothDevice
{
    BluetoothDevice(RemoteDevice device)
    {
        this.device = device;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public RemoteDevice getDevice()
    {
        return device;
    }

    void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return name == null ? device.getBluetoothAddress() : name;
    }

    private final RemoteDevice device;
    private int index = -1;
    private String name;
}
