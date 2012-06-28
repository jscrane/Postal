package org.syzygy.chessms.validation;

import junit.framework.TestCase;

import org.syzygy.chessms.model.Colour;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.model.Square;

public class BoardValidatorSupport
    extends TestCase
{
    protected Move move(Square from, Square to)
    {
        return new Move(from, to);
    }

    protected Square square(String s)
    {
	return new Square(s);
    }

    protected final Colour black = Colour.BLACK;
    protected final Colour white = Colour.WHITE;

    protected final Square a1 = square("a1"), a7 = square("a7"); 
    protected final Square a2 = square("a2"), a4 = square("a4");
    protected final Square a6 = square("a6"), a8 = square("a8");
    protected final Square b4 = square("b4"), b8 = square("b8");
    protected final Square b1 = square("b1"), b2 = square("b2");
    protected final Square c1 = square("c1"), c2 = square("c2"), c3 = square("c3");
    protected final Square c7 = square("c7"), c8 = square("c8");
    protected final Square d1 = square("d1"), d2 = square("d2"), d4 = square("d4");
    protected final Square d3 = square("d3"), d8 = square("d8");
    protected final Square d5 = square("d5"), d7 = square("d7");
    protected final Square d6 = square("d6");
    protected final Square e1 = square("e1"), e8 = square("e8");
    protected final Square e2 = square("e2"), e4 = square("e4");
    protected final Square e5 = square("e5"), e6 = square("e6");
    protected final Square e3 = square("e3"), e7 = square("e7");
    protected final Square f1 = square("f1"), f3 = square("f3"), f8 = square("f8");
    protected final Square f5 = square("f5"), f7 = square("f7");
    protected final Square f4 = square("f4"), f6 = square("f6");
    protected final Square g1 = square("g1"), g8 = square("g8");
    protected final Square g5 = square("g5"), g7 = square("g7");
    protected final Square h1 = square("h1"), h7 = square("h7");
    protected final Square h4 = square("h4"), h8 = square("h8");
    protected final Square h3 = square("h3"), h6 = square("h6");
}
