package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Piece;

public final class TestWhiteCastling extends BoardValidatorSupport
{
    public void testKingsSide() throws Exception
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), h1);
        Move m = move(e1, g1);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        Piece king = board.get(g1), rook = board.get(f1);
        assertNotNull(king);
        assertEquals(Piece.KING, king.getType());
        assertNotNull(rook);
        assertEquals(Piece.ROOK, rook.getType());
    }

    public void testKingsSideRookAbsent()
    {
        board.set(new Piece(Piece.KING, white), e1);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testQueensSide() throws Exception
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), a1);
        Move m = move(e1, c1);
        validator.validate(board, m);
        board.move(m);
        validator.confirm(board, m);
        Piece king = board.get(c1), rook = board.get(d1);
        assertNotNull(king);
        assertEquals(Piece.KING, king.getType());
        assertNotNull(rook);
        assertEquals(Piece.ROOK, rook.getType());
    }

    public void testQueensSideRookAbsent()
    {
        board.set(new Piece(Piece.KING, white), e1);
        try {
            validator.validate(board, move(e1, c1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testKingsSideBlackRook()
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, black), h1);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIfKingMoved() throws Exception
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), h1);
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
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), h1);
        board.set(new Piece(Piece.KING, black), d8);
        board.set(new Piece(Piece.ROOK, black), e8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleOutOfDanger()
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), h1);
        board.set(new Piece(Piece.KING, black), d8);
        board.set(new Piece(Piece.ROOK, black), h8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoCheckOnKingsSide()
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), h1);
        board.set(new Piece(Piece.KING, black), d8);
        board.set(new Piece(Piece.ROOK, black), g8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoDangerOnKingsSide()
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), h1);
        board.set(new Piece(Piece.KING, black), d8);
        board.set(new Piece(Piece.ROOK, black), f8);
        try {
            validator.validate(board, move(e1, g1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoCheckOnQueensSide()
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), a1);
        board.set(new Piece(Piece.KING, black), d8);
        board.set(new Piece(Piece.ROOK, black), c8);
        try {
            validator.validate(board, move(e1, c1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCantCastleIntoDangerOnQueensSide()
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), a1);
        board.set(new Piece(Piece.KING, black), c8);
        board.set(new Piece(Piece.ROOK, black), d8);
        try {
            validator.validate(board, move(e1, c1));
            fail("IllegalMoveException not thrown");
        } catch (Exception _) {
        }
    }

    public void testCastlingGivesCheck() throws Exception
    {
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.ROOK, white), a1);
        board.set(new Piece(Piece.KING, black), d8);
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
