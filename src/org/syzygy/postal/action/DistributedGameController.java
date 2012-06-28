package org.syzygy.postal.action;

import org.syzygy.postal.io.EventListener;
import org.syzygy.postal.io.RmsGame;
import org.syzygy.postal.io.Transport;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.ui.MainDisplay;

import java.util.Date;

public final class DistributedGameController extends VisualGameController implements PartnerCallback
{
    public DistributedGameController(MainDisplay main, StateChangeListener stateChangeListener, EventListener transportListener)
    {
        super(main, Colour.BLACK);
        this.partner = new Partner(this, transportListener);
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
                String status;
                String comment = move.getComment();
                if (comment == null || "".equals(comment))
                    status = move.isCheck() ? "Check!" : move.isResignation() ? "You win!" : "Your move";
                else {
                    status = comment;
                    if (move.isCheck())
                        status += ". Check!";
                }
                main.setStatus(status);
                updateState(move);
                myTurn(!move.isResignation());
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

    public void start(Transport transport, Colour colour, boolean myTurn, String id)
    {
        if (myTurn)
            partner.connect(transport);
        else
            partner.listen(transport);
        main.setGame(getBoard(), colour, myTurn);
        myTurn(myTurn);
        state.setId(id);
    }

    private void updateState(Move move)
    {
        state.startGame();
        if (move.isResignation())
            state.gameOver();
    }

    private void sendMoveWithComment(String m, String comment)
    {
        if (comment != null && !"".equals(comment))
            m += " " + comment;
        Move move = validate(m);
        if (move != null) {
            if (state.getId() == null)
                state.setId(new Long(new Date().getTime()).toString());
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
        return state.isGameStarted() && !state.isGameEnded();
    }

    public void save(String name, RmsGame rms)
    {
        rms.save(name, getMoves(), partner.toString() + " " + main.getColour() + " " + state.getId());
    }

    private final Partner partner;
    private final State state;
}
