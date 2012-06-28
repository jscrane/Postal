package org.syzygy.postal.action;

import org.syzygy.postal.io.AbstractTransport;
import org.syzygy.postal.io.EventListener;

public final class Partner
{
    public Partner(PartnerCallback callback, EventListener listener)
    {
        this.callback = callback;
        this.listener = listener;
    }

    public void listen(final AbstractTransport transport)
    {
        setTransport(transport);
        new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    transport.listen();
                    loop();
                } catch (Exception e) {
                    transport.close();
                    listener.onEvent("Listen: " + transport, e);
                }
            }
        }).start();
    }

    public void connect(final AbstractTransport transport)
    {
        setTransport(transport);
        new Thread(new Runnable()
        {
            public void run()
            {
                String s = waitMessage();
                try {
                    transport.connect(s);
                    callback.onSent(s);
                    loop();
                } catch (Exception e) {
                    transport.close();
                    send(s);
                    listener.onEvent("Connect: " + transport, e);
                }
            }
        }).start();
    }

    private void loop()
    {
        for (; ; ) {
            try {
                callback.onReceive(transport.receive());
            } catch (Exception e) {
                listener.onEvent("Receive", e);
                return;
            }
            String s = waitMessage();
            try {
                transport.send(s);
                callback.onSent(s);
            } catch (Exception e) {
                send(s);
                listener.onEvent("Send", e);
                return;
            }
        }
    }

    public void send(String m)
    {
        synchronized (lock) {
            while (message != null)
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            this.message = m;
            lock.notify();
        }
    }

    String waitMessage()
    {
        synchronized (lock) {
            while (message == null)
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            String m = message;
            this.message = null;
            lock.notify();
            return m;
        }
    }

    public String toString()
    {
        return transport.toString();
    }

    void setTransport(AbstractTransport transport)
    {
        this.transport = transport;
    }

    private String message;
    private AbstractTransport transport;

    private final Object lock = new Object();
    private final PartnerCallback callback;
    private final EventListener listener;
}
