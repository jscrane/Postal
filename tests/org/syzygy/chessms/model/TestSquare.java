package org.syzygy.chessms.model;

import junit.framework.TestCase;

public class TestSquare
    extends TestCase
{
    public void testSquare()
    {
        Square p = new Square("e7");
        assertEquals(4, p.getFile());
        assertEquals(6, p.getRank());
    }
    
    public void testBadSquare()
    {
        try {
            new Square("foo");
            fail("Didn't get expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal square: foo", e.getMessage());
        }
    }

    public void testIllegalFile()
    {
        try {
            new Square("z4");
            fail("Didn't get expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal file: z", e.getMessage());
        }
    }

    public void testIllegalRank()
    {
        try {
            new Square("a9");
            fail("Didn't get expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal rank: 9", e.getMessage());
        }
    }
}
