package org.syzygy.postal.action;

import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.ui.MainDisplay;
import org.syzygy.postal.validation.IllegalMoveException;

import java.util.Enumeration;

public class VisualGameController extends GameController
{
    VisualGameController(MainDisplay main, Colour colour)
    {
        this.main = main;
        main.setGame(getBoard(), colour, colour == Colour.WHITE);
    }

    public void resign()
    {
        move(main.getColour() == Colour.WHITE ? Move.WHITE_RESIGNS : Move.BLACK_RESIGNS);
    }

    public int makeMoves(Enumeration moves)
    {
        int n;
        for (n = 0; moves.hasMoreElements(); n++) {
            String m = (String) moves.nextElement();
            move(Move.valueOf(m.substring(m.indexOf(' ') + 1)));
        }
        return n;
    }

    public Move move(Move m)
    {
        try {
            return super.move(m);
        } catch (IllegalMoveException e) {
            main.clearMove();
        }
        return null;
    }

    public Move validate(Move m)
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
        main.setMove(getNumberOfMoves(), move);
    }

    final MainDisplay main;
}
