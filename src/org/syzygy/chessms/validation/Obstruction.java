package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Piece;
import org.syzygy.chessms.model.Square;

final class Obstruction
    implements BoardValidator
{
    public void validate(Board board, Move move)
	throws IllegalMoveException
    {
        Square from = move.getFrom(), to = move.getTo();
        if (board.get(from).equals(Piece.KNIGHT))
            return;
        
        int sx = from.getFile(), sy = from.getRank(), ex = to.getFile(), ey = to.getRank();
        int dx = sx == ex? 0: sx > ex? -1: 1, dy = sy == ey? 0: sy > ey? -1: 1;
        if (dx == 0) {
            for (int j = sy + dy; j != ey; j += dy)
                if (board.isOccupied(j, sx))
                    throw e;
        } else if (dy == 0) {
            for (int i = sx + dx; i != ex; i += dx)
                if (board.isOccupied(sy, i))
                    throw e;
        } else
            for (int i = sx + dx, j = sy + dy; i != ex; i += dx, j += dy)
		if (board.isOccupied(j, i))
                    throw e;
    }

    private final IllegalMoveException e = new IllegalMoveException("Obstruction");
}
