package org.syzygy.chessms.ui;

import org.syzygy.chessms.model.Board;
import org.syzygy.chessms.model.Colour;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class MainCanvas extends Canvas implements MainDisplay
{
    public MainCanvas(DefaultCommand defaultCommand)
    {
        setFullScreenMode(true);
        this.defaultCommand = defaultCommand;
        setCommandListener(defaultCommand);
        setStatus("Postal (c) 2008 Stephen Crane");
    }

    public void setAdvantage(int a)
    {
        if (getColour() == Colour.BLACK)
            a = -a;
        status.setAdvantage(Integer.toString(a));
        status.repaint();
    }

    public void setStatus(String comment)
    {
        status.setString(comment);
        status.repaint();
    }

    public void setIsMyTurn(boolean yes)
    {
        this.isEditable = yes;
    }

    public Colour getColour()
    {
        return boardImage.getColour();
    }

    public void setColour(Colour colour)
    {
        boardImage.setColour(colour);
        moveEntryItem.setContents(MoveEntry.get(colour));
    }

    public void setGame(Board board, Colour colour, boolean isMyTurn)
    {
        setColour(colour);
        setIsMyTurn(isMyTurn);

        this.board = board;
        redrawBoard();

        status.setAdvantage(null);
        moveEntryItem.setLabel(null);
        moveEntryItem.setString(null);
        moveEntryItem.repaint();
    }

    public String getMove()
    {
        return moveEntryItem.getString();
    }

    public void setMove(int n, String move)
    {
        int m = (n + 1) / 2;
        boolean white = (n % 2) != 0;
        String ms;
        if (white) {
            status.setString("Black to move");
            ms = Integer.toString(m);
        } else {
            status.setString("White to move");
            ms = moveEntryItem.getLabel();
            if (ms == null)
                ms = Integer.toString(m) + "...";
        }
        status.repaint();
        int space = move.indexOf(' ');
        if (space > 0)
            move = move.substring(0, space);
        moveEntryItem.setLabel(ms + " " + move);
        clearMove();
        redrawBoard();
    }

    public void clearMove()
    {
        moveEntryItem.setString(null);
        moveEntryItem.repaint();
    }

    protected void keyReleased(int keyCode)
    {
        if (isEditable)
            moveEntryItem.keyReleased(keyCode);
    }

    protected void paint(Graphics g)
    {
        status.paint(g);
        g.translate(0, textHeight);
        g.drawImage(boardImage.drawBoard(board, side), 0, 0, Graphics.TOP | Graphics.LEFT);
        g.translate(0, width);
        moveEntryItem.paint(g);
    }

    private void redrawBoard()
    {
        repaint(0, textHeight, side, side);
    }

    private abstract class TextItem
    {
        public void paint(Graphics g)
        {
            g.setFont(font);
            int c = g.getColor();
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, width, textHeight);
            g.setColor(c);
            String s = toString();
            if (s != null)
                g.drawString(s, 0, 0, Graphics.LEFT | Graphics.TOP);
        }

        public abstract String toString();

        public abstract void setString(String s);

        public abstract void repaint();
    }

    private final class MoveEntryItem extends TextItem
    {
        public String toString()
        {
            String s = label;
            if (s == null)
                s = "";
            else
                s += " ";
            return s + getString();
        }

        public void repaint()
        {
            MainCanvas.this.repaint(0, side + textHeight, width, textHeight);
        }

        public void keyReleased(int keyCode)
        {
            if (keyCode == -8)  // ugh: C key on S-E
                keyCode = '#';
            String cont = contents.toString();
            if (keyCode > 0)
                contents.processKey(keyCode);
            else {
                int a = getGameAction(keyCode);
                if (a > 0) {
                    contents.processActionKey(a);
                    if (a == FIRE && contents.isComplete())
                        defaultCommand.fire(MainCanvas.this);
                }
            }
            if (!contents.toString().equals(cont)) {
                boardImage.setHighlight(contents.getSquare());
                redrawBoard();
                moveEntryItem.repaint();
            }
        }

        public void setLabel(String label)
        {
            this.label = label;
        }

        public String getLabel()
        {
            return label;
        }

        public void setString(String string)
        {
            contents.setString(string);
        }

        public String getString()
        {
            return contents.toString();
        }

        public void setContents(MoveEntry contents)
        {
            this.contents = contents;
        }

        private String label;
        private MoveEntry contents = new WhiteMoveEntry();
    }

    private final class StatusItem extends TextItem
    {
        public void repaint()
        {
            MainCanvas.this.repaint(0, 0, width, textHeight);
        }

        public void paint(Graphics g)
        {
            super.paint(g);
            if (advantage != null) {
                int w = font.stringWidth(advantage);
                g.drawString(advantage, width - w, 0, Graphics.LEFT | Graphics.TOP);
            }
        }

        public void setString(String status)
        {
            this.status = status;
        }

        public String toString()
        {
            return status;
        }

        public void setAdvantage(String advantage)
        {
            this.advantage = advantage;
        }

        private String status, advantage;
    }

    protected void sizeChanged(int w, int h)
    {
        this.width = w;
        this.side = width < h ? width : h;
    }

    private final StatusItem status = new StatusItem();
    private final MoveEntryItem moveEntryItem = new MoveEntryItem();
    private final Font font = Font.getFont(Font.FONT_INPUT_TEXT);
    private final int textHeight = font.getHeight();
    private final BoardImage boardImage = new BoardImage();
    private final DefaultCommand defaultCommand;
    private boolean isEditable = true;
    private int width;
    private int side;
    private Board board;
}
