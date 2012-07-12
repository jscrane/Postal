package org.syzygy.postal.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.View;
import org.syzygy.postal.R;
import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Piece;
import org.syzygy.postal.model.Square;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Bitmap.Config.RGB_565;
import static android.graphics.Color.RED;
import static org.syzygy.postal.model.Colour.BLACK;
import static org.syzygy.postal.model.Colour.WHITE;
import static org.syzygy.postal.model.Piece.*;

public final class BoardView extends View
{
    private final Map<Piece, Bitmap> bitmaps = new HashMap<Piece, Bitmap>();
    private Colour colour;
    private Square highlight = null;
    private Board board;

    public BoardView(Context context)
    {
        super(context);

        Resources resources = context.getResources();
        bitmaps.put(pawn(BLACK), BitmapFactory.decodeResource(resources, R.drawable.bp));
        bitmaps.put(pawn(WHITE), BitmapFactory.decodeResource(resources, R.drawable.wp));
        bitmaps.put(rook(BLACK), BitmapFactory.decodeResource(resources, R.drawable.br));
        bitmaps.put(rook(WHITE), BitmapFactory.decodeResource(resources, R.drawable.wr));
        bitmaps.put(knight(BLACK), BitmapFactory.decodeResource(resources, R.drawable.bn));
        bitmaps.put(knight(WHITE), BitmapFactory.decodeResource(resources, R.drawable.wn));
        bitmaps.put(bishop(BLACK), BitmapFactory.decodeResource(resources, R.drawable.bb));
        bitmaps.put(bishop(WHITE), BitmapFactory.decodeResource(resources, R.drawable.wb));
        bitmaps.put(queen(BLACK), BitmapFactory.decodeResource(resources, R.drawable.bq));
        bitmaps.put(queen(WHITE), BitmapFactory.decodeResource(resources, R.drawable.wq));
        bitmaps.put(king(BLACK), BitmapFactory.decodeResource(resources, R.drawable.bk));
        bitmaps.put(king(WHITE), BitmapFactory.decodeResource(resources, R.drawable.wk));
        colour = Colour.WHITE;
    }

    public void setBoard(Board b)
    {
        this.board = b;
    }

    public void setColour(Colour colour)
    {
        this.colour = colour;
    }

    public Colour getColour()
    {
        return colour;
    }

    public void setHighlight(String highlight)
    {
        this.highlight = highlight == null ? null : new Square(highlight);
    }

    private int getSide()
    {
        int w = getWidth(), h = getHeight();
        return h > w ? w : h;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        drawBoard(canvas, board);
    }

    private void drawBoard(Canvas canvas, Board board)
    {
        int side = getSide();
        Bitmap bb = Bitmap.createBitmap(side, side, RGB_565);
        Canvas cb = new Canvas(bb);
        cb.drawRGB(255, 255, 255);
        Paint red = new Paint();
        red.setColor(RED);
        int d = side / 8;
        boolean odd = true;
        for (int y = 0, j = 0; j < 8; y += d, j++) {
            for (int x = odd ? d : 0, i = 0; i < 4; x += d + d, i++) {
                Rect r = new Rect(x, y, x + d, y + d);
                cb.drawRect(r, red);
            }
            odd = !odd;
        }

        Paint paint = new Paint();
        canvas.drawBitmap(bb, 0, 0, paint);
        if (highlight != null)
            drawHighlight(canvas, highlight, d);
        for (Enumeration e = board.getPieces(WHITE); e.hasMoreElements(); )
            drawPieceAt(canvas, paint, board, (Square) e.nextElement(), d);
        for (Enumeration e = board.getPieces(BLACK); e.hasMoreElements(); )
            drawPieceAt(canvas, paint, board, (Square) e.nextElement(), d);
    }

    private int getRankForColour(int r)
    {
        return colour == WHITE ? 7 - r : r;
    }

    private int getRankForColour(Square s)
    {
        return getRankForColour(s.getRank());
    }

    private int getFileForColour(int f)
    {
        return colour == BLACK ? 7 - f : f;
    }

    private int getFileForColour(Square s)
    {
        return getFileForColour(s.getFile());
    }

    private void drawHighlight(Canvas canvas, Square s, int d)
    {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        int r = getRankForColour(s), f = getFileForColour(s);
        int left = d * f, top = d * r;
        canvas.drawRect(left, top, left + d, top + d, p);
    }

    private void drawPieceAt(Canvas canvas, Paint paint, Board board, Square s, int d)
    {
        Bitmap b = bitmaps.get(board.get(s));
        int w = b.getWidth(), h = b.getHeight();
        int ox = (d - w) / 2 - 1, oy = (d - h) / 2 - 1;
        int r = getRankForColour(s), f = getFileForColour(s);
        canvas.drawBitmap(b, d * f + ox, d * r + oy, paint);
    }

    @Override
    protected int getSuggestedMinimumHeight()
    {
        return bitmaps.values().iterator().next().getHeight() * 8;
    }

    @Override
    protected int getSuggestedMinimumWidth()
    {
        return getSuggestedMinimumHeight();
    }

    public Square getSquareAt(float x, float y)
    {
        int side = getSide();
        int r = (int) (y * 8 / side), f = (int) (x * 8 / side);
        return new Square(getRankForColour(r), getFileForColour(f));
    }
}
