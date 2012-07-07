package org.syzygy.postal.action;

import org.syzygy.postal.Util;
import org.syzygy.postal.io.AbstractTransport;
import org.syzygy.postal.io.Completion;
import org.syzygy.postal.io.Persistence;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.ui.MainDisplay;

import java.util.Date;

public final class DistributedGameController extends VisualGameController implements PartnerCallback
{
    public DistributedGameController(MainDisplay main, StateChangeListener stateChangeListener)
    {
        super(main, Colour.BLACK);
        this.partner = new Partner(this);
        this.state = new State(stateChangeListener);
    }

    private void myTurn(boolean yes)
    {
        main.setIsMyTurn(yes);
        state.setIsMyTurn(yes);
    }

    public void onReceive(String m)
    {
        System.out.println("received: " + m);
        int sl = m.indexOf('/');
        if (state.isValidId(m.substring(0, sl))) {
            Move move = move(m.substring(sl + 1));
            if (move != null) {
                String comment = move.getComment();
                String status = Util.isBlank(comment) ? "" : comment + ". ";
                if (move.isResignation())
                    status += "You win!";
                else if (move.isCheckMate())
                    status += "Checkmate!";
                else if (move.isCheck())
                    status += "Check!";
                main.setStatus(status);
                updateState(move);
                myTurn(!move.isResignation() && !move.isCheckMate());
            }
        }
    }

    public void onSent(String m)
    {
        System.out.println("sent: " + m);
        int sl = m.indexOf('/');
        Move move = Move.valueOf(m.substring(sl + 1));
        complete(move);
        updateState(move);
        myTurn(false);
    }

    public void start(final AbstractTransport transport, final Completion completion, final boolean myTurn, final String id)
    {
        Completion c = new Completion()
        {
            public void complete(Object result)
            {
                myTurn(myTurn);
                state.setId(id);
                completion.complete(result);
            }

            public void error(Exception e)
            {
                completion.error(e);
            }
        };
        if (myTurn)
            partner.connect(transport, c);
        else
            partner.listen(transport, c);
    }

    private void updateState(Move move)
    {
        state.startGame();
        if (move.isResignation() || move.isCheckMate())
            state.gameOver();
    }

    private void sendMoveWithComment(String m, String comment)
    {
        if (!Util.isBlank(comment))
            m += " " + comment;
        Move move = validate(m);
        if (move != null) {
            if (state.getId() == null)
                state.setId(Long.toString(new Date().getTime()));
            partner.send(state.getId() + "/" + move.toString());
        }
    }

    public void processMove(String comment)
    {
        String m = main.getMove();
        if (m != null)
            sendMoveWithComment(m, comment);
    }

    public void resign(String comment)
    {
        sendMoveWithComment((main.getColour() == Colour.WHITE ? Move.WHITE_RESIGNS : Move.BLACK_RESIGNS).toString(), comment);
    }

    public boolean needsSave()
    {
        return state.isGameStarted() && !state.isGameOver();
    }

    public void save(String name, Persistence rms)
    {
        rms.save(name, getMoves(), partner.toString() + " " + main.getColour() + " " + state.getId());
    }

    private final Partner partner;
    private final State state;
}
