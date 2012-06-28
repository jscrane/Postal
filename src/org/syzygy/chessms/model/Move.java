package org.syzygy.chessms.model;

public final class Move
{
    private Move()
    {
        this(null, null);
    }

    public Move(Square from, Square to)
    {
        this(from, to, null);
    }

    public Move(Square from, Square to, String comment)
    {
        this.from = from;
        this.to = to;
        this.comment = comment;
    }

    public Square getFrom()
    {
        return from;
    }

    public Square getTo()
    {
        return to;
    }

    public int getDx()
    {
        return to.getFile() - from.getFile();
    }

    public int getDy()
    {
        return to.getRank() - from.getRank();
    }

    public String getComment()
    {
        return comment;
    }

    public boolean isCheck()
    {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck)
    {
        this.isCheck = isCheck;
    }

    public boolean isCapture()
    {
        return isCapture;
    }

    public void setIsCapture(boolean isCapture)
    {
        this.isCapture = isCapture;
    }

    public boolean isResignation()
    {
        return this == WHITE_RESIGNS || this == BLACK_RESIGNS;
    }

    public String toString()
    {
        if (this == WHITE_RESIGNS)
            return "0-1";
        if (this == BLACK_RESIGNS)
            return "1-0";

        String s = from.toString();
        s += isCapture() ? "x" : "-";
        s += to.toString();
        if (isCheck())
            s += "+";
        return comment == null ? s : s + " " + comment;
    }

    public static Move valueOf(String s)
    {
        if ("0-1".equals(s))
            return WHITE_RESIGNS;
        if ("1-0".equals(s))
            return BLACK_RESIGNS;

        if (s.length() < 5)
            throw new IllegalArgumentException(s);
        Square from = new Square(s.substring(0, 2)), to;
        boolean isCapture = s.charAt(2) == 'x', isCheck = false;
        String comment = null;
        int space = s.indexOf(' ');
        if (space == -1)
            to = new Square(s.substring(3));
        else {
            comment = s.substring(space + 1);
            to = new Square(s.substring(3, 5));
            isCheck = s.charAt(5) == '+';
        }
        Move move = new Move(from, to, comment);
        move.setIsCapture(isCapture);
        move.setIsCheck(isCheck);
        return move;
    }

    public static final Move WHITE_RESIGNS = new Move();
    public static final Move BLACK_RESIGNS = new Move();

    private final Square from, to;
    private final String comment;
    private boolean isCheck, isCapture;
}
