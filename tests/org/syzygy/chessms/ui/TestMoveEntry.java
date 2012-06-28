package org.syzygy.chessms.ui;

import junit.framework.TestCase;

import javax.microedition.lcdui.Canvas;

public final class TestMoveEntry extends TestCase
{
    public void testWhiteInitialState()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processActionKey(Canvas.UP);
        assertEquals("a1", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testBlackInitialState()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processActionKey(Canvas.UP);
        assertEquals("h8", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testWhiteUp()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.UP);
        assertEquals("a2", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testBlackUp()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.UP);
        assertEquals("h7", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testWhiteDown()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.DOWN);
        assertEquals("a8", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testBlackDown()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.DOWN);
        assertEquals("h1", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testWhiteLeft()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.LEFT);
        assertEquals("h1", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testBlackLeft()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.LEFT);
        assertEquals("a8", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testWhiteRight()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.RIGHT);
        assertEquals("b1", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testBlackRight()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.RIGHT);
        assertEquals("g8", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testWhiteFire()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.FIRE);
        assertEquals("a1-a1", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testBlackFire()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.FIRE);
        assertEquals("h8-h8", entry.toString());
        assertFalse(entry.isComplete());
    }

    public void testA1A2()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processKey('1');
        entry.processKey('1');
        entry.processKey('1');
        entry.processKey('2');
        assertEquals("a1-a2", entry.toString());
        assertTrue(entry.isComplete());
    }

    public void testClear()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processKey('1');
        entry.processKey('2');
        entry.processKey('#');
        entry.processKey('3');
        assertEquals("a3-", entry.toString());
    }

    public void testWhiteE2Up()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processKey('5');
        entry.processKey('2');
        entry.processActionKey(Canvas.UP);
        assertEquals("e2-e3", entry.toString());
        assertTrue(entry.isComplete());
    }

    public void testBlackE2Up()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processKey('5');
        entry.processKey('2');
        entry.processActionKey(Canvas.UP);
        assertEquals("e2-e1", entry.toString());
        assertTrue(entry.isComplete());
    }

    public void testWhiteUpA4()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.UP);
        entry.processKey('1');
        entry.processKey('4');
        assertEquals("a2-a4", entry.toString());
        assertTrue(entry.isComplete());
    }

    public void testBlackUpA4()
    {
        MoveEntry entry = new BlackMoveEntry();
        entry.processActionKey(Canvas.UP);
        entry.processActionKey(Canvas.UP);
        entry.processKey('1');
        entry.processKey('4');
        assertEquals("h7-a4", entry.toString());
        assertTrue(entry.isComplete());
    }

    public void testGetSquareNone()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processKey('5');
        assertNull(entry.getSquare());
    }

    public void testGetFirstSquare()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processKey('5');
        entry.processKey('2');
        assertEquals("e2", entry.getSquare());
    }

    public void testGetSecondSquare()
    {
        MoveEntry entry = new WhiteMoveEntry();
        entry.processKey('5');
        entry.processKey('2');
        entry.processKey('5');
        entry.processKey('4');
        assertEquals("e4", entry.getSquare());
    }
}
