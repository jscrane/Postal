package org.syzygy.postal.model;

/**
 * Colour is a poor-man's enum.
 */
public final class Colour
{
    public static final Colour WHITE = new Colour("White");
    public static final Colour BLACK = new Colour("Black");

    private Colour(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }

    public static Colour valueOf(String name)
    {
        if (name.equals("White"))
            return WHITE;
        if (name.equals("Black"))
            return BLACK;
        throw new IllegalArgumentException("Unknown Colour: " + name);
    }

    private final String name;
}
