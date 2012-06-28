package org.syzygy.chessms.action;

import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;

public final class TestState extends TestCase
{
    private final Mockery context = new Mockery();
    private final StateChangeListener listener = context.mock(StateChangeListener.class);
    private final State state = new State(listener);

    public void testGameStarted()
    {
        final String name = "gameStarted";

        context.checking(new Expectations()
        {{
                one(listener).stateChanged(state, name);
            }});
        state.startGame();
        assertTrue(state.isGameStarted());
        context.assertIsSatisfied();
    }

    public void testGameEnded()
    {
        final String name = "gameEnded";

        context.checking(new Expectations()
        {{
                one(listener).stateChanged(state, name);
            }});
        state.gameOver();
        assertTrue(state.isGameEnded());
        context.assertIsSatisfied();
    }
}
