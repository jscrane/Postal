package org.syzygy.postal.io.midp;

import javax.microedition.io.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class IOUtil
{
    public static void closeQuietly(Connection conn)
    {
        if (conn != null)
            try {
                conn.close();
            } catch (IOException _) {
                // ignore
            }
    }

    public static void closeQuietly(InputStream stream)
    {
        if (stream != null)
            try {
                stream.close();
            } catch (IOException _) {
                // ignore
            }
    }

    public static void closeQuietly(OutputStream stream)
    {
        if (stream != null)
            try {
                stream.close();
            } catch (IOException _) {
                // ignore
            }
    }
}
