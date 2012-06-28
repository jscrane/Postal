package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Piece;

public final class TestValidationUtils extends BoardValidatorSupport
{
    public void testIsAttacked()
    {
        Board board = new Board();
        board.set(new Piece(Piece.ROOK, black), b8);
        board.set(new Piece(Piece.ROOK, black), b1);
        board.set(new Piece(Piece.PAWN, white), b4);
        assertTrue(utils.isAttacked(board, b4, board.getPieces(Colour.BLACK)));
    }

    private final ValidationUtils utils = new Validators();
}
