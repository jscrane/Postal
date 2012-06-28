package org.syzygy.postal.io.midp;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import java.io.IOException;

public final class SmsTransport extends MidpTransport
{
    public static final String PREFIX = "sms://";

    public SmsTransport(String port, String number)
    {
        super(number);
        this.port = port;
    }

    protected String getPrefix()
    {
        return PREFIX;
    }

    private void send(MessageConnection conn, String message, String number) throws IOException
    {
        TextMessage tm = (TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
        tm.setAddress(PREFIX + number + ":" + port);
        tm.setPayloadText(message);
        conn.send(tm);
    }

    protected Connection doConnect(String message) throws IOException
    {
        MessageConnection conn = (MessageConnection) Connector.open(PREFIX + ":" + port);
        send(conn, message, getRemoteAddress());
        return conn;
    }

    protected synchronized Connection doListen() throws IOException
    {
        return Connector.open(PREFIX + ":" + port);
    }

    protected void send(Connection conn, String message) throws IOException
    {
        send((MessageConnection) conn, message, getRemoteAddress());
    }

    protected String receive(Connection conn) throws IOException
    {
        TextMessage tm = (TextMessage) ((MessageConnection) conn).receive();
        if (getRemoteAddress() == null) {
            String number = tm.getAddress().substring(PREFIX.length());
            int colon = number.indexOf(':');
            if (colon > 0)
                number = number.substring(0, colon);
            setRemoteAddress(number);
        }
        return tm.getPayloadText();
    }

    private final String port;
}
