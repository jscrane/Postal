package org.syzygy.postal.action;

import org.syzygy.postal.model.Colour;
import org.syzygy.postal.ui.MainDisplay;

import java.util.Vector;

public final class SharedGameController extends VisualGameController
{
    public SharedGameController(MainDisplay main)
    {
        super(main, Colour.WHITE);
    }

    public void restart(Vector moves)
    {
        int n = makeMoves(moves.elements());
        Colour colour = (n % 2) == 0 ? Colour.WHITE : Colour.BLACK;
        main.setGame(getBoard(), colour, true);
    }
}
