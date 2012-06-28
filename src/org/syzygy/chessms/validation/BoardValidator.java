package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;

/**
 * A BoardValidator validates a move against an aspect of the position
 * of the board.
 */
interface BoardValidator
{
    void validate(Board board, Move move) throws IllegalMoveException;
}
