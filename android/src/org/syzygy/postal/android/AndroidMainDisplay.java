package org.syzygy.postal.android;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Square;
import org.syzygy.postal.ui.MainDisplay;

public final class AndroidMainDisplay implements MainDisplay
{
    public AndroidMainDisplay(TextView status, TextView advantage, final BoardView board, ArrayAdapter<Round> moves, final MoveListener listener)
    {
        this.status = status;
        this.advantage = advantage;
        this.board = board;
        this.moves = moves;
        board.setFocusable(true);
        board.setFocusableInTouchMode(true);
        board.setOnTouchListener(new View.OnTouchListener()
        {
            private Square from;

            public boolean onTouch(View v, MotionEvent e)
            {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        from = board.getSquareAt(e.getX(), e.getY());
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        from = null;
                        board.setHighlight(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (from != null) {
                            Square to = board.getSquareAt(e.getX(), e.getY());
                            if (to != null)
                                listener.onMove(new Move(from, to));
                            from = null;
                            board.setHighlight(null);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (from != null)
                            board.setHighlight(board.getSquareAt(e.getX(), e.getY()));
                        break;
                }
                return true;
            }
        });
    }

    public void setAdvantage(int a)
    {
        if (getColour() == Colour.BLACK)
            a = -a;
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
            status.setText("Black to move");
            round = new Round(mv);
        } else {
            colour = Colour.WHITE;
            status.setText("White to move");
            round = moves.getItem(m - 1);
            moves.remove(round);
            round.black = mv;
        }
        moves.add(round);
        board.setColour(colour);
        board.invalidate();
    }

    public void setGame(Board b, Colour colour, boolean isMyTurn)
    {
        board.setBoard(b);
        board.setColour(colour);
        setIsMyTurn(isMyTurn);
        board.invalidate();
    }

    private final TextView status, advantage;
    private final ArrayAdapter<Round> moves;
    private final BoardView board;
}
