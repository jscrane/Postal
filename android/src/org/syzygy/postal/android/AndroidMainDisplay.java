package org.syzygy.postal.android;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.syzygy.postal.R;
import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.ui.MainDisplay;

import java.util.HashMap;
import java.util.Map;

public final class AndroidMainDisplay implements MainDisplay
{
    public AndroidMainDisplay(TextView status, TextView advantage, BoardView board, ArrayAdapter<Round> moves, Context context)
    {
        this.status = status;
        this.advantage = advantage;
        this.board = board;
        this.moves = moves;
        statuses.put(R.string.black_move, context.getString(R.string.black_move));
        statuses.put(R.string.black_resigns, context.getString(R.string.black_resigns));
        statuses.put(R.string.white_move, context.getString(R.string.white_move));
        statuses.put(R.string.white_resigns, context.getString(R.string.white_resigns));
    }

    public void setAdvantage(int a)
    {
        if (getColour() == Colour.BLACK)
            a = -a;
        Log.d("Postal", "advantage=" + Integer.toString(a));
        advantage.setText(Integer.toString(a));
    }

    public void setStatus(String comment)
    {
        status.setText(comment);
    }

    public void setIsMyTurn(boolean yes)
    {
    }

    public Colour getColour()
    {
        return board.getColour();
    }

    public void clearMove()
    {
    }

    public void setMove(int n, Move mv)
    {
        int m = (n + 1) / 2;
        boolean white = (n % 2) != 0;
        Colour colour;
        Round round;
        if (white) {
            colour = Colour.BLACK;
            round = new Round(mv);
        } else {
            colour = Colour.WHITE;
            round = moves.getItem(m - 1);
            moves.remove(round);
            round.black = mv;
        }
        moves.add(round);
        if (mv.isResignation())
            status.setText(statuses.get(colour == Colour.WHITE ? R.string.black_resigns : R.string.white_resigns));
        else {
            status.setText(statuses.get(colour == Colour.WHITE ? R.string.black_move : R.string.white_move));
            board.setColour(colour);
            board.invalidate();
        }
    }

    public void setGame(Board b, Colour colour, boolean isMyTurn)
    {
        moves.clear();
        board.setBoard(b);
        board.setColour(colour);
        status.setText(statuses.get(colour == Colour.WHITE ? R.string.white_move : R.string.black_move));
        setIsMyTurn(isMyTurn);
        board.invalidate();
    }

    private final TextView status, advantage;
    private final ArrayAdapter<Round> moves;
    private final BoardView board;
    private final Map<Integer, String> statuses = new HashMap<Integer, String>();
}
