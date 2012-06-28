package org.syzygy.chessms.action;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class TestState
	extends TestCase
{
	private final Mockery context = new Mockery();
	private final StateChangeListener listener = context.mock(StateChangeListener.class);
	private final State state = new State(listener);

	public void testGameStarted()
	{
		final String name = "gameStarted";

		context.checking(new Expectations() {{
			one(listener).stateChanged(state, name);
		}});
		state.setGameStarted(true);
		assertTrue(state.isGameStarted());
		context.assertIsSatisfied();
	}

	public void testGameEnded()
	{
		final String name = "gameEnded";

		context.checking(new Expectations() {{
			one(listener).stateChanged(state, name);
		}});
		state.setGameEnded(true);
		assertTrue(state.isGameEnded());
		context.assertIsSatisfied();
	}
}
