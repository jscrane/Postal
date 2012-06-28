package org.syzygy.chessms.action;

import java.io.IOException;

import javax.microedition.io.Connection;

import junit.framework.TestCase;

import org.syzygy.chessms.io.Transport;

public class TestPartner
	extends TestCase
{
	public void testWhite()
		throws Exception
	{
		final String[] white = new String[] { "e2-e4", "d2-d4" };
		final String[] black = new String[] { "e7-e5", "d7-d5" };
		DummyTransport transport = new DummyTransport("white", white, black);
		Partner partner = new Partner(new DummyCallback(black), null);
		partner.connect(transport);
		for (int i = 0; i < white.length; i++)
			partner.send(white[i]);
		Thread.sleep(50);
		assertTrue(transport.success());
	}

	public void testBlack()
		throws Exception
	{
		final String[] white = new String[] { "e2-e4", "d2-d4", "g1-f3" };
		final String[] black = new String[] { "e7-e5", "d7-d5", "g8-f6" };
		DummyTransport transport = new DummyTransport("black", black, white);
		Partner partner = new Partner(new DummyCallback(white), null);
		partner.listen(transport);
		for (int i = 0; i < black.length; i++)
			partner.send(black[i]);
		Thread.sleep(50);
		assertTrue(transport.success());
	}

	private class DummyCallback
		implements PartnerCallback
	{
		public DummyCallback(String[] expected)
		{
			this.expected = expected;
		}

		public void onReceive(String move)
		{
			if (move == null)
				assertEquals(expected.length, i);
			else {
				assertEquals(expected[i], move);
				i++;
			}
		}
		
		public void onSent(String move)
		{
			// eh
		}

		private int i = 0;
		private String[] expected;
	}

	private class DummyTransport
		extends Transport
	{
		public DummyTransport(String address, String[] toSend, String[] toReceive)
		{
			super(address);
			this.toSend = toSend;
			this.toReceive = toReceive;
			setRemoteAddress(address);
		}

		protected String getPrefix() { return ""; }
		
		protected Connection doConnect(String message)
			throws IOException
		{
			send(null, message);
			return null;
		}

		protected Connection doListen()
			throws IOException
		{
			return null;
		}

		protected void send(Connection conn, String move)
			throws IOException
		{
			assertEquals(toSend[s++], move);
		}

		protected String receive(Connection conn)
			throws IOException
		{
			return r == toReceive.length? null: toReceive[r++];
		}

		public boolean success()
		{
			return r == toReceive.length && s == toSend.length;
		}

		private int r = 0, s = 0;
		private final String[] toReceive, toSend;
	}
}
