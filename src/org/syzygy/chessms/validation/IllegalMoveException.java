package org.syzygy.chessms.validation;

public final class IllegalMoveException extends Exception
{
    public IllegalMoveException(String message)
    {
        super(message);
    }
}