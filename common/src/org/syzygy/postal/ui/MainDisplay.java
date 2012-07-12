package org.syzygy.postal.ui;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;

public interface MainDisplay
{
    public void setAdvantage(int advantage);

    public void setStatus(String status);

    public void setIsMyTurn(boolean isMyTurn);

    public Colour getColour();

    public void clearMove();

    public void setMove(int n, Move move);

    public void setGame(Board board, Colour colour, boolean isMyTurn);
}
