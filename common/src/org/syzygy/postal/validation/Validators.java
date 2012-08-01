package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Square;

import java.util.Enumeration;
import java.util.Vector;

public class Validators implements BoardValidator, BoardObserver, ValidationUtils
{
    public Validators()
    {
        validators.addElement(new MoveValidator());
        validators.addElement(new PawnCapture());
        validators.addElement(new Castling(Colour.WHITE, this));
        validators.addElement(new Castling(Colour.BLACK, this));
        validators.addElement(new Obstruction());
        validators.addElement(new Capture());
        validators.addElement(new EnPassant());
        validators.addElement(new Check(this));
        validators.addElement(new Queen(this));
    }

    public boolean isAttacked(Board board, Square s, Enumeration pieces)
    {
        while (pieces.hasMoreElements())
            try {
                runValidation(board, new Move((Square) pieces.nextElement(), s));
                return true;
            } catch (IllegalMoveException e) {
                // ignored
            }
        return false;
    }

    public int attackerCount(Board board, Square s, Enumeration pieces)
    {
        int n = 0;
        while (pieces.hasMoreElements())
            try {
                runValidation(board, new Move((Square) pieces.nextElement(), s));
                n++;
            } catch (IllegalMoveException e) {
                // ignored
            }
        return n;
    }

    public void runValidation(Board board, Move move) throws IllegalMoveException
    {
        for (Enumeration e = validators.elements(); e.hasMoreElements(); ) {
            Object o = e.nextElement();
            if (o instanceof BoardValidator)
                ((BoardValidator) o).validate(board, move);
        }
    }

    public void validate(Board board, Move move) throws IllegalMoveException
    {
        turn.validate(board, move);
        if (!move.isResignation())
            runValidation(board, move);
    }

    public void confirm(Board board, Move move)
    {
        turn.confirm(board, move);
        if (!move.isResignation())
            for (Enumeration e = validators.elements(); e.hasMoreElements(); ) {
                Object o = e.nextElement();
                if (o instanceof BoardObserver)
                    ((BoardObserver) o).confirm(board, move);
            }
    }

    private final TurnValidator turn = new TurnValidator();
    private final Vector validators = new Vector();
}
