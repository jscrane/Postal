package org.syzygy.chessms.model;

public final class Square
{
    public Square(String s)
    {
        if (s.length() > 2)
            throw new IllegalArgumentException("illegal square: " + s);
        char f = s.charAt(0);
        int r = Character.digit(s.charAt(1), 10);
        if (r < 1 || r > 8)
            throw new IllegalArgumentException("illegal rank: " + r);
        if (f < 'a' || f > 'h')
            throw new IllegalArgumentException("illegal file: " + f);
        this.rank = r - 1;
        this.file = f - 'a';
    }

    public Square(int rank, int file)
    {
        this.rank = rank;
        this.file = file;
    }

    public int getFile()
    {
        return file;
    }

    public int getRank()
    {
        return rank;
    }

    public String toString()
    {
        return new String(new char[]{ (char) ('a' + file), (char) ('1' + rank) });
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Square))
            return false;
        Square p = (Square) o;
        return p.rank == rank && p.file == file;
    }

    private final int rank, file;
}
