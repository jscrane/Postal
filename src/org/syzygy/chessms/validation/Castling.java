package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Piece;
import org.syzygy.chessms.model.Square;

final class Castling
    implements BoardValidator, BoardObserver
{
    public Castling(Colour colour, ValidationUtils utils)
    {
        this.colour = colour;
	this.utils = utils;
        if (colour == Colour.WHITE) {
            this.h = new Square("h1");
            this.a = new Square("a1");
            this.e = new Square("e1");
            this.f = new Square("f1");
            this.d = new Square("d1");
        } else {
            this.h = new Square("h8");
            this.a = new Square("a8");
            this.e = new Square("e8");
            this.f = new Square("f8");
            this.d = new Square("d8");
        }
    }

    public void validate(Board board, Move move)
	throws IllegalMoveException
    {
        Square from = move.getFrom(), to = move.getTo();
        Piece k = board.get(from), kr = board.get(h), qr = board.get(a);
        if (!e.equals(from) || !k.equals(Piece.KING) || k.getColour() != colour )
            return;

        int dx = move.getDx(), dy = move.getDy();
        if (dy != 0 || (dx != 2 && dx != -2))
            return;

        if (dx == 2 && kr != null && colour == kr.getColour() && kr.equals(Piece.ROOK))
	    if (!kMoved && !krMoved && !attacked(board, from) && !attacked(board, h)) {
		Board copy = new Board(board);
		copy.move(move);
		if (!attacked(copy, to) && !attacked(copy, f))
		    return;
	    }

        if (dx == -2 && qr != null && colour == qr.getColour() && qr.equals(Piece.ROOK))
	    if (!kMoved && !qrMoved && !attacked(board, from) && !attacked(board, a)) {
		Board copy = new Board(board);
		copy.move(move);
		if (!attacked(copy, to) && !attacked(copy, d))
		    return;
	    }

	throw new IllegalMoveException("Invalid attempt to Castle");
    }

    private boolean attacked(Board b, Square s)
    {
	return utils.isAttacked(b, s, b.getOpposingPieces(colour));
    }

    public void confirm(Board board, Move move)
    {
        Piece p = board.get(move.getTo());
        if (p.getColour() == colour) {
	    if (p.equals(Piece.KING)) {
		kMoved = true;
		int dx = move.getDx();
		if (dx == 2)
		    board.set(board.remove(h), f);
		else
		    board.set(board.remove(a), d);
	    }
            if (p.equals(Piece.ROOK)) {
		Square from = move.getFrom();
                if (!qrMoved)
                    qrMoved = from.equals(h);
                if (!krMoved)
                    krMoved = from.equals(a);
            }
        }
    }

    private final Colour colour;
    private final Square h, a, e, f, d;
    private final ValidationUtils utils;
    private boolean kMoved, qrMoved, krMoved;
}
