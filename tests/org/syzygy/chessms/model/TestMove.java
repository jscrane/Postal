package org.syzygy.chessms.model;

import junit.framework.TestCase;

public final class TestMove extends TestCase
{
    public void testToString()
    {
        Move m = new Move(new Square("e2"), new Square("e4"), "yawn");
        assertEquals("e2-e4 yawn", m.toString());
        assertEquals(2, m.getDy());
        assertEquals(0, m.getDx());
    }

    public void testToStringCaptureCheck()
    {
        Move m = new Move(new Square("a7"), new Square("b8"), "gotcha");
        m.setIsCapture(true);
        m.setIsCheck(true);
        assertEquals("a7xb8+ gotcha", m.toString());
    }

    public void testParse()
    {
        Move m = Move.valueOf("e2-e4 yawn");
        assertEquals("e2", m.getFrom().toString());
        assertEquals("e4", m.getTo().toString());
        assertFalse(m.isCapture());
        assertFalse(m.isCheck());
        assertEquals("yawn", m.getComment());
    }

    public void testParseCaptureCheck()
    {
        Move m = Move.valueOf("a7xb8+ gotcha");
        assertEquals("a7", m.getFrom().toString());
        assertEquals("b8", m.getTo().toString());
        assertTrue(m.isCapture());
        assertTrue(m.isCheck());
        assertEquals("gotcha", m.getComment());
    }

    public void testParseIllegalInput()
    {
        try {
            Move.valueOf("");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testWhiteResigns()
    {
        assertEquals("0-1", Move.WHITE_RESIGNS.toString());
    }

    public void testBlackResigns()
    {
        assertEquals("1-0", Move.BLACK_RESIGNS.toString());
    }

    public void testIsResignation()
    {
        assertTrue(Move.BLACK_RESIGNS.isResignation());
    }
}
