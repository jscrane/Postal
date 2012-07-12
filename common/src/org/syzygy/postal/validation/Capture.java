package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;

public final class Capture implements BoardValidator
{
    public void validate(Board board, Move move) throws IllegalMoveException
    {
        Colour my = board.get(move.getFrom()).getColour();
        Piece p = board.get(move.getTo());
        if (p != null) {
            if (p.is(my))
                throw new IllegalMoveException("Cannot capture own colour!");
            move.setIsCapture(true);
        }
    }
}
        
