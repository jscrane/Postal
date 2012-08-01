package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Move;

public final class TestTurnValidator extends BoardValidatorSupport
{
    protected void setUp() throws Exception
    {
        Move white = Move.valueOf("e2-e4");
        validator.validate(board, white);
        validator.confirm(board, white);
    }

    public void testValidatesTrue() throws Exception
    {
        Move black = Move.valueOf("e7-e5");
        validator.validate(board, black);
    }

    public void testValidatesFalse() throws Exception
    {
        Move white = Move.valueOf("d2-d4");
        try {
            validator.validate(board, white);
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testBlackResigns() throws Exception
    {
        Move resigns = Move.BLACK_RESIGNS;
        validator.validate(board, resigns);
        validator.confirm(board, resigns);
        try {
            validator.validate(board, Move.valueOf("d2-d4"));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    public void testWhiteResignsOutOfTurn() throws Exception
    {
        try {
            validator.validate(board, Move.WHITE_RESIGNS);
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    private final Board board = Board.create();
    private final TurnValidator validator = new TurnValidator();
}
