package org.syzygy.chessms.action;

public class State
{
	public State(StateChangeListener listener)
	{
		this.listener = listener;
	}

	public void setGameStarted(boolean gameStarted)
	{
		boolean gs = this.gameStarted;
		this.gameStarted = gameStarted;
		if (gs != gameStarted)
			listener.stateChanged(this, "gameStarted");
	}

	public boolean isGameStarted()
	{
		return gameStarted;
	}

	public boolean isMyTurn()
	{
		return isMyTurn;
	}

	public void setIsMyTurn(boolean isMyTurn)
	{
		boolean b = this.isMyTurn;
		this.isMyTurn = isMyTurn;
		if (b != isMyTurn)
			listener.stateChanged(this, "isMyTurn");
	}

	public boolean isGameEnded()
	{
		return gameEnded;
	}

	public void setGameEnded(boolean gameEnded)
	{
		boolean b = this.gameEnded;
		this.gameEnded = gameEnded;
		if (b != gameEnded)
			listener.stateChanged(this, "gameEnded");
	}

	public boolean isValidId(String i)
	{
		if (id == null) {
			setId(i);
			return true;
		}
		return id.equals(i);
	}

	public void setId(String i)
	{
		this.id = i;
		listener.stateChanged(this, "id");		
	}
	
	public String getId()
	{
		return id;
	}
	
	private boolean isMyTurn = false;
	private boolean gameStarted = false;
	private boolean gameEnded = false;
	private String id = null;
	private final StateChangeListener listener;
}
