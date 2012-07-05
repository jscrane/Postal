package org.syzygy.postal.io.midp;

import org.syzygy.postal.io.Completion;
import org.syzygy.postal.io.Filer;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

public final class MidpFiler implements Filer
{
    private final String directory;

    public MidpFiler(String directory)
    {
        this.directory = "file:///" + directory;
    }

    public void save(final String name, final Enumeration moves, final Completion result)
    {
        Thread saver = new Thread()
        {
            public void run()
            {
                FileConnection file = null, dir = null;
                OutputStream out = null;
                try {
                    dir = (FileConnection) Connector.open(directory);
                    if (!dir.exists())
                        dir.mkdir();
                    file = (FileConnection) Connector.open(directory + "/" + name);
                    if (!file.exists())
                        file.create();
                    out = file.openOutputStream();
                    save(out, moves);
                    result.complete(name);
                } catch (IOException e) {
                    result.error(e);
                } finally {
                    IOUtil.closeQuietly(out);
                    IOUtil.closeQuietly(file);
                    IOUtil.closeQuietly(dir);
                }
            }
        };
        saver.start();
    }

    private void save(OutputStream out, Enumeration e) throws IOException
    {
        StringBuffer buf = new StringBuffer();
        while (e.hasMoreElements())
            buf.append(e.nextElement().toString()).append("\n");

        out.write(buf.toString().getBytes());
    }

    public void list(final Completion result)
    {
        Thread lister = new Thread()
        {
            public void run()
            {
                FileConnection dir = null;
                try {
                    dir = (FileConnection) Connector.open(directory, Connector.READ);
                    result.complete(list(dir.list()));
                } catch (IOException e) {
                    result.error(e);
                } finally {
                    IOUtil.closeQuietly(dir);
                }
            }
        };
        lister.start();
    }

    private Vector list(Enumeration e)
    {
        Vector v = new Vector();
        while (e.hasMoreElements())
            v.addElement(e.nextElement());

        return v;
    }

    public void load(final String name, final Completion result)
    {
        Thread loader = new Thread()
        {
            public void run()
            {
                FileConnection file = null;
                InputStream in = null;
                try {
                    file = (FileConnection) Connector.open(directory + "/" + name, Connector.READ);
                    in = file.openInputStream();
                    result.complete(load(in, (int) file.fileSize()));
                } catch (IOException e) {
                    result.error(e);
                } finally {
                    IOUtil.closeQuietly(in);
                    IOUtil.closeQuietly(file);
                }
            }
        };
        loader.start();
    }

    private Vector load(InputStream in, int n) throws IOException
    {
        byte[] buf = new byte[n];
        int off = 0, read;
        while (n > 0 && (read = in.read(buf, off, n)) != -1) {
            n -= read;
            off += read;
        }
        String s = new String(buf);
        int curr = 0, next;
        Vector moves = new Vector();
        while ((next = s.indexOf("\n", curr)) != -1) {
            String m = s.substring(curr, next);
            moves.addElement(m);
            curr = next + 1;
        }
        return moves;
    }
}
