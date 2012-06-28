package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;

public final class Capture
    implements BoardValidator
{
    public void validate(Board board, Move move)
    {
        move.setIsCapture(board.get(move.getTo()) != null);
    }
}
        
