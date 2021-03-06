package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Piece;

public final class TestPawnCapture extends BoardValidatorSupport
{
    protected void setUp()
    {
        this.board = new Board();
        this.validator = new PawnCapture();
    }

    public void testLegalWhiteCapture() throws Exception
    {
        board.set(Piece.pawn(black), d5);
        board.set(Piece.pawn(white), e4);
        validator.validate(board, move(e4, d5));
    }

    public void testLegalBlackCapture() throws Exception
    {
        board.set(Piece.pawn(white), e4);
        board.set(Piece.pawn(black), d5);
        validator.validate(board, move(d5, e4));
    }

    public void testPieceInWay()
    {
        board.set(Piece.pawn(black), d6);
        board.set(Piece.pawn(white), d5);
        try {
            validator.validate(board, move(d6, d5));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testIllegalBlackMove()
    {
        board.set(Piece.pawn(black), d6);
        board.set(Piece.pawn(white), b4);
        try {
            validator.validate(board, move(b4, d6));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    private Board board;
    private BoardValidator validator;
}
