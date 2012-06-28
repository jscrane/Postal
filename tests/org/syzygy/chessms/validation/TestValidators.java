package org.syzygy.chessms.validation;

import junit.framework.TestCase;
import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Move;

public final class TestValidators extends TestCase
{
    public void testWhiteOpeningE2E4() throws Exception
    {
        v.validate(board, Move.valueOf("e2-e4"));
    }

    private final Board board = Board.create();
    private final Validators v = new Validators();
}
