package org.syzygy.chessms.action;

import java.util.Enumeration;

import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.ui.MainDisplay;
import org.syzygy.chessms.validation.IllegalMoveException;

class VisualGameController
	extends GameController
{
	protected VisualGameController(MainDisplay main, Colour colour)
	{
		this.main = main;
		main.setGame(getBoard(), colour, colour == Colour.WHITE);
	}

	public int makeMoves(Enumeration moves)
	{
		int n;
		for (n = 0; moves.hasMoreElements(); n++) {
		    String m = (String)moves.nextElement();
		    move(m.substring(m.indexOf(' ')+1));
		}
		return n;
	}
	
	protected Move move(String m)
	{
		Move move = validate(m);
		if (move == null)
			main.clearMove();
		else
			complete(move);
		return move;
	}

	public Move validate(String m)
	{
		try {
			return super.validate(m);
		} catch (IllegalMoveException e) {
			main.setStatus(e.getMessage());
		}
		return null;		
	}

	public void complete(Move move)
	{
		super.complete(move);
		if (move.isCapture())
			main.setAdvantage(getBoard().getMaterialAdvantage());
		main.setMove(getNumberOfMoves(), move.toString());	
	}

	protected final MainDisplay main;
}
