package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;
import org.syzygy.postal.model.Square;

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
            if (moved.isA(Piece.PAWN)) {
                board.remove(to);
                board.set(Piece.queen(moved.getColour()), to);
                all.confirm(board, move);
            }
        }
    }

    private final BoardObserver all;
}
