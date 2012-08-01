package org.syzygy.postal.validation;

import junit.framework.TestCase;
import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;

public final class TestValidators extends TestCase
{
    public void testWhiteOpeningE2E4() throws Exception
    {
        v.validate(board, Move.valueOf("e2-e4"));
    }

    public void testWhiteResigns() throws Exception
    {
        Move move = Move.valueOf("0-1");
        v.validate(board, move);
        v.confirm(board, move);
    }

    private final Board board = Board.create();
    private final Validators v = new Validators();
}
