package org.syzygy.chessms.model;

public final class Piece
{
    static final class Type
    {
        Type(String name, int value)
        {
            this.name = name;
            this.value = value;
            next++;
        }

        public int hashCode()
        {
            return next;
        }

        public String toString()
        {
            return name;
        }

        public int getValue()
        {
            return value;
        }

        private final String name;
        private final int value;
        private static int next = 0;
    }

    public static final Type PAWN = new Type("P", 1);
    public static final Type KNIGHT = new Type("N", 2);
    public static final Type BISHOP = new Type("B", 3);
    public static final Type ROOK = new Type("R", 5);
    public static final Type QUEEN = new Type("Q", 8);
    public static final Type KING = new Type("K", 0);

    public Piece(Type type, Colour colour)
    {
        this.type = type;
        this.colour = colour;
    }

    public Piece(char c)
    {
        this.colour = c >= 'A' && c <= 'Z' ? Colour.WHITE : Colour.BLACK;
        switch (c) {
            case 'P':
            case 'p':
                this.type = PAWN;
                break;
            case 'N':
            case 'n':
                this.type = KNIGHT;
                break;
            case 'B':
            case 'b':
                this.type = BISHOP;
                break;
            case 'R':
            case 'r':
                this.type = ROOK;
                break;
            case 'Q':
            case 'q':
                this.type = QUEEN;
                break;
            case 'K':
            case 'k':
                this.type = KING;
                break;
            default:
                throw new IllegalArgumentException("invalid piece: " + c);
        }
    }

    public Colour getColour()
    {
        return colour;
    }

    public Type getType()
    {
        return type;
    }

    public String toString()
    {
        return getColour() == Colour.WHITE ? type.toString() : type.toString().toLowerCase();
    }

    public boolean isA(Type t)
    {
        return getType() == t;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Piece) && !(o instanceof Type))
            return false;
        Type t;
        if (o instanceof Piece) {
            Piece p = (Piece) o;
            t = p.getType();
        } else
            t = (Type) o;
        return t == getType();
    }

    public int hashCode()
    {
        return 31 * type.hashCode() + colour.hashCode();
    }

    private final Colour colour;
    private final Type type;
}
