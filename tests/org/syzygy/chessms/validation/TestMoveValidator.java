package org.syzygy.chessms.validation;

import org.syzygy.chessms.model.Piece;

public class TestMoveValidator
	extends BoardValidatorSupport
{
	public void testNoMove()
	{
		Piece pawn = new Piece(Piece.PAWN, white);
		assertFalse(v.validate(pawn, move(e2, e2)));
	}

	public void testValidateWhitePawnMove()
	{
		Piece pawn = new Piece(Piece.PAWN, white);
		assertTrue(v.validate(pawn, move(e2, e4)));
		assertTrue(v.validate(pawn, move(e5, f6)));
		assertFalse(v.validate(pawn, move(e2, f4)));
		assertFalse(v.validate(pawn, move(e4, e2)));
		assertFalse(v.validate(pawn, move(e4, e6)));
		assertFalse(v.validate(pawn, move(e4, e8)));
		assertFalse(v.validate(pawn, move(e2, e5)));
	}

	public void testValidateWhiteEnPassant()
	{
		Piece pawn = new Piece(Piece.PAWN, white);
		assertTrue(v.validate(pawn, move(e6, f7)));
		assertTrue(v.validate(pawn, move(e6, d7)));
	}

	public void testValidateBlackPawnMove()
	{
		Piece pawn = new Piece(Piece.PAWN, black);
		assertTrue(v.validate(pawn, move(e7, e5)));
		assertTrue(v.validate(pawn, move(e4, f3)));
		assertFalse(v.validate(pawn, move(e7, f5)));
		assertFalse(v.validate(pawn, move(e5, e7)));
		assertFalse(v.validate(pawn, move(e5, e3)));
		assertFalse(v.validate(pawn, move(e5, e1)));
	}

	public void testValidateBlackEnPassant()
	{
		Piece pawn = new Piece(Piece.PAWN, black);
		assertTrue(v.validate(pawn, move(e4, d3)));
		assertTrue(v.validate(pawn, move(e4, f3)));
	}

	public void testValidateRookMove()
	{
		Piece rook = new Piece(Piece.ROOK, white);
		assertTrue(v.validate(rook, move(a1, a8)));
		assertTrue(v.validate(rook, move(a1, h1)));
		assertFalse(v.validate(rook, move(a1, b2)));
	}

	public void testValidateKingMove()
	{
		Piece king = new Piece(Piece.KING, black);
		assertTrue(v.validate(king, move(e4, e5)));
		assertTrue(v.validate(king, move(e5, e4)));
		assertTrue(v.validate(king, move(e4, d5)));
		assertTrue(v.validate(king, move(e4, d3)));
		assertFalse(v.validate(king, move(e4, e6)));
		assertFalse(v.validate(king, move(e4, d2)));
	}

	public void testValidateKingCastle()
	{
		Piece king = new Piece(Piece.KING, black);
		assertTrue(v.validate(king, move(e8, g8)));
		assertFalse(v.validate(king, move(d8, f8)));
		assertTrue(v.validate(king, move(e8, c8)));
		assertFalse(v.validate(king, move(e7, c7)));
	}

	public void testValidateBishopMove()
	{
		Piece bishop = new Piece(Piece.BISHOP, white);
		assertTrue(v.validate(bishop, move(a1, h8)));
		assertTrue(v.validate(bishop, move(h1, a8)));
		assertTrue(v.validate(bishop, move(a8, h1)));
		assertTrue(v.validate(bishop, move(g7, b2)));
		assertFalse(v.validate(bishop, move(e2, e4)));
		assertFalse(v.validate(bishop, move(e4, e2)));
	}

	public void testValidateQueenMove()
	{
		Piece queen = new Piece(Piece.QUEEN, black);
		assertTrue(v.validate(queen, move(a1, h8)));
		assertTrue(v.validate(queen, move(h1, a8)));
		assertTrue(v.validate(queen, move(a8, h1)));
		assertTrue(v.validate(queen, move(g7, b2)));
		assertTrue(v.validate(queen, move(a1, a8)));
		assertTrue(v.validate(queen, move(a1, h1)));
		assertFalse(v.validate(queen, move(a1, c2)));
		assertFalse(v.validate(queen, move(h8, a2)));
	}

	public void testValidateKnightMove()
	{
		Piece kn = new Piece(Piece.KNIGHT, white);
		assertTrue(v.validate(kn, move(b1, c3)));
		assertTrue(v.validate(kn, move(g1, f3)));
		assertTrue(v.validate(kn, move(b8, a6)));
		assertTrue(v.validate(kn, move(g8, h6)));
		assertFalse(v.validate(kn, move(a1, h1)));
		assertFalse(v.validate(kn, move(g7, b2)));
	}

	private final MoveValidator v = new MoveValidator();
}
