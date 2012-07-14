package org.syzygy.postal.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView status = (TextView) findViewById(R.id.status);
        TextView advantage = (TextView) findViewById(R.id.advantage);
        final BoardView board = (BoardView) findViewById(R.id.board);
        ListView moves = (ListView) findViewById(R.id.moves);

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
        controller = new SharedGameController(new AndroidMainDisplay(status, advantage, board, adapter));

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

    // see http://www.vogella.com/articles/AndroidListView/article.html
    static class RoundAdapter extends ArrayAdapter<Round>
    {
        private final Context context;
        private final List<Round> rounds;

        RoundAdapter(Context context, List<Round> rounds)
        {
            super(context, R.layout.row, rounds);
            this.context = context;
            this.rounds = rounds;
        }

        @Override
        public View getView(int n, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
