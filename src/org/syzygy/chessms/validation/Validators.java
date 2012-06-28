package org.syzygy.chessms.validation;

import java.util.Enumeration;
import java.util.Vector;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Square;

public class Validators
    implements BoardValidator, BoardObserver, ValidationUtils
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
		runValidation(board, new Move((Square)pieces.nextElement(), s));
		return true;
	    } catch (IllegalMoveException e) {}
	return false;
    }

    private void runValidation(Board board, Move move)
	throws IllegalMoveException
    {
        for (Enumeration e = validators.elements(); e.hasMoreElements(); ) {
            Object o = e.nextElement();
            if (o instanceof BoardValidator)
                ((BoardValidator)o).validate(board, move);
        }
    }

    public void validate(Board board, Move move)
	throws IllegalMoveException
    {
	turn.validate(board, move);
	runValidation(board, move);
    }

    public void confirm(Board board, Move move)
    {
	turn.confirm(board, move);
        for (Enumeration e = validators.elements(); e.hasMoreElements(); ) {
            Object o = e.nextElement();
            if (o instanceof BoardObserver)
                ((BoardObserver)o).confirm(board, move);
        }
    }
    
    private final TurnValidator turn = new TurnValidator();
    private final Vector validators = new Vector();
}
