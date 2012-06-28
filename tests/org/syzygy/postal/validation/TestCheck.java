package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

public final class TestCheck extends BoardValidatorSupport
{
    public void testCantMoveIntoCheck()
    {
        Board board = new Board();
        board.set(new Piece(Piece.ROOK, black), a8);
        board.set(new Piece(Piece.KING, white), b1);
        try {
            check.validate(board, move(b1, a1));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testCantMoveAndStayInCheck()
    {
        Board board = new Board();
        board.set(new Piece(Piece.ROOK, black), a8);
        board.set(new Piece(Piece.PAWN, white), e2);
        board.set(new Piece(Piece.KING, white), a2);
        board.set(new Piece(Piece.KING, black), h8);
        try {
            check.validate(board, move(e2, e4));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testMoveBlocksCheck() throws Exception
    {
        Board board = new Board();
        board.set(new Piece(Piece.ROOK, black), a4);
        board.set(new Piece(Piece.PAWN, white), e2);
        board.set(new Piece(Piece.KING, white), h4);
        board.set(new Piece(Piece.KING, black), h8);
        check.validate(board, move(e2, e4));
    }

    public void testGivesCheck()
    {
        Board board = new Board();
        board.set(new Piece(Piece.ROOK, black), b8);
        board.set(new Piece(Piece.KING, white), b1);
        Move move = move(a8, b8);
        check.confirm(board, move);
        assertTrue(move.isCheck());
    }

    public void testDisclosedCheck() throws Exception
    {
        Board board = new Board();
        board.set(new Piece(Piece.KING, black), h8);
        board.set(new Piece(Piece.ROOK, black), b8);
        board.set(new Piece(Piece.BISHOP, black), b4);
        board.set(new Piece(Piece.KING, white), b1);
        Move move = move(b4, d6);
        check.validate(board, move);
        board.move(move);
        check.confirm(board, move);
        assertTrue(move.isCheck());
    }

    public void testFindKing()
    {
        Board board = Board.create();
        assertEquals(e8, check.findKing(board, board.getPieces(Colour.BLACK)));
        assertEquals(e1, check.findKing(board, board.getPieces(Colour.WHITE)));

        Board empty = new Board();
        assertNull(check.findKing(empty, empty.getPieces(Colour.BLACK)));
    }

    private final Check check = new Check(new Validators());
}
