package org.syzygy.postal.io;

import javax.microedition.io.Connection;
import java.io.IOException;

public abstract class Transport
{
    protected Transport(String remoteAddress)
    {
        this.remoteAddress = remoteAddress;
    }

    String getRemoteAddress()
    {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress)
    {
        this.remoteAddress = remoteAddress;
    }

    protected abstract String getPrefix();

    public String toString()
    {
        return getPrefix() + getRemoteAddress();
    }

    public boolean hasPeer()
    {
        return remoteAddress != null && !"".equals(remoteAddress);
    }

    public void connect(String message) throws IOException
    {
        conn = doConnect(message);
    }

    public void send(String message) throws IOException
    {
        send(conn, message);
    }

    public String receive() throws IOException
    {
        return receive(conn);
    }

    public void listen() throws IOException
    {
        conn = doListen();
    }

    protected abstract Connection doConnect(String message) throws IOException;

    protected abstract Connection doListen() throws IOException;

    protected abstract void send(Connection conn, String message) throws IOException;

    protected abstract String receive(Connection conn) throws IOException;

    public synchronized void close()
    {
        Util.closeQuietly(conn);
        conn = null;
    }

    private String remoteAddress;
    private Connection conn = null;
}