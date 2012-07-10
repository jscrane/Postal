package org.syzygy.postal.action;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.validation.IllegalMoveException;
import org.syzygy.postal.validation.Validators;

import java.util.Enumeration;
import java.util.Vector;

public class GameController
{
    public Move validate(Move move) throws IllegalMoveException
    {
        if (!move.isResignation())
            validation.validate(board, move);
        return move;
    }

    public void complete(Move move)
    {
        if (!move.isResignation()) {
            board.move(move);
            validation.confirm(board, move);
        }
        moves.addElement(move);
    }

    public Board getBoard()
    {
        return board;
    }

    public Enumeration getMoves()
    {
        return moves.elements();
    }

    public int getNumberOfMoves()
    {
        return moves.size();
    }

    private final Board board = Board.create();
    private final Validators validation = new Validators();
    private final Vector moves = new Vector();
}
