package org.syzygy.postal.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.syzygy.postal.R;
import org.syzygy.postal.action.SharedGameController;
import org.syzygy.postal.model.Move;

import java.util.ArrayList;
import java.util.Vector;

public final class ChessBoardActivity extends Activity implements MoveListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView status = (TextView) findViewById(R.id.status);
        TextView advantage = (TextView) findViewById(R.id.advantage);
        BoardView board = (BoardView) findViewById(R.id.board);
        ListView moves = (ListView) findViewById(R.id.moves);

        // could use a custom adapter here which supplies its own row view
        // see http://www.vogella.com/articles/AndroidListView/article.html
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.label, new ArrayList<String>());
        moves.setAdapter(adapter);
        controller = new SharedGameController(new AndroidMainDisplay(status, advantage, board, adapter, this));
        controller.restart(new Vector(0));
    }

    private SharedGameController controller;

    public void onMove(Move move)
    {
        controller.move(move);
    }
}
