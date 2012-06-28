package org.syzygy.postal.io;

import java.util.Enumeration;
import java.util.Vector;

public interface Persistence
{
    void save(String name, Enumeration moves, String details);

    String load(String name, Vector moves);
}
