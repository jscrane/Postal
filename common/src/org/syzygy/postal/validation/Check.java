package org.syzygy.postal.validation;

import org.syzygy.postal.model.*;

import java.util.Enumeration;

public final class Check implements BoardValidator, BoardObserver
{
    public Check(ValidationUtils utils)
    {
        this.utils = utils;
    }

    public void validate(Board board, Move move) throws IllegalMoveException
    {
        Board copy = new Board(board);
        Piece moving = copy.move(move);
        Colour c = moving.getColour();
        Enumeration mine = copy.getPieces(c), opp = copy.getOpposingPieces(c);
        Square king = findKing(copy, mine);
        if (utils.isAttacked(copy, king, opp))
            throw new IllegalMoveException("In Check");
    }

    static Square findKing(Board board, Enumeration pieces)
    {
        while (pieces.hasMoreElements()) {
            Square s = (Square) pieces.nextElement();
            if (board.get(s).isA(Piece.KING))
                return s;
        }
        return null;
    }

    private final ValidationUtils utils;

    /**
     * Marks the move as a check or not.
     */
    public void confirm(Board board, Move move)
    {
        Square to = move.getTo();
        Piece moving = board.get(to);
        Colour my = moving.getColour();
        Enumeration opp = board.getOpposingPieces(my);
        Square oppKing = findKing(board, opp);
        boolean check = utils.isAttacked(board, oppKing, board.getPieces(my));
        move.setIsCheck(check);
        if (check) {
            int f = oppKing.getFile(), r = oppKing.getRank();
            for (int i = f - 1; i <= f + 1; i++)
                for (int j = r - 1; j <= r + 1; j++)
                    if (i >= 0 && i <= 7 && j >= 0 && j <= 7)
                        try {
                            utils.runValidation(board, new Move(oppKing, new Square(j, i)));
                            return;
                        } catch (IllegalMoveException _) {
                            // can't move there
                        }
            // TODO: need to check that can't interpose a piece
            move.setIsCheckMate(true);
        }
    }
}
