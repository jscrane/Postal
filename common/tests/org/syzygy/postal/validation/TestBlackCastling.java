package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

public final class TestBlackCastling extends BoardValidatorSupport
{
    public void testKingsSide() throws Exception
    {
        board.set(Piece.king(black), e8);
        board.set(Piece.rook(black), h8);
        Move m = move(e8, g8);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        assertEquals(Piece.king(Colour.BLACK), board.get(g8));
        assertEquals(Piece.rook(Colour.BLACK), board.get(f8));
    }

    public void testKingsSideRookAbsent()
    {
        board.set(Piece.king(black), e8);
        try {
            validator.validate(board, move(e8, g8));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testQueensSide() throws Exception
    {
        board.set(Piece.king(black), e8);
        board.set(Piece.rook(black), a8);
        Move m = move(e8, c8);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        assertEquals(Piece.king(Colour.BLACK), board.get(c8));
        assertEquals(Piece.rook(Colour.BLACK), board.get(d8));
    }

    public void testQueensSideRookAbsent()
    {
        board.set(Piece.king(black), e8);
        try {
            validator.validate(board, move(e8, c8));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testKingsSideWhiteRook()
    {
        board.set(Piece.king(black), e8);
        board.set(Piece.rook(white), h8);
        try {
            validator.validate(board, move(e8, g8));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    private final Board board = new Board();
    private final Castling validator = new Castling(Colour.BLACK, new Validators());
}
