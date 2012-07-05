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
        try {
            this.notifier = (StreamConnectionNotifier) Connector.open(PREFIX + "localhost:" + uuid);
            Connection c = notifier.acceptAndOpen();
            setRemoteAddress(RemoteDevice.getRemoteDevice(c).getBluetoothAddress());
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("open(" + PREFIX + "localhost:" + uuid + "): " + e.getMessage());
        }
    }

    private String discover() throws IOException
    {
        // ugh!
        final String[] urls = new String[1];
        bluetooth.search(getRemoteAddress(), uuid, new Completion()
        {
            public void complete(Object result)
            {
                urls[0] = (String) result;
            }

            public void error(Exception e)
            {
                // TODO
            }
        });
        bluetooth.waitComplete();
        return urls[0];
    }

    protected Connection doConnect(String message) throws IOException
    {
        try {
            StreamConnection conn = (StreamConnection) Connector.open(discover());
            send(conn, message);
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (RuntimeException e) {
            e.printStackTrace();
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