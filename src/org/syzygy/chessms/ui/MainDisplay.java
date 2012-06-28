package org.syzygy.chessms.ui;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;

public interface MainDisplay
{
    public void setAdvantage(int advantage);

    public void setStatus(String status);

    public void setIsMyTurn(boolean isMyTurn);

    public void setColour(Colour colour);

    public Colour getColour();

    public String getMove();
    
    public void clearMove();

    public void setMove(int n, String move);

    public void setGame(Board board, Colour colour, boolean isMyTurn);
}
