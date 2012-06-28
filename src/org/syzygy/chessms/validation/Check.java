package org.syzygy.chessms.validation;

import java.util.Enumeration;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Piece;
import org.syzygy.chessms.model.Square;

public class Check
	implements BoardValidator, BoardObserver
{
	public Check(ValidationUtils utils)
	{
		this.utils = utils;
	}

	public void validate(Board board, Move move)
		throws IllegalMoveException
	{
		Board copy = new Board(board);
		Piece moving = copy.move(move);
		Colour c = moving.getColour();
		Enumeration mine = copy.getPieces(c), opp = copy.getOpposingPieces(c);
		Square king = findKing(copy, mine);
		if (utils.isAttacked(copy, king, opp))
			throw new IllegalMoveException("In Check");
	}

	Square findKing(Board board, Enumeration pieces)
	{
		while (pieces.hasMoreElements()) {
			Square s = (Square)pieces.nextElement();
			if (board.get(s).equals(Piece.KING))
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
		move.setIsCheck(utils.isAttacked(board, oppKing, board.getPieces(my)));
	}
}
