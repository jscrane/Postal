package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;

final class TurnValidator implements BoardValidator, BoardObserver
{
    public void validate(Board board, Move move) throws IllegalMoveException
    {
        if (!board.get(move.getFrom()).is(expected))
            throw new IllegalMoveException("Not your turn");
    }

    public void confirm(Board board, Move move)
    {
        this.expected = expected == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
    }

    private Colour expected = Colour.WHITE;
}
