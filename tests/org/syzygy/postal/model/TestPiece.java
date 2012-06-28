package org.syzygy.postal.model;

import junit.framework.TestCase;

public final class TestPiece extends TestCase
{
    public void testPiece()
    {
        Piece p = new Piece('k');
        assertEquals(Colour.BLACK, p.getColour());
        assertEquals(Piece.KING, p.getType());
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
