package org.syzygy.postal.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.syzygy.postal.R;
import org.syzygy.postal.action.SharedGameController;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Square;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public final class ChessBoardActivity extends Activity
{
    private SharedGameController controller;
    private AndroidMainDisplay main;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView status = (TextView) findViewById(R.id.status);
        TextView advantage = (TextView) findViewById(R.id.advantage);
        ListView moves = (ListView) findViewById(R.id.moves);
        registerForContextMenu(status);

        final BoardView board = (BoardView) findViewById(R.id.board);
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
                            if (to != null && !to.equals(from))
                                controller.move(new Move(from, to));
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

        RoundAdapter adapter = new RoundAdapter(this, new ArrayList<Round>());
        moves.setAdapter(adapter);
        main = new AndroidMainDisplay(status, advantage, board, adapter, this);

        Vector<String> m = new Vector<String>();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        for (int i = 0; ; i++) {
            String mv = preferences.getString(Integer.toString(i), null);
            Log.d("Postal", Integer.toString(i) + " " + mv);
            if (mv == null)
                break;
            m.add(mv);
        }
        startGame(m);
    }

    private void startGame(Vector moves)
    {
        controller = new SharedGameController(main);
        controller.restart(moves);
    }

    // see http://www.vogella.com/articles/AndroidListView/article.html
    static class RoundAdapter extends ArrayAdapter<Round>
    {
        private final LayoutInflater inflater;
        private final List<Round> rounds;

        RoundAdapter(Context context, List<Round> rounds)
        {
            super(context, R.layout.row, rounds);
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.rounds = rounds;
        }

        @Override
        public View getView(int n, View convertView, ViewGroup parent)
        {
            View rowView = inflater.inflate(R.layout.row, parent, false);
            TextView number = (TextView) rowView.findViewById(R.id.number);
            TextView white = (TextView) rowView.findViewById(R.id.white);
            TextView black = (TextView) rowView.findViewById(R.id.black);
            // TODO comments
//            TextView comment = (TextView) rowView.findViewById(R.id.comment);

            Round r = rounds.get(n);
            number.setText(Integer.toString(n + 1));
            white.setText(r.white.toString());
            if (r.black != null)
                black.setText(r.black.toString());

            return rowView;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        Enumeration<Move> e = controller.getMoves();
        for (int i = 0; e.hasMoreElements(); i++) {
            Move m = e.nextElement();
            Log.d("Postal", Integer.toString(i) + " " + m.toString());
            if (m.isGameEnding()) {
                editor.clear();
                break;
            }
            editor.putString(Integer.toString(i), m.toString());
        }
        editor.commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.board, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.new_game:
                startGame(new Vector());
                return true;
            case R.id.resign:
                controller.resign();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
