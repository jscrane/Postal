package org.syzygy.postal.model;

import junit.framework.TestCase;

public final class TestBoard extends TestCase
{
    public void testEmptyToString()
    {
        Board board = new Board();
        assertEquals(" _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n" +
                " _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n" +
                " _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n" +
                " _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n", board.toString());
        assertEquals(0, board.getMaterialAdvantage());
    }

    public void testPlaceWhitePawnE4ToString()
    {
        Board board = new Board();
        board.set(Piece.pawn(Colour.WHITE), new Square("e4"));
        assertEquals(" _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n" +
                " _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n" +
                " _ _P_ _" + "\n" +
                "_ _ _ _ " + "\n" +
                " _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n", board.toString());
        assertEquals(1, board.getMaterialAdvantage());
    }

    public void testCreatedBoardToString()
    {
        Board board = Board.create();
        assertEquals("rnbqkbnr" + "\n" +
                "pppppppp" + "\n" +
                " _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n" +
                " _ _ _ _" + "\n" +
                "_ _ _ _ " + "\n" +
                "PPPPPPPP" + "\n" +
                "RNBQKBNR" + "\n", board.toString());
        assertEquals(0, board.getMaterialAdvantage());
    }

    public void testCopyBoard()
    {
        Board board = new Board();
        Square a1 = new Square("a1"), b1 = new Square("b1"), b8 = new Square("b8");
        board.set(Piece.rook(Colour.BLACK), b8);
        board.set(Piece.king(Colour.WHITE), b1);
        Board copy = new Board(board);
        assertEquals(board.get(b1), copy.get(b1));
        assertEquals(board.get(b8), copy.get(b8));
        assertEquals(board.get(a1), copy.get(a1));
    }
}
