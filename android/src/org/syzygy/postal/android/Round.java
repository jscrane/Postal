package org.syzygy.postal.android;

import org.syzygy.postal.model.Move;

public final class Round
{
    public final Move white;
    public Move black;

    public Round(Move white)
    {
        this.white = white;
    }
}
