package org.syzygy.postal.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.syzygy.postal.R;
import org.syzygy.postal.action.SharedGameController;
import org.syzygy.postal.model.Move;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public final class ChessBoardActivity extends Activity implements MoveListener
{
    private SharedGameController controller;

    public void onMove(Move move)
    {
        controller.move(move);
    }

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
        Vector<String> m = new Vector<String>();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        for (int i = 0; ; i++) {
            String mv = preferences.getString(Integer.toString(i), null);
            if (mv == null)
                break;
            m.add(mv);
        }
        controller.restart(m);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Enumeration<Move> e = controller.getMoves();
        for (int i = 0; e.hasMoreElements(); i++) {
            Move m = e.nextElement();
            if (m.isGameEnding()) {
                editor.clear();
                break;
            }
            editor.putString(Integer.toString(i), m.toString());
        }
        editor.commit();
    }
}
