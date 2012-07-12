package org.syzygy.postal.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.syzygy.postal.action.SharedGameController;
import org.syzygy.postal.model.Move;

import java.util.Vector;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.LayoutParams;
import static android.widget.LinearLayout.VERTICAL;

public final class ChessBoardActivity extends Activity implements MoveListener
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(VERTICAL);
        TextView textView = new TextView(this);
        textView.setText("hello world");
        layout.addView(textView, new LayoutParams(FILL_PARENT, WRAP_CONTENT));
        BoardView boardView = new BoardView(this);
        layout.addView(boardView, new LayoutParams(FILL_PARENT, WRAP_CONTENT));
        setContentView(layout);

        controller = new SharedGameController(new AndroidMainDisplay(textView, boardView, this));
        controller.restart(new Vector(0));
    }

    private SharedGameController controller;

    public void onMove(Move move)
    {
        controller.processMove(move);
    }
}
