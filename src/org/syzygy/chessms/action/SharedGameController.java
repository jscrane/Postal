package org.syzygy.chessms.action;

import java.util.Vector;

import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.ui.MainDisplay;

public class SharedGameController
	extends VisualGameController
{
	public SharedGameController(MainDisplay main)
	{
		super(main, Colour.WHITE);
	}

	public void processMove()
	{
		if (move(main.getMove()) != null)
			main.setColour(main.getColour() == Colour.WHITE? Colour.BLACK: Colour.WHITE);
	}

	public void restart(Vector moves)
	{
		int n = makeMoves(moves.elements());
		Colour colour = (n % 2) == 0? Colour.WHITE: Colour.BLACK;
		main.setGame(getBoard(), colour, true);
	}
}
