package org.syzygy.postal.io.midp;

import org.syzygy.postal.io.Completion;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.Vector;

public final class BluetoothDiscovery
{
    public BluetoothDiscovery() throws IOException
    {
        LocalDevice device = LocalDevice.getLocalDevice();
        if (!device.setDiscoverable(DiscoveryAgent.GIAC))
            throw new IOException("Failed to set GIAC, is the stack on?");
        this.agent = device.getDiscoveryAgent();
    }

    private static final class Device extends RemoteDevice
    {
        public Device(String address)
        {
            super(address);
        }
    }

    public void search(String address, String uuid, Completion operation) throws IOException
    {
        RemoteDevice device = new Device(address);
        this.listener = new DiscoveryAdapter(operation);
        agent.searchServices(null, new UUID[]{ new UUID(uuid, false) }, device, listener);
    }

    /**
     * Performs a device inquiry and fetches the devices' names
     *
     * @param devices   a collection of discovered BluetoothDevices
     * @param operation the completion to call when the operation has finished.
     *                  If no devices are found, this is called with a null parameter.
     *                  Otherwise for each device, this is called with a BluetoothDevice
     *                  parameter.
     */
    public void discoverDevices(final Vector devices, final Completion operation)
    {
        Completion c = new Completion()
        {
            public void complete(Object result)
            {
                if (result == null)
                    operation.complete(null);
                else
                    devices.addElement(new BluetoothDevice((RemoteDevice) result));
            }
        };
        this.listener = new DiscoveryAdapter(c);
        try {
            agent.startInquiry(DiscoveryAgent.GIAC, listener);
        } catch (IOException e) {
            e.printStackTrace();
            listener = null;
            operation.complete(null);
            return;
        }
        new Thread(new Runnable()
        {
            public void run()
            {
                waitComplete();
                if (devices.size() == 0)
                    operation.complete(null);
                else
                    for (int i = 0; i < devices.size(); i++) {
                        BluetoothDevice d = (BluetoothDevice) devices.elementAt(i);
                        String s;
                        try {
                            s = d.getDevice().getFriendlyName(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                            s = e.getMessage();
                        }
                        d.setName(s);
                        operation.complete(d);
                    }
            }
        }).start();
    }

    public synchronized void cancel()
    {
        if (listener != null) {
            agent.cancelInquiry(listener);
            listener = null;
        }
    }

    public synchronized void waitComplete()
    {
        while (listener != null)
            try {
                wait();
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
    }

    private final class DiscoveryAdapter implements DiscoveryListener
    {
        DiscoveryAdapter(Completion operation)
        {
            this.operation = operation;
        }

        public void deviceDiscovered(RemoteDevice device, DeviceClass cod)
        {
            operation.complete(device);
        }

        public void inquiryCompleted(int status)
        {
            complete();
        }

        public void servicesDiscovered(int transaction, ServiceRecord[] recs)
        {
            if (recs.length > 0)
                operation.complete(recs[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
        }

        public void serviceSearchCompleted(int transaction, int status)
        {
            complete();
        }

        private void complete()
        {
            synchronized (BluetoothDiscovery.this) {
                BluetoothDiscovery.this.listener = null;
                BluetoothDiscovery.this.notify();
            }
        }

        private final Completion operation;
    }

    private final DiscoveryAgent agent;
    private DiscoveryListener listener = null;
}
