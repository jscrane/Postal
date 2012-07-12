package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Square;

import java.util.Enumeration;

public interface ValidationUtils
{
    /**
     * Returns true if any of the pieces attacks the given square.
     */
    boolean isAttacked(Board board, Square s, Enumeration pieces);

    /**
     * Returns the number of pieces which can legally move to the given square
     */
    int attackerCount(Board board, Square s, Enumeration pieces);
}
