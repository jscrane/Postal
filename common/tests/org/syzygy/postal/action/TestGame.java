package org.syzygy.postal.action;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Move;
import org.syzygy.postal.model.Piece;
import org.syzygy.postal.model.Square;
import org.syzygy.postal.validation.IllegalMoveException;

import java.util.Enumeration;

public final class TestGame extends TestCase
{
    public void testIllegalE4E5() throws Exception
    {
        move("e2-e4");
        move("e7-e5");
        try {
            move("e4-e5");
            Assert.fail("IllegalMoveException expected");
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
        String[] moves = { "e2-e4", "c7-c5", "g1-f3", "d7-d6", "d2-d4", "c5xd4", "f3xd4", "g8-f6", "b1-c3" };
        move(moves);
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

    public void testQueenExchange() throws Exception
    {
        move(new String[]{ "d2-d4", "d7-d5", "e2-e4", "e7-e5", "d4xe5", "d5xe4", "d1xd8", "e8xd8" });
        assertEquals(Piece.king(Colour.BLACK), game.getBoard().get(new Square("d8")));
    }

    private void move(String[] moves) throws IllegalMoveException
    {
        for (int i = 0; i < moves.length; i++)
            assertNotNull(move(moves[i]));
    }

    private Move move(String m) throws IllegalMoveException
    {
        return game.move(Move.valueOf(m));
    }

    private final GameController game = new GameController();
}
