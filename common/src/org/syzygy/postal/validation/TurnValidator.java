package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;

final class TurnValidator implements BoardValidator, BoardObserver
{
    public void validate(Board board, Move move) throws IllegalMoveException
    {
        if (expected == null)
            throw new IllegalMoveException("The game is over");

        if ((!move.isResignation() && !board.get(move.getFrom()).is(expected)) || unexpectedResignation(move))
            throw new IllegalMoveException("Not your turn");
    }

    private boolean unexpectedResignation(Move move)
    {
        return move.isResignation() && ((expected == Colour.WHITE && move.equals(Move.BLACK_RESIGNS)) || (expected == Colour.BLACK && move.equals(Move.WHITE_RESIGNS)));
    }

    public void confirm(Board board, Move move)
    {
        this.expected = move.isGameEnding() ? null : expected == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
    }

    private Colour expected = Colour.WHITE;
}
