package org.syzygy.chessms.action;

import junit.framework.TestCase;
import org.syzygy.chessms.model.Move;
import org.syzygy.chessms.validation.IllegalMoveException;

import java.util.Enumeration;

public final class TestGame extends TestCase
{
    public void testIllegalE4E5() throws Exception
    {
        move("e2-e4");
        move("e7-e5");
        try {
            move("e4-e5");
            fail("IllegalMoveException expected");
        } catch (Exception _) {
        }
    }

    public void testSicilian() throws Exception
    {
        move("e2-e4");
        move("c7-c5");
        move("g1-f3");
        move("d7-d6");
        move("d2-d4");
        Move m = move("c5xd4");
        assertTrue(m.isCapture());
        m = move("f3xd4");
        assertTrue(m.isCapture());
        move("g8-f6");
        move("b1-c3");
    }

    public void testGameMoves() throws Exception
    {
        String[] moves = { "e2-e4", "c7-c5", "g1-f3", "d7-d6", "d2-d4",
                "c5xd4", "f3xd4", "g8-f6", "b1-c3" };
        for (int i = 0; i < moves.length; i++)
            assertNotNull(move(moves[i]));
        int i = 0;
        for (Enumeration e = game.getMoves(); e.hasMoreElements(); i++) {
            Move m = (Move) e.nextElement();
            assertEquals(moves[i], m.toString());
        }
        assertEquals(moves.length, i);
        assertEquals(moves.length, game.getNumberOfMoves());
    }

    public void testKingCapturesQueen() throws Exception
    {
        String[] moves = { "e2-e4", "e7-e5", "d2-d4", "d7-d5",
                "d4xe5", "d5xe4", "d1xd8", "e8xd8" };
        for (int i = 0; i < moves.length; i++) {
            System.out.println(game.getBoard().toString());
            System.out.println(moves[i]);
            assertNotNull(move(moves[i]));
        }
    }

    private Move move(String m) throws IllegalMoveException
    {
        Move move = game.validate(m);
        if (m != null)
            game.complete(move);
        return move;
    }

    private final GameController game = new GameController();
}
