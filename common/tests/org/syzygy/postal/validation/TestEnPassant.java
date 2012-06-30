package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

public final class TestEnPassant extends BoardValidatorSupport
{
    public void testEnPassant() throws Exception
    {
        board.set(Piece.pawn(white), e5);
        board.set(Piece.pawn(black), f7);

        Move m = move(f7, f5);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);

        m = move(e5, f6);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        assertNull(board.get(f5));
    }

    public void testNotEnPassant() throws Exception
    {
        board.set(Piece.pawn(white), d4);
        board.set(Piece.pawn(white), e4);
        board.set(Piece.pawn(black), e5);

        Move m = move(d4, e5);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);

        assertNotNull(board.get(e4));
    }

    public void testSameColour() throws Exception
    {
        board.set(Piece.pawn(white), e5);
        board.set(Piece.pawn(white), f7);

        Move m = move(f7, f5);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);

        m = move(e5, f6);
        try {
            validator.validate(board, m);
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testTooLate() throws Exception
    {
        board.set(Piece.pawn(white), d2);
        board.set(Piece.pawn(white), e5);
        board.set(Piece.pawn(black), f7);
        board.set(Piece.pawn(black), g7);

        Move m = move(f7, f5);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);

        m = move(d2, d3);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);

        m = move(g7, g5);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        m = move(e5, f6);
        try {
            validator.validate(board, m);
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    private final Board board = new Board();
    private final EnPassant validator = new EnPassant();
}
