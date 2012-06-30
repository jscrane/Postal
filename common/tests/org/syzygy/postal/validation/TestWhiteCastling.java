package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

public final class TestWhiteCastling extends BoardValidatorSupport
{
    public void testKingsSide() throws Exception
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), h1);
        Move m = move(e1, g1);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        Piece king = board.get(g1), rook = board.get(f1);
        assertNotNull(king);
        assertTrue(king.isA(Piece.KING));
        assertNotNull(rook);
        assertTrue(rook.isA(Piece.ROOK));
    }

    public void testKingsSideRookAbsent()
    {
        board.set(Piece.king(white), e1);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testQueensSide() throws Exception
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), a1);
        Move m = move(e1, c1);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        Piece king = board.get(c1), rook = board.get(d1);
        assertNotNull(king);
        assertTrue(king.isA(Piece.KING));
        assertNotNull(rook);
        assertTrue(rook.isA(Piece.ROOK));
    }

    public void testQueensSideRookAbsent()
    {
        board.set(Piece.king(white), e1);
        try {
            validator.validate(board, move(e1, c1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testKingsSideBlackRook()
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(black), h1);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIfKingMoved() throws Exception
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), h1);
        Move m = move(e1, e2);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        m = move(e2, e1);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleOutOfCheck()
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), h1);
        board.set(Piece.king(black), d8);
        board.set(Piece.rook(black), e8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleOutOfDanger()
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), h1);
        board.set(Piece.king(black), d8);
        board.set(Piece.rook(black), h8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoCheckOnKingsSide()
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), h1);
        board.set(Piece.king(black), d8);
        board.set(Piece.rook(black), g8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoDangerOnKingsSide()
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), h1);
        board.set(Piece.king(black), d8);
        board.set(Piece.rook(black), f8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoCheckOnQueensSide()
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), a1);
        board.set(Piece.king(black), d8);
        board.set(Piece.rook(black), c8);
        try {
            validator.validate(board, move(e1, c1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoDangerOnQueensSide()
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), a1);
        board.set(Piece.king(black), c8);
        board.set(Piece.rook(black), d8);
        try {
            validator.validate(board, move(e1, c1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCastlingGivesCheck() throws Exception
    {
        board.set(Piece.king(white), e1);
        board.set(Piece.rook(white), a1);
        board.set(Piece.king(black), d8);
        Move m = move(e1, c1);

        Validators all = new Validators();
        all.validate(board, m);
        board.move(m);
        all.confirm(board, m);
        assertTrue(m.isCheck());
    }

    private final Board board = new Board();
    private final Castling validator = new Castling(Colour.WHITE, new Validators());
}
