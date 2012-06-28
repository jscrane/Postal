package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;

/**
 * A BoardObserver records a move against an aspect of the position
 * of the board.
 */
interface BoardObserver
{
    /**
     * Updates the state of this observer with a validated move.
     */
    void confirm(Board board, Move move);
}
