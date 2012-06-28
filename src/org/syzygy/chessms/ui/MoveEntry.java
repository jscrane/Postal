package org.syzygy.chessms.ui;

import javax.microedition.lcdui.Canvas;

import org.syzygy.chessms.model.Colour;

public abstract class MoveEntry
{
    public String toString()
    {
    	return contents.toString();
    }

    public void setString(String s)
    {
        contents.setLength(0);
        if (s != null)
            contents.append(s);
    }

    public String getSquare()
    {
		int n = contents.length();
		if (n < 2)
		    return null;
		if (n <= 4)
		    return contents.toString().substring(0, 2);
		return contents.toString().substring(3, 5);
    }

    public void processKey(int keyCode)
    {
		int n = contents.length();
		if (n == 2) {
		    contents.append('-');
		    n++;
		}
		char c = (char)keyCode;
		if ("12345678#".indexOf(c) == -1)
			return;
		if (n == 0 || n == 3) {
		    switch (c) {
		    case '1':
		    	c = 'a';
		    	break;
		    case '2':
		    	c = 'b';
		    	break;
		    case '3':
		    	c = 'c';
		    	break;
		    case '4':
		    	c = 'd';
		    	break;
		    case '5':
		    	c = 'e';
		    	break;
		    case '6':
		    	c = 'f';
		    	break;
		    case '7':
		    	c = 'g';
		    	break;
		    case '8':
		    	c = 'h';
		    	break;
		    }
		}
		if (c == '#') {
		    if (n == 3)
		    	n = 1;
		    else if (n > 0)
		    	n--;
		    contents.setLength(n);
		} else if (n < 5) {
		    contents.append(c);
		    n++;
		    if (n == 2)
		    	contents.append('-');
		}
    }

    public void processActionKey(int a)
    {
		int n = contents.length();
		if (a == Canvas.FIRE) {
		    if (n == 2) {
		    	String cont = contents.toString();
		    	contents.append('-').append(cont);
		    }
		} else if (n == 0)
		    contents.append(startSquare());
		else {
		    String s = startSquare();
		    char c = s.charAt(0), r = s.charAt(1);
		    if (n < 3) {
		    	c = contents.charAt(0);
		    	if (n > 1)
		    		r = contents.charAt(1);
		    	contents.delete(0, n);
		    	n = 2;
		    } else if (n == 3) {
		    	c = contents.charAt(0);
		    	r = contents.charAt(1);
				n = 5;
		    } else if (n < 6) {
				c = contents.charAt(3);
				if (n > 4)
					r = contents.charAt(4);
				contents.delete(3, n);
				n = 5;
		    }
		    switch (a) {
		    case Canvas.UP:
		    	r = up(r);
		    	break;
		    case Canvas.DOWN:
		    	r = down(r);
		    	break;
		    case Canvas.LEFT:
		    	c = left(c);
		    	break;
		    case Canvas.RIGHT:
		    	c = right(c);
		    	break;
		    }
		    contents.insert(n-2, new String(new char[] { c, r }));
		}
    }

    public boolean isComplete()
    {
    	return contents.length() == 5 && (contents.charAt(0) != contents.charAt(3) || contents.charAt(1) != contents.charAt(4));
    }

    public static MoveEntry get(Colour colour)
    {
    	return colour == Colour.WHITE? white: black;
    }

    protected abstract String startSquare();

    protected abstract char up(char c);

    protected abstract char down(char c);

    protected abstract char left(char c);

    protected abstract char right(char c);

    private final StringBuffer contents = new StringBuffer();
    
    private static final MoveEntry white = new WhiteMoveEntry(), black = new BlackMoveEntry();
}

class WhiteMoveEntry
    extends MoveEntry
{
    protected String startSquare() { return "a1"; }

    protected char up(char c) { return c == '8'? '1': ++c; }

    protected char down(char c) { return c == '1'? '8': --c; }

    protected char left(char c) { return c == 'a'? 'h': --c; }

    protected char right (char c) { return c == 'h'? 'a': ++c; }
}

class BlackMoveEntry
    extends MoveEntry
{
    protected String startSquare() { return "h8"; }

    protected char down(char c) { return c == '8'? '1': ++c; }

    protected char up(char c) { return c == '1'? '8': --c; }

    protected char right(char c) { return c == 'a'? 'h': --c; }

    protected char left(char c) { return c == 'h'? 'a': ++c; }
}

