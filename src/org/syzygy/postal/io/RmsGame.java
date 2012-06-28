package org.syzygy.postal.io;

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import java.util.Enumeration;
import java.util.Vector;

public final class RmsGame
{
    private interface Operation
    {
        Object perform(RecordStore rs) throws RecordStoreException;
    }

    private Object doWithRecordStore(String name, Operation op)
    {
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(name, true);
            return op.perform(rs);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.closeRecordStore();
                } catch (RecordStoreException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    private String moves(String name)
    {
        return name + "_moves";
    }

    private String details(String name)
    {
        return name + "_details";
    }

    public void save(String name, final Enumeration moves, final String details)
    {
        doWithRecordStore(moves(name), new Operation()
        {
            public Object perform(RecordStore rs) throws RecordStoreException
            {
                for (int i = 0; moves.hasMoreElements(); i++) {
                    Object move = moves.nextElement();
                    String s = Integer.toString(i) + " " + move.toString();
                    byte[] b = s.getBytes();
                    rs.addRecord(b, 0, b.length);
                }
                return null;
            }
        });
        if (details != null)
            doWithRecordStore(details(name), new Operation()
            {
                public Object perform(RecordStore rs)
                        throws RecordStoreException
                {
                    byte[] b = details.getBytes();
                    rs.addRecord(b, 0, b.length);
                    return null;
                }
            });
    }

    public String load(String name, final Vector moves)
    {
        doWithRecordStore(moves(name), new Operation()
        {
            public Object perform(RecordStore rs) throws RecordStoreException
            {
                for (RecordEnumeration e = rs.enumerateRecords(null, cmp, false); e.hasNextElement(); )
                    moves.addElement(new String(e.nextRecord()));
                return null;
            }
        });
        try {
            RecordStore.deleteRecordStore(moves(name));
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
        String o = (String) doWithRecordStore(details(name), new Operation()
        {
            public Object perform(RecordStore rs) throws RecordStoreException
            {
                RecordEnumeration e = rs.enumerateRecords(null, null, false);
                if (e.hasNextElement())
                    return new String(e.nextRecord());
                return null;
            }
        });
        if (o != null)
            try {
                RecordStore.deleteRecordStore(details(name));
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        return o;
    }

    private final RecordComparator cmp = new RecordComparator()
    {
        public int compare(byte[] r1, byte[] r2)
        {
            return index(r1) < index(r2) ? PRECEDES : FOLLOWS;
        }

        private int index(byte[] r)
        {
            String s = new String(r);
            return Integer.parseInt(s.substring(0, s.indexOf(' ')));
        }
    };
}
