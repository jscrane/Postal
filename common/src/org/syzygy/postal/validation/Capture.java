package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;

public final class Capture implements BoardValidator
{
    public void validate(Board board, Move move)
    {
        move.setIsCapture(board.get(move.getTo()) != null);
    }
}
        
