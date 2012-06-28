package org.syzygy.postal.model;

import junit.framework.Assert;
import junit.framework.TestCase;

public final class TestMove extends TestCase
{
    public void testToString()
    {
        Move m = new Move(new Square("e2"), new Square("e4"), "yawn");
        Assert.assertEquals("e2-e4 yawn", m.toString());
        Assert.assertEquals(2, m.getDy());
        Assert.assertEquals(0, m.getDx());
    }

    public void testToStringCaptureCheck()
    {
        Move m = new Move(new Square("a7"), new Square("b8"), "gotcha");
        m.setIsCapture(true);
        m.setIsCheck(true);
        Assert.assertEquals("a7xb8+ gotcha", m.toString());
    }

    public void testParse()
    {
        Move m = Move.valueOf("e2-e4 yawn");
        Assert.assertEquals("e2", m.getFrom().toString());
        Assert.assertEquals("e4", m.getTo().toString());
        Assert.assertFalse(m.isCapture());
        Assert.assertFalse(m.isCheck());
        Assert.assertEquals("yawn", m.getComment());
    }

    public void testParseCaptureCheck()
    {
        Move m = Move.valueOf("a7xb8+ gotcha");
        Assert.assertEquals("a7", m.getFrom().toString());
        Assert.assertEquals("b8", m.getTo().toString());
        Assert.assertTrue(m.isCapture());
        Assert.assertTrue(m.isCheck());
        Assert.assertEquals("gotcha", m.getComment());
    }

    public void testParseIllegalInput()
    {
        try {
            Move.valueOf("");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testWhiteResigns()
    {
        Assert.assertEquals("0-1", Move.WHITE_RESIGNS.toString());
    }

    public void testBlackResigns()
    {
        Assert.assertEquals("1-0", Move.BLACK_RESIGNS.toString());
    }

    public void testIsResignation()
    {
        Assert.assertTrue(Move.BLACK_RESIGNS.isResignation());
    }
}
