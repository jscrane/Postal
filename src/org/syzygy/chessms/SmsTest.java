package org.syzygy.chessms;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

public class SmsTest extends MIDlet
{
    protected void destroyApp(boolean unconditional)
    {
        notifyDestroyed();
    }

    protected void pauseApp()
    {
        notifyPaused();
    }

    protected void startApp() throws MIDletStateChangeException
    {
        form.addCommand(send);
        form.addCommand(exit);
        form.setCommandListener(new CommandListener()
        {
            public void commandAction(Command c, Displayable disp)
            {
                if (c == exit)
                    destroyApp(false);
                else if (c == send) {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            MessageConnection conn = null;
                            status.setText("");
                            String address = number.getString();
                            String where = "open";
                            try {
                                conn = (MessageConnection) Connector.open("sms://" + address + ":" + port);
                                where = "new";
                                TextMessage tm = (TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
                                where = "setPayload";
                                tm.setPayloadText(message.getString());
                                where = "send";
                                conn.send(tm);
                            } catch (Exception e) {
                                e.printStackTrace();
                                status.setText(where + ": " + e.getMessage());
                            } finally {
                                if (conn != null)
                                    try {
                                        conn.close();
                                    } catch (Exception e) {
                                        // ignored
                                    }
                            }
                        }
                    }).start();
                }
            }
        });
        form.append(number);
        form.append(message);
        form.append(status);
        display.setCurrent(form);
    }

    public SmsTest()
    {
        this.display = Display.getDisplay(this);
        this.port = getAppProperty("midlet.port");
    }

    private final Display display;
    private final String port;
    private final Form form = new Form("Send");
    private final Command send = new Command("Send", Command.SCREEN, 1);
    private final Command exit = new Command("Exit", Command.SCREEN, 1);
    private final TextField number = new TextField("Number", null, 16, TextField.PHONENUMBER);
    private final TextField message = new TextField("Move", null, 32, TextField.ANY);
    private final StringItem status = new StringItem(null, null);
}
