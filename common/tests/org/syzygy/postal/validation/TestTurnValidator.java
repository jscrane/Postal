package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;

public final class TestTurnValidator extends BoardValidatorSupport
{
    protected void setUp()
    {
        this.board = Board.create();
        this.validator = new TurnValidator();
    }

    public void testValidatesTrue() throws Exception
    {
        Move white = Move.valueOf("e2-e4");
        validator.validate(board, white);
        validator.confirm(board, white);
        Move black = Move.valueOf("e7-e5");
        validator.validate(board, black);
    }

    public void testValidatesFalse() throws Exception
    {
        Move white = Move.valueOf("e2-e4");
        validator.validate(board, white);
        validator.confirm(board, white);
        white = Move.valueOf("d2-d4");
        try {
            validator.validate(board, white);
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    private Board board;
    private TurnValidator validator;
}
