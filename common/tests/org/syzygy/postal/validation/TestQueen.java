package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

public final class TestQueen extends BoardValidatorSupport
{
    public void testQueen()
    {
        Board board = new Board();
        board.set(Piece.king(black), h7);
        board.set(Piece.king(white), e1);
        board.set(Piece.pawn(white), a7);
        Move move = move(a7, a8);
        board.move(move);
        queen.confirm(board, move);
        Piece p = board.get(a8);
        assertNotNull(p);
        assertTrue(p.isA(Piece.QUEEN));
    }

    public void testNewQueenGivesCheck()
    {
        Board board = new Board();
        board.set(Piece.king(black), h8);
        board.set(Piece.king(white), e1);
        board.set(Piece.pawn(white), a7);
        Move move = move(a7, a8);
        board.move(move);
        queen.confirm(board, move);
        Piece p = board.get(a8);
        assertNotNull(p);
        assertTrue(p.isA(Piece.QUEEN));
        assertTrue(move.isCheck());
    }

    public void testNewQueenChangesMaterial()
    {
        Board board = new Board();
        board.set(Piece.king(black), h8);
        board.set(Piece.king(white), e1);
        board.set(Piece.pawn(white), a7);
        assertEquals(1, board.getMaterialAdvantage());
        Move move = move(a7, a8);
        board.move(move);
        queen.confirm(board, move);
        assertEquals(8, board.getMaterialAdvantage());
    }

    private final Queen queen = new Queen(new Validators());
}
