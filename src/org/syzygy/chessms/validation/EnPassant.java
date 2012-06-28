package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.*;

final class EnPassant implements BoardValidator, BoardObserver
{
    public void validate(Board board, Move move) throws IllegalMoveException
    {
        Square from = move.getFrom(), to = move.getTo();
        Piece p = board.get(from), q = board.get(to);
        if (p.getType() == Piece.PAWN && q == null) {
            int dx = move.getDx(), dy = move.getDy(), d = dx * dy;
            if (d == 1 || d == -1) {
                Piece r = board.get(from.getRank(), to.getFile());
                if (!passe || r == null || p.getColour() == r.getColour())
                    throw new IllegalMoveException("Invalid en passant");
            }
        }
    }

    public void confirm(Board board, Move move)
    {
        Square from = move.getFrom(), to = move.getTo();
        Piece p = board.get(to);
        passe = false;
        if (p.getType() == Piece.PAWN) {
            int f = to.getFile(), dx = move.getDx(), dy = move.getDy(), d = dx * dy;
            if (d == 1 || d == -1) {
                int r = from.getRank();
                if ((p.getColour() == Colour.WHITE && r == 4) ||
                        (p.getColour() == Colour.BLACK && r == 3))
                    board.remove(new Square(r, f));
            } else if (dx == 0 && (dy == 2 || dy == -2)) {
                if (f > 1)
                    passe = passing(p, board.get(to.getRank(), f - 1));
                if (!passe && f < 7)
                    passe = passing(p, board.get(to.getRank(), f + 1));
            }
        }
    }

    private boolean passing(Piece p, Piece r)
    {
        return r != null && r.getType() == Piece.PAWN && r.getColour() != p.getColour();
    }

    private boolean passe = false;
}
