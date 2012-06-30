package org.syzygy.postal.validation;

import junit.framework.Assert;
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
        Assert.assertTrue(utils.isAttacked(board, b4, board.getPieces(Colour.BLACK)));
    }

    private final ValidationUtils utils = new Validators();
}
