package org.syzygy.postal.io.midp;

import org.syzygy.postal.io.Completion;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BluetoothTransport extends MidpTransport
{
    public static final String PREFIX = "btspp://";

    public BluetoothTransport(String uuid, String remoteAddress, BluetoothDiscovery bluetooth)
    {
        super(remoteAddress);
        this.uuid = uuid;
        this.bluetooth = bluetooth;
    }

    protected String getPrefix()
    {
        return PREFIX;
    }

    protected Connection doListen() throws IOException
    {
        Connection c = null;
        try {
            this.notifier = (StreamConnectionNotifier) Connector.open(PREFIX + "localhost:" + uuid);
            c = notifier.acceptAndOpen();
            setRemoteAddress(RemoteDevice.getRemoteDevice(c).getBluetoothAddress());
            return c;
        } catch (IOException e) {
            if (c == null)
                IOUtil.closeQuietly(notifier);
            else
                IOUtil.closeQuietly(c);
            throw e;
        }
    }

    private String discover() throws IOException
    {
        // ugh!
        final Object[] r = new Object[1];
        bluetooth.search(getRemoteAddress(), uuid, new Completion()
        {
            public void complete(Object result)
            {
                r[0] = result;
            }

            public void error(Exception e)
            {
                r[0] = e;
            }
        });
        bluetooth.waitComplete();
        if (r[0] instanceof String)
            return (String) r[0];
        throw (IOException) r[0];
    }

    protected Connection doConnect(String message) throws IOException
    {
        StreamConnection conn = null;
        try {
            conn = (StreamConnection) Connector.open(discover());
            send(conn, message);
            return conn;
        } catch (IOException e) {
            IOUtil.closeQuietly(conn);
            throw e;
        }
    }

    protected String receive(Connection conn) throws IOException
    {
        if (input == null)
            input = ((StreamConnection) conn).openDataInputStream();
        return input.readUTF();
    }

    protected void send(Connection conn, String message) throws IOException
    {
        if (output == null)
            output = ((StreamConnection) conn).openDataOutputStream();
        output.writeUTF(message);
    }

    public void close()
    {
        super.close();
        IOUtil.closeQuietly(notifier);
        IOUtil.closeQuietly(input);
        IOUtil.closeQuietly(output);
    }

    private StreamConnectionNotifier notifier;
    private DataInputStream input;
    private DataOutputStream output;
    private final String uuid;
    private final BluetoothDiscovery bluetooth;
}