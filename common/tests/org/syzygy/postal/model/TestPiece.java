package org.syzygy.postal.model;

import junit.framework.TestCase;

public final class TestPiece extends TestCase
{
    public void testPiece()
    {
        Piece p = new Piece('k');
        assertTrue(p.is(Colour.BLACK));
        assertTrue(p.isA(Piece.KING));
    }

    public void testBadPiece()
    {
        try {
            new Piece('z');
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // ...
        }
    }
}
