package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Piece;
import org.syzygy.chessms.model.Square;

final class Queen implements BoardObserver
{
    public Queen(BoardObserver all)
    {
        this.all = all;
    }

    public void confirm(Board board, Move move)
    {
        Square to = move.getTo();
        if (to.getRank() == 7) {
            Piece moved = board.get(to);
            if (moved.getType() == Piece.PAWN) {
                board.remove(to);
                board.set(new Piece(Piece.QUEEN, moved.getColour()), to);
                all.confirm(board, move);
            }
        }
    }

    private final BoardObserver all;
}
