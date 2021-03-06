package org.syzygy.postal;

import org.syzygy.postal.action.DistributedGameController;
import org.syzygy.postal.action.State;
import org.syzygy.postal.action.StateChangeListener;
import org.syzygy.postal.io.AbstractTransport;
import org.syzygy.postal.io.Completion;
import org.syzygy.postal.io.Filer;
import org.syzygy.postal.io.Persistence;
import org.syzygy.postal.io.midp.MidpFiler;
import org.syzygy.postal.io.midp.RmsPersistence;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.ui.midp.DefaultCommand;
import org.syzygy.postal.ui.midp.MainCanvas;

import javax.microedition.lcdui.*;
import java.util.Vector;

abstract class TwoPlayerMIDlet extends PauseableMIDlet
{
    TwoPlayerMIDlet()
    {
        this.display = Display.getDisplay(this);
        this.name = getAppProperty("org.syzygy.postal.name");
        this.filer = new MidpFiler(getAppProperty("midlet.directory"));
        StateChangeListener stateListener = new StateChangeListener()
        {
            public void stateChanged(State state, String change)
            {
                if ("gameStarted".equals(change)) {
                    if (state.isGameStarted())
                        playing();
                } else if ("isMyTurn".equals(change)) {
                    if (state.isMyTurn()) {
                        main.addCommand(move);
                        main.addCommand(resign);
                    } else {
                        main.removeCommand(move);
                        main.removeCommand(resign);
                    }
                } else if ("gameEnded".equals(change)) {
                    main.removeCommand(comment);
                    main.removeCommand(move);
                    main.removeCommand(resign);
                }
            }
        };
        this.eventListener = new EventListener()
        {
            public void onEvent(String eventClass, Object event)
            {
                Alert alert = new Alert(eventClass, event.toString(), null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                alert.setCommandListener(new CommandListener()
                {
                    public void commandAction(Command c, Displayable d)
                    {
                        saveDialog.removeCommand(cancel);
                        tryQuit();
                    }
                });
                display.setCurrent(alert);
            }
        };
        this.controller = new DistributedGameController(main, stateListener);
    }

    protected void destroyApp(boolean unconditional)
    {
        notifyDestroyed();
    }

    /**
     * Called from one of the find() methods below with a transport for communicating with an opponent.
     */
    protected void setTransport(final AbstractTransport transport)
    {
        if (isNewGame && transport.isConnected()) {
            colour = Colour.WHITE;
            isMyTurn = true;
        }
        final boolean myTurn = isMyTurn;
        Completion c = new Completion()
        {
            public void complete(Object result)
            {
                main.setGame(controller.getBoard(), colour, myTurn);
            }

            public void error(Exception e)
            {
                transport.close();
                eventListener.onEvent(transport.toString(), e);
            }
        };
        controller.start(transport, c, isMyTurn, id);
        display.setCurrent(main);
    }

    /**
     * Finds an opponent for a new game to play either black or white.
     */
    protected abstract void findOpponent(Display display);

    /**
     * Finds an opponent for an existing game. If the information can be determined from the url, it should be.
     */
    protected abstract void findExistingOpponent(Display display, String url, boolean toMove);

    void starting()
    {
        main.addCommand(quit);

        Vector moves = new Vector();
        String details = rms.load(name, moves);
        if (details == null) {
            this.isNewGame = true;
            findOpponent(display);
        } else {
            int n = controller.makeMoves(moves.elements());
            int space = details.indexOf(' ');
            String url = details.substring(0, space);
            details = details.substring(space + 1);
            space = details.indexOf(' ');
            this.colour = Colour.valueOf(details.substring(0, space));
            this.id = details.substring(space + 1);
            this.isMyTurn = ((n % 2) == 0 && colour == Colour.WHITE) || ((n % 2) != 0 && colour == Colour.BLACK);
            findExistingOpponent(display, url, isMyTurn);
            playing();
        }

        exportDialog.addCommand(ok);
        exportDialog.addCommand(cancel);
        exportDialog.setCommandListener(new CommandListener()
        {
            public void commandAction(Command c, Displayable d)
            {
                if (c == ok)
                    filer.save(fileName.getString(), controller.getMoves(), new Completion()
                    {
                        public void complete(Object result)
                        {
                        }

                        public void error(Exception e)
                        {
                            eventListener.onEvent("Save", e.getMessage());
                        }
                    });
                display.setCurrent(main);
            }
        });

        final Command yes = new Command("Yes", Command.SCREEN, 1);
        final Command no = new Command("No", Command.SCREEN, 1);
        saveDialog.addCommand(yes);
        saveDialog.addCommand(no);
        saveDialog.addCommand(cancel);
        saveDialog.setCommandListener(new CommandListener()
        {
            public void commandAction(Command c, Displayable d)
            {
                if (c == cancel)
                    display.setCurrent(main);
                else {
                    if (c == yes)
                        controller.save(name, rms);
                    destroyApp(false);
                }
            }
        });

        commentText.addCommand(ok);
        commentText.addCommand(cancel);
        commentText.setCommandListener(new CommandListener()
        {
            public void commandAction(Command c, Displayable d)
            {
                if (c == cancel)
                    commentText.setString(null);
                display.setCurrent(main);
            }
        });
    }

    private void playing()
    {
        main.addCommand(comment);
    }

    private String getAndClearComment()
    {
        String comment = commentText.getString();
        commentText.setString(null);
        return comment;
    }

    private void tryQuit()
    {
        if (controller.needsSave())
            display.setCurrent(saveDialog);
        else
            destroyApp(false);
    }

    final Command ok = new Command("OK", Command.SCREEN, 1);
    final Command cancel = new Command("Cancel", Command.SCREEN, 1);

    private final Command move = new Command("Move", Command.SCREEN, 1);
    private final Command comment = new Command("Comment", Command.SCREEN, 1);
    private final Command resign = new Command("Resign", Command.SCREEN, 1);
    private final Command export = new Command("Export", Command.SCREEN, 2);
    private final Command quit = new Command("Quit", Command.SCREEN, 3);

    private final MainCanvas main = new MainCanvas(new DefaultCommand(move, new CommandListener()
    {
        public void commandAction(Command c, Displayable disp)
        {
            if (c == quit)
                tryQuit();
            else if (c == move) {
                Move m = main.getMove();
                if (m != null)
                    controller.sendMoveWithComment(m, getAndClearComment());
            } else if (c == export)
                display.setCurrent(exportDialog);
            else if (c == comment)
                display.setCurrent(commentText);
            else if (c == resign)
                controller.resign(getAndClearComment());
        }
    }));

    protected final EventListener eventListener;

    private final Display display;
    private final String name;
    private final Filer filer;
    private final Persistence rms = new RmsPersistence();
    private final TextField fileName = new TextField(null, null, 64, TextField.ANY);
    private final Screen exportDialog = new Form("Export PGN...", new Item[]{ fileName });
    private final Screen saveDialog = new Alert("Save...", "Save game for next time?", null, AlertType.CONFIRMATION);
    private final TextBox commentText = new TextBox("Send Comment", null, 150, TextField.ANY);
    private final DistributedGameController controller;

    private Colour colour = Colour.BLACK;
    private boolean isMyTurn;
    private boolean isNewGame;
    private String id;
}
