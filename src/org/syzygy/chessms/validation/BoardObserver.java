package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;

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
