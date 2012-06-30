package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Piece;

public final class TestObstruction extends BoardValidatorSupport
{
    protected void setUp()
    {
        this.board = new Board();
        this.validator = new Obstruction();
    }

    public void testCannotObstructKnight() throws Exception
    {
        board.set(Piece.knight(white), b1);
        validator.validate(board, move(b1, c2));
    }

    public void testObstructedPositiveDiagonalMove()
    {
        board.set(Piece.bishop(white), c1);
        board.set(Piece.knight(white), f4);
        try {
            validator.validate(board, move(c1, h6));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testObstructedNegativeDiagonalMove()
    {
        board.set(Piece.bishop(black), c8);
        board.set(Piece.knight(black), f5);
        try {
            validator.validate(board, move(c8, h3));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testObstructedPositiveVerticalMove()
    {
        board.set(Piece.rook(white), a1);
        board.set(Piece.pawn(black), a6);
        try {
            validator.validate(board, move(a1, a8));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testObstructedNegativeVerticalMove()
    {
        board.set(Piece.rook(black), a8);
        board.set(Piece.pawn(white), a6);
        try {
            validator.validate(board, move(a8, a1));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testUnobstructedMove() throws Exception
    {
        board.set(Piece.rook(black), a8);
        validator.validate(board, move(a8, a1));
    }

    private Board board;
    private BoardValidator validator;
}
