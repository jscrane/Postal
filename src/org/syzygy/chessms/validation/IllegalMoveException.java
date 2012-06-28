package org.syzygy.chessms.validation;

public class IllegalMoveException
    extends Exception
{
    public IllegalMoveException(String message)
    {
    	super(message);
    }
}