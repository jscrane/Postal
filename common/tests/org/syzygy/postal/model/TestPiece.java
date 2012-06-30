package org.syzygy.postal.model;

import junit.framework.Assert;
import junit.framework.TestCase;

public final class TestPiece extends TestCase
{
    public void testPiece()
    {
        Piece p = new Piece('k');
        Assert.assertTrue(p.is(Colour.BLACK));
        Assert.assertTrue(p.isA(Piece.KING));
    }

    public void testBadPiece()
    {
        try {
            new Piece('z');
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // ...
        }
    }
}
