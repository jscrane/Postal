package org.syzygy.postal.android;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Square;
import org.syzygy.postal.ui.MainDisplay;

public final class AndroidMainDisplay implements MainDisplay
{
    public AndroidMainDisplay(TextView status, final BoardView boardView, final MoveListener listener)
    {
        this.status = status;
        this.boardView = boardView;
        boardView.setFocusable(true);
        boardView.setFocusableInTouchMode(true);
        boardView.setOnTouchListener(new View.OnTouchListener()
        {
            private Square from;

            public boolean onTouch(View v, MotionEvent e)
            {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    from = boardView.getSquareAt(e.getX(), e.getY());
                } else if (e.getAction() == MotionEvent.ACTION_CANCEL) {
                    from = null;
                } else if (e.getAction() == MotionEvent.ACTION_UP && from != null) {
                    Square to = boardView.getSquareAt(e.getX(), e.getY());
                    if (to == null)
                        from = null;
                    else
                        listener.onMove(new Move(from, to));
                }
                return true;
            }
        });
    }

    public void setAdvantage(int advantage)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setStatus(String comment)
    {
        status.setText(comment);
    }

    public void setIsMyTurn(boolean yes)
    {
        this.isEditable = yes;
    }

    public void setColour(Colour colour)
    {
        boardView.setColour(colour);
    }

    public Colour getColour()
    {
        return boardView.getColour();
    }

    public void clearMove()
    {
    }

    public void setMove(int n, Move move)
    {
        int m = (n + 1) / 2;
        boolean white = (n % 2) != 0;
        String ms;
        if (white) {
            status.setText("Black to move");
            ms = Integer.toString(m);
        } else {
            status.setText("White to move");
//            ms = moveEntryItem.getLabel();
//            if (ms == null)
//                ms = Integer.toString(m) + "...";
        }
//        moveEntryItem.setLabel(ms + " " + move.getComment());
        clearMove();
        redrawBoard();
    }

    private void redrawBoard()
    {
        boardView.invalidate();
    }

    public void setGame(Board board, Colour colour, boolean isMyTurn)
    {
        this.board = board;
        boardView.setBoard(board);
        setColour(colour);
        setIsMyTurn(isMyTurn);
        redrawBoard();
    }

    private final TextView status;
    private final BoardView boardView;
    private boolean isEditable;
    private Board board;
}
