package org.syzygy.chessms.action;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.validation.IllegalMoveException;
import org.syzygy.chessms.validation.Validators;

import java.util.Enumeration;
import java.util.Vector;

public class GameController
{
    public Move validate(String m) throws IllegalMoveException
    {
        Move move;
        try {
            move = Move.valueOf(m);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalMoveException("Internal Error: " + e.getClass().getName());
        }
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
