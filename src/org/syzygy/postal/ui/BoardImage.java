package org.syzygy.postal.ui;

import org.syzygy.postal.model.Board;
import org.syzygy.postal.model.Colour;
import org.syzygy.postal.model.Piece;
import org.syzygy.postal.model.Square;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

final class BoardImage
{
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

    public Image drawBoard(Board board, int side)
    {
        Image image = Image.createImage(side, side);
        Graphics g = image.getGraphics();
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, side - 1, side - 1);
        int d = side / 8;
        boolean odd = true;
        g.setColor(255, 0, 0);
        for (int y = 0, j = 0; j < 8; y += d, j++) {
            for (int x = odd ? d : 0, i = 0; i < 4; x += d + d, i++)
                g.fillRect(x, y, d, d);
            odd = !odd;
        }
        if (highlight != null)
            drawHighlight(g, highlight, d);
        for (Enumeration e = board.getPieces(Colour.WHITE); e.hasMoreElements(); )
            drawPieceAt(g, board, (Square) e.nextElement(), d);
        for (Enumeration e = board.getPieces(Colour.BLACK); e.hasMoreElements(); )
            drawPieceAt(g, board, (Square) e.nextElement(), d);
        return image;
    }

    private void drawHighlight(Graphics g, Square s, int d)
    {
        int c = g.getColor();
        g.setColor(0);
        int r = s.getRank(), f = s.getFile();
        if (colour == Colour.WHITE)
            r = 7 - r;
        else
            f = 7 - f;
        g.drawRect(d * f, d * r, d - 1, d - 1);
        g.setColor(c);
    }

    private void drawPieceAt(Graphics g, Board board, Square s, int d)
    {
        Piece p = board.get(s);
        Image i = (Image) images.get(p);
        try {
            if (i == null) {
                String t = p.toString();
                if (p.getColour() == Colour.WHITE)
                    t = "w" + t.toLowerCase();
                else
                    t = "b" + t;
                for (int b = d; b >= 0; b--) {
                    InputStream in = getClass().getResourceAsStream("/" + Integer.toString(b) + "/" + t + ".png");
                    if (in != null) {
                        i = Image.createImage(in);
                        images.put(p, i);
                        break;
                    }
                }
            }
            int w = i.getWidth(), h = i.getHeight();
            int ox = (d - w) / 2 - 1, oy = (d - h) / 2 - 1;
            int r = s.getRank(), f = s.getFile();
            if (colour == Colour.WHITE)
                r = 7 - r;
            else
                f = 7 - f;
            g.drawImage(i, d * f + ox, d * r + oy, Graphics.TOP | Graphics.LEFT);
        } catch (IOException _) {
            _.printStackTrace();
        }
    }

    private final Hashtable images = new Hashtable();
    private Colour colour = Colour.WHITE;
    private Square highlight = null;
}
