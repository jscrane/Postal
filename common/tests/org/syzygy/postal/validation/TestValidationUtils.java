package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Piece;

public final class TestValidationUtils extends BoardValidatorSupport
{
    public void testIsAttacked()
    {
        Board board = new Board();
        board.set(Piece.rook(black), b8);
        board.set(Piece.rook(black), b1);
        board.set(Piece.pawn(white), b4);
        assertTrue(utils.isAttacked(board, b4, board.getPieces(Colour.BLACK)));
    }

    public void testAttackerCount()
    {
        Board board = new Board();
        board.set(Piece.rook(black), b8);
        board.set(Piece.rook(black), b1);
        board.set(Piece.pawn(white), b4);
        assertEquals(2, utils.attackerCount(board, b4, board.getPieces(Colour.BLACK)));
    }

    private final ValidationUtils utils = new Validators();
}
