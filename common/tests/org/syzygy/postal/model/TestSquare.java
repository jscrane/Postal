package org.syzygy.postal.model;

import junit.framework.Assert;
import junit.framework.TestCase;

public final class TestSquare extends TestCase
{
    public void testSquare()
    {
        Square p = new Square("e7");
        Assert.assertEquals(4, p.getFile());
        Assert.assertEquals(6, p.getRank());
    }

    public void testBadSquare()
    {
        try {
            new Square("foo");
            Assert.fail("Didn't get expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("illegal square: foo", e.getMessage());
        }
    }

    public void testIllegalFile()
    {
        try {
            new Square("z4");
            Assert.fail("Didn't get expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("illegal file: z", e.getMessage());
        }
    }

    public void testIllegalRank()
    {
        try {
            new Square("a9");
            Assert.fail("Didn't get expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("illegal rank: 9", e.getMessage());
        }
    }
}
