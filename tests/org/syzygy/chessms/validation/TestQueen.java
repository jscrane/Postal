package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Piece;

public final class TestQueen extends BoardValidatorSupport
{
    public void testQueen()
    {
        Board board = new Board();
        board.set(new Piece(Piece.KING, black), h7);
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.PAWN, white), a7);
        Move move = move(a7, a8);
        board.move(move);
        queen.confirm(board, move);
        Piece p = board.get(a8);
        assertNotNull(p);
        assertEquals(Piece.QUEEN, p.getType());
    }

    public void testNewQueenGivesCheck()
    {
        Board board = new Board();
        board.set(new Piece(Piece.KING, black), h8);
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.PAWN, white), a7);
        Move move = move(a7, a8);
        board.move(move);
        queen.confirm(board, move);
        Piece p = board.get(a8);
        assertNotNull(p);
        assertEquals(Piece.QUEEN, p.getType());
        assertTrue(move.isCheck());
    }

    public void testNewQueenChangesMaterial()
    {
        Board board = new Board();
        board.set(new Piece(Piece.KING, black), h8);
        board.set(new Piece(Piece.KING, white), e1);
        board.set(new Piece(Piece.PAWN, white), a7);
        assertEquals(1, board.getMaterialAdvantage());
        Move move = move(a7, a8);
        board.move(move);
        queen.confirm(board, move);
        assertEquals(8, board.getMaterialAdvantage());
    }

    private final Queen queen = new Queen(new Validators());
}
