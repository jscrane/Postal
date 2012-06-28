package org.syzygy.postal.validation;

import org.syzygy.postal.model.*;

import java.util.Hashtable;

public final class MoveValidator implements BoardValidator
{
    public MoveValidator()
    {
        validators.put(Piece.PAWN, new Pawn());
        validators.put(Piece.ROOK, new Rook());
        validators.put(Piece.KING, new King());
        validators.put(Piece.BISHOP, new Bishop());
        validators.put(Piece.QUEEN, new Queen());
        validators.put(Piece.KNIGHT, new Knight());
    }

    public void validate(Board board, Move move)
            throws IllegalMoveException
    {
        Piece moving = board.get(move.getFrom());
        if (!validate(moving, move))
            throw new IllegalMoveException("What?");
    }

    boolean validate(Piece piece, Move move)
    {
        Square from = move.getFrom(), to = move.getTo();
        if (piece == null || from.equals(to))
            return false;
        Validator v = (Validator) validators.get(piece.getType());
        return v.validate(piece, move);
    }

    private final Hashtable validators = new Hashtable();

    static interface Validator
    {
        /**
         * Validates a Piece's move.
         *
         * @return this validator's opinion of the validity of
         *         the move: true if it considers the move valid, or false
         *         if it's invalid.
         */
        boolean validate(Piece piece, Move move);
    }

    static class Pawn
            implements Validator
    {
        public boolean validate(Piece p, Move m)
        {
            int dx = m.getDx(), dy = m.getDy();
            if (p.getColour() == Colour.BLACK)
                dy = -dy;
            if (dx == 0) {
                if (dy == 1)
                    return true;
                int f = m.getFrom().getRank();
                if (dy == 2)
                    return p.getColour() == Colour.WHITE ? f == 1 : f == 6;
            } else {
                if (dx < 0)
                    dx = -dx;
                if (dx == 1 && dy == 1)
                    return true;
            }
            return false;
        }
    }

    static final class Rook implements Validator
    {
        public boolean validate(Piece p, Move m)
        {
            return m.getDy() == 0 || m.getDx() == 0;
        }
    }

    static final class King implements Validator
    {
        public boolean validate(Piece p, Move m)
        {
            int dx = m.getDx(), dy = m.getDy();
            if (dx < 0) dx = -dx;
            if (dy < 0) dy = -dy;
            if (dx < 2 && dy < 2)
                return true;
            if (dx == 2 && dy == 0)
                return p.getColour() == Colour.WHITE && m.getFrom().toString().equals("e1") || p.getColour() == Colour.BLACK && m.getFrom().toString().equals("e8");
            return false;
        }
    }

    static final class Bishop implements Validator
    {
        public boolean validate(Piece p, Move m)
        {
            int dx = m.getDx(), dy = m.getDy();
            return dx == dy || dx == -dy;
        }
    }

    static final class Queen implements Validator
    {
        private final Validator bishop = new Bishop();
        private final Validator rook = new Rook();

        public boolean validate(Piece p, Move m)
        {
            return bishop.validate(p, m) || rook.validate(p, m);
        }
    }

    static final class Knight implements Validator
    {
        public boolean validate(Piece p, Move m)
        {
            int dx = m.getDx(), dy = m.getDy();
            if (dx < 0) dx = -dx;
            if (dy < 0) dy = -dy;
            return (dx == 1 && dy == 2) || (dy == 1 && dx == 2);
        }
    }
}
