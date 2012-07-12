package org.syzygy.postal.validation;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Piece;

public class TestCapture extends BoardValidatorSupport
{
    public void testCannotCaptureOwnPiece()
    {
        board.set(Piece.king(black), h8);
        board.set(Piece.pawn(black), g7);
        try {
            validator.validate(board, move(h8, g7));
            fail("IllegalMoveException not thrown");
        } catch (IllegalMoveException _) {
        }
    }

    private final Board board = new Board();
    private final BoardValidator validator = new Capture();
}
