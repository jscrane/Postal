package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Piece;

public final class TestBlackCastling extends BoardValidatorSupport
{
    public void testKingsSide() throws Exception
    {
        board.set(new Piece(Piece.KING, black), e8);
        board.set(new Piece(Piece.ROOK, black), h8);
        Move m = move(e8, g8);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        Piece king = board.get(g8), rook = board.get(f8);
        assertNotNull(king);
        assertEquals(Piece.KING, king.getType());
        assertNotNull(rook);
        assertEquals(Piece.ROOK, rook.getType());
    }

    public void testKingsSideRookAbsent()
    {
        board.set(new Piece(Piece.KING, black), e8);
        try {
            validator.validate(board, move(e8, g8));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testQueensSide() throws Exception
    {
        board.set(new Piece(Piece.KING, black), e8);
        board.set(new Piece(Piece.ROOK, black), a8);
        Move m = move(e8, c8);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        Piece king = board.get(c8), rook = board.get(d8);
        assertNotNull(king);
        assertEquals(Piece.KING, king.getType());
        assertNotNull(rook);
        assertEquals(Piece.ROOK, rook.getType());
    }

    public void testQueensSideRookAbsent()
    {
        board.set(new Piece(Piece.KING, black), e8);
        try {
            validator.validate(board, move(e8, c8));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testKingsSideWhiteRook()
    {
        board.set(new Piece(Piece.KING, black), e8);
        board.set(new Piece(Piece.ROOK, white), h8);
        try {
            validator.validate(board, move(e8, g8));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    private final Board board = new Board();
    private final Castling validator = new Castling(Colour.BLACK, new Validators());
}
