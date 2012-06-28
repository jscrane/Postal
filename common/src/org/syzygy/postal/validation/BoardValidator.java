package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;

/**
 * A BoardValidator validates a move against an aspect of the position
 * of the board.
 */
interface BoardValidator
{
    void validate(Board board, Move move) throws IllegalMoveException;
}
