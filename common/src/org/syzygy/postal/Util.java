package org.syzygy.postal;

public final class Util
{
    private Util()
    {
    }

    public static boolean isBlank(String s)
    {
        return s == null || "".equals(s);
    }
}
