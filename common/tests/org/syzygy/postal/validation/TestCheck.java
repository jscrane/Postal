package org.syzygy.postal.validation;

import junit.framework.Assert;
import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

public final class TestCheck extends BoardValidatorSupport
{
    public void testCantMoveIntoCheck()
    {
        Board board = new Board();
        board.set(Piece.rook(black), a8);
        board.set(Piece.king(white), b1);
        try {
            check.validate(board, move(b1, a1));
            Assert.fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testCantMoveAndStayInCheck()
    {
        Board board = new Board();
        board.set(Piece.rook(black), a8);
        board.set(Piece.pawn(white), e2);
        board.set(Piece.king(white), a2);
        board.set(Piece.king(black), h8);
        try {
            check.validate(board, move(e2, e4));
            Assert.fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testMoveBlocksCheck() throws Exception
    {
        Board board = new Board();
        board.set(Piece.rook(black), a4);
        board.set(Piece.pawn(white), e2);
        board.set(Piece.king(white), h4);
        board.set(Piece.king(black), h8);
        check.validate(board, move(e2, e4));
    }

    public void testGivesCheck()
    {
        Board board = new Board();
        board.set(Piece.rook(black), b8);
        board.set(Piece.king(white), b1);
        Move move = move(a8, b8);
        check.confirm(board, move);
        Assert.assertTrue(move.isCheck());
        Assert.assertFalse(move.isCheckMate());
    }

    public void testDisclosedCheck() throws Exception
    {
        Board board = new Board();
        board.set(Piece.king(black), h8);
        board.set(Piece.rook(black), b8);
        board.set(Piece.bishop(black), b4);
        board.set(Piece.king(white), b1);
        Move move = move(b4, d6);
        check.validate(board, move);
        board.move(move);
        check.confirm(board, move);
        Assert.assertTrue(move.isCheck());
        Assert.assertFalse(move.isCheckMate());
    }

    public void testCheckMate() throws Exception
    {
        Board board = new Board();
        board.set(Piece.king(white), b1);
        board.set(Piece.king(black), h8);
        board.set(Piece.pawn(black), h7);
        board.set(Piece.pawn(black), g7);
        board.set(Piece.rook(white), f1);
        Move move = move(f1, f8);
        check.validate(board, move);
        board.move(move);
        check.confirm(board, move);
        Assert.assertTrue(move.isCheck());
        Assert.assertTrue(move.isCheckMate());
    }

    public void testIsNotCheckMateIfCanCaptureAttacker() throws Exception
    {
        Board board = new Board();
        board.set(Piece.king(black), e8);
        board.set(Piece.pawn(black), f7);
        board.set(Piece.king(white), e1);
        board.set(Piece.queen(white), d1);
        Move move = move(d1, d8);
        check.validate(board, move);
        board.move(move);
        check.confirm(board, move);
        Assert.assertTrue(move.isCheck());
        Assert.assertFalse(move.isCheckMate());
    }

    public void testIsNotCheckMateIfCanBlock() throws Exception
    {
        Board board = new Board();
        board.set(Piece.king(black), e8);
        board.set(Piece.rook(black), d6);
        board.set(Piece.rook(white), a7);
        board.set(Piece.rook(white), b1);
        board.set(Piece.king(white), e1);
        Move move = move(b1, b8);
        check.validate(board, move);
        board.move(move);
        check.confirm(board, move);
        Assert.assertTrue(move.isCheck());
        Assert.assertFalse(move.isCheckMate());
    }

    public void testIsCheckMateWhenCannotCaptureAttacker() throws Exception
    {
        Board board = new Board();
        board.set(Piece.king(black), e8);
        board.set(Piece.rook(white), a7);
        board.set(Piece.rook(white), b1);
        board.set(Piece.king(white), e1);
        Move move = move(b1, b8);
        check.validate(board, move);
        board.move(move);
        check.confirm(board, move);
        Assert.assertTrue(move.isCheck());
        Assert.assertTrue(move.isCheckMate());
    }

    public void testFindKing()
    {
        Board board = Board.create();
        Assert.assertEquals(e8, Check.findKing(board, board.getPieces(Colour.BLACK)));
        Assert.assertEquals(e1, Check.findKing(board, board.getPieces(Colour.WHITE)));

        Board empty = new Board();
        Assert.assertNull(Check.findKing(empty, empty.getPieces(Colour.BLACK)));
    }

    private final Check check = new Check(new Validators());
}
