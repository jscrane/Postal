package org.syzygy.postal.action;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.syzygy.postal.io.AbstractTransport;
import org.syzygy.postal.io.Completion;

import java.io.IOException;

public final class TestPartner extends TestCase
{
    public void testWhite() throws Exception
    {
        final String[] white = new String[]{ "e2-e4", "d2-d4" };
        final String[] black = new String[]{ "e7-e5", "d7-d5" };
        DummyTransport transport = new DummyTransport(white, black);
        Partner partner = new Partner(new DummyCallback(black));
        partner.connect(transport, dummyCompletion);
        for (int i = 0; i < white.length; i++)
            partner.send(white[i]);
        Thread.sleep(50);
        Assert.assertTrue(transport.success());
    }

    public void testBlack() throws Exception
    {
        final String[] white = new String[]{ "e2-e4", "d2-d4", "g1-f3" };
        final String[] black = new String[]{ "e7-e5", "d7-d5", "g8-f6" };
        DummyTransport transport = new DummyTransport(black, white);
        Partner partner = new Partner(new DummyCallback(white));
        partner.listen(transport, dummyCompletion);
        for (int i = 0; i < black.length; i++)
            partner.send(black[i]);
        Thread.sleep(50);
        Assert.assertTrue(transport.success());
    }

    private final class DummyCallback implements PartnerCallback
    {
        public DummyCallback(String[] expected)
        {
            this.expected = expected;
        }

        public void onReceive(String move)
        {
            if (move == null)
                Assert.assertEquals(expected.length, i);
            else {
                Assert.assertEquals(expected[i], move);
                i++;
            }
        }

        public void onSent(String move)
        {
            // eh
        }

        private int i = 0;
        private final String[] expected;
    }

    private final Completion dummyCompletion = new Completion()
    {
        public void complete(Object result)
        {
        }

        public void error(Exception e)
        {
        }
    };

    private final class DummyTransport implements AbstractTransport
    {
        public DummyTransport(String[] toSend, String[] toReceive)
        {
            this.toSend = toSend;
            this.toReceive = toReceive;
        }

        public boolean success()
        {
            return r == toReceive.length && s == toSend.length;
        }

        private int r = 0, s = 0;
        private final String[] toReceive, toSend;

        public void setRemoteAddress(String remoteAddress)
        {
        }

        public boolean hasPeer()
        {
            return false;
        }

        public void connect(String message) throws IOException
        {
            send(message);
        }

        public void send(String message) throws IOException
        {
            Assert.assertEquals(toSend[s++], message);
        }

        public String receive() throws IOException
        {
            return r == toReceive.length ? null : toReceive[r++];
        }

        public void listen() throws IOException
        {
        }

        public void close()
        {
        }
    }
}
