package org.syzygy.postal.model;

public final class Piece
{
    static final class Type
    {
        private Type(String name, int value)
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

    public static Piece pawn(Colour colour)
    {
        return new Piece(PAWN, colour);
    }

    public static Piece knight(Colour colour)
    {
        return new Piece(KNIGHT, colour);
    }

    public static Piece bishop(Colour colour)
    {
        return new Piece(BISHOP, colour);
    }

    public static Piece rook(Colour colour)
    {
        return new Piece(ROOK, colour);
    }

    public static Piece queen(Colour colour)
    {
        return new Piece(QUEEN, colour);
    }

    public static Piece king(Colour colour)
    {
        return new Piece(KING, colour);
    }

    private Piece(Type type, Colour colour)
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

    public boolean is(Colour c)
    {
        return colour == c;
    }

    public Type getType()
    {
        return type;
    }

    public boolean isA(Type t)
    {
        return type == t;
    }

    public int getValue()
    {
        return type.value;
    }

    public String toString()
    {
        return is(Colour.WHITE) ? type.toString() : type.toString().toLowerCase();
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Piece))
            return false;
        Piece p = (Piece) o;
        return isA(p.type) && is(p.colour);
    }

    public int hashCode()
    {
        return 31 * type.hashCode() + colour.hashCode();
    }

    private final Colour colour;
    private final Type type;
}
