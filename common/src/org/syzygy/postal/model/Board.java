package org.syzygy.postal.model;

import java.util.Enumeration;
import java.util.Vector;

public final class Board
{
    public Board()
    {
    }

    public Board(Board board)
    {
        for (Enumeration e = board.getPieces(Colour.WHITE); e.hasMoreElements(); ) {
            Square s = (Square) e.nextElement();
            set(board.get(s), s);
        }
        for (Enumeration e = board.getPieces(Colour.BLACK); e.hasMoreElements(); ) {
            Square s = (Square) e.nextElement();
            set(board.get(s), s);
        }
    }

    public static Board create()
    {
        Board b = new Board();
        Colour wh = Colour.WHITE, bl = Colour.BLACK;

        for (char file = 'a'; file <= 'h'; file++) {
            b.set(Piece.pawn(wh), new Square("" + file + "2"));
            b.set(Piece.pawn(bl), new Square("" + file + "7"));
        }

        b.set(Piece.rook(wh), new Square("a1"));
        b.set(Piece.rook(bl), new Square("a8"));
        b.set(Piece.rook(wh), new Square("h1"));
        b.set(Piece.rook(bl), new Square("h8"));

        b.set(Piece.knight(wh), new Square("b1"));
        b.set(Piece.knight(bl), new Square("b8"));
        b.set(Piece.knight(wh), new Square("g1"));
        b.set(Piece.knight(bl), new Square("g8"));

        b.set(Piece.bishop(wh), new Square("c1"));
        b.set(Piece.bishop(bl), new Square("c8"));
        b.set(Piece.bishop(wh), new Square("f1"));
        b.set(Piece.bishop(bl), new Square("f8"));

        b.set(Piece.queen(wh), new Square("d1"));
        b.set(Piece.queen(bl), new Square("d8"));

        b.set(Piece.king(wh), new Square("e1"));
        b.set(Piece.king(bl), new Square("e8"));

        return b;
    }

    public Piece move(Move m)
    {
        Piece p = remove(m.getFrom());
        set(p, m.getTo());
        return p;
    }

    public void set(Piece p, Square s)
    {
        Piece q = get(s);
        squares[8 * s.getRank() + s.getFile()] = p;
        if (p == null) {
            if (!white.removeElement(s))
                black.removeElement(s);
        } else {
            if (q != null)
                if (!white.removeElement(s))
                    black.removeElement(s);
            if (p.getColour() == Colour.WHITE)
                white.addElement(s);
            else
                black.addElement(s);
        }
    }

    public Piece remove(Square s)
    {
        Piece p = get(s);
        set(null, s);
        return p;
    }

    public Piece get(Square p)
    {
        return get(p.getRank(), p.getFile());
    }

    public Piece get(int rank, int file)
    {
        return squares[8 * rank + file];
    }

    public boolean isOccupied(int rank, int file)
    {
        return get(rank, file) != null;
    }

    private String getAsString(int rank, int file)
    {
        Piece p = get(rank, file);
        if (p != null)
            return p.toString();
        int s = (rank % 2) + (file % 2);
        return (s == 0 || s == 2) ? "_" : " ";
    }

    public String toString()
    {
        StringBuffer board = new StringBuffer();
        for (int r = 8; r-- > 0; ) {
            for (int f = 0; f < 8; f++)
                board.append(getAsString(r, f));
            board.append("\n");
        }
        return board.toString();
    }

    public Enumeration getPieces(Colour c)
    {
        return c == Colour.WHITE ? white.elements() : black.elements();
    }

    public Enumeration getOpposingPieces(Colour c)
    {
        return c == Colour.WHITE ? black.elements() : white.elements();
    }

    /**
     * Returns the material advantage for white based on pieces' types' values.
     */
    public int getMaterialAdvantage()
    {
        int a = 0;
        for (Enumeration e = getPieces(Colour.WHITE); e.hasMoreElements(); ) {
            Square s = (Square) e.nextElement();
            Piece p = get(s);
            a += p.getValue();
        }
        for (Enumeration e = getPieces(Colour.BLACK); e.hasMoreElements(); ) {
            Square s = (Square) e.nextElement();
            Piece p = get(s);
            a -= p.getValue();
        }
        return a;
    }

    private final Piece squares[] = new Piece[64];
    private final Vector white = new Vector(), black = new Vector();
}
