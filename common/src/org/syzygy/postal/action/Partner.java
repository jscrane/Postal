package org.syzygy.postal.action;

import org.syzygy.postal.io.AbstractTransport;
import org.syzygy.postal.io.Completion;

public final class Partner
{
    public Partner(PartnerCallback callback)
    {
        this.callback = callback;
    }

    public void listen(final AbstractTransport transport, final Completion result)
    {
        setTransport(transport);
        new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    transport.listen();
                    result.complete(null);
                    loop();
                } catch (Exception e) {
                    result.error(e);
                }
            }
        }).start();
    }

    public void connect(final AbstractTransport transport, final Completion result)
    {
        setTransport(transport);
        new Thread(new Runnable()
        {
            public void run()
            {
                String s = waitMessage();
                try {
                    transport.connect(s);
                    result.complete(null);
                    callback.onSent(s);
                    loop();
                } catch (Exception e) {
                    result.error(e);
                    send(s);
                }
            }
        }).start();
    }

    private void loop() throws Exception
    {
        for (; ; ) {
            callback.onReceive(transport.receive());
            String s = waitMessage();
            try {
                transport.send(s);
                callback.onSent(s);
            } catch (Exception e) {
                send(s);
                throw e;
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
}
