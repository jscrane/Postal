package org.syzygy.chessms.validation;

import java.util.Enumeration;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Square;

public interface ValidationUtils
{
    /**
     * Returns true if any of the pieces attacks the given square.
     */
    boolean isAttacked(Board board, Square s, Enumeration pieces);
}
