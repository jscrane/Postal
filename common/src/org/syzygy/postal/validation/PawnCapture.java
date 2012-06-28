package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

final class PawnCapture implements BoardValidator
{
    public void validate(Board board, Move move) throws IllegalMoveException
    {
        Piece moving = board.get(move.getFrom());
        if (!moving.isA(Piece.PAWN))
            return;

        Piece opposing = board.get(move.getTo());
        if (opposing == null)
            return;

        int dx = move.getDx(), dy = move.getDy();
        if (dx < 0) dx = -dx;
        if (moving.getColour() == Colour.BLACK)
            dy = -dy;
        if (dx == 1 && dy == 1 && moving.getColour() != opposing.getColour())
            return;

        throw new IllegalMoveException("Invalid Pawn Capture");
    }
}
