package org.syzygy.postal;

import org.syzygy.postal.io.midp.SmsTransport;

import javax.microedition.io.PushRegistry;
import javax.microedition.lcdui.*;

public final class SmsMIDlet extends ChessMIDlet
{
    public SmsMIDlet()
    {
        this.port = getAppProperty("midlet.port");
    }

    protected void onStartApp()
    {
        form.addCommand(ok);
        form.addCommand(cancel);
        form.append(number);
        form.setCommandListener(new CommandListener()
        {
            public void commandAction(Command c, Displayable disp)
            {
                String n = number.getString();
                if (c == cancel)
                    setTransport(new SmsTransport(port, null));
                else if (n != null && !"".equals(n))
                    setTransport(new SmsTransport(port, number.getString()));
            }
        });
        starting();
    }

    protected void findOpponent(Display display)
    {
        String[] conns = PushRegistry.listConnections(true);
        if (conns != null && conns.length == 1)
            setTransport(new SmsTransport(port, null));
        else
            display.setCurrent(form);
    }

    protected void findExistingOpponent(Display display, String url, boolean toMove)
    {
        if (url != null && url.startsWith(SmsTransport.PREFIX)) {
            String number = url.substring(SmsTransport.PREFIX.length());
            setTransport(new SmsTransport(port, number));
        } else if (!toMove)
            setTransport(new SmsTransport(port, null));
        else {
            form.removeCommand(cancel);
            findOpponent(display);
        }
    }

    private final String port;
    private final Form form = new Form("Select Opponent");
    private final TextField number = new TextField("Phone Contact", null, 64, TextField.PHONENUMBER);
}
