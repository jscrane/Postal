package org.syzygy.postal.io;

import java.util.Enumeration;

public interface Filer
{
    void save(String name, Enumeration moves, Completion operation);

    void list(Completion result);

    void load(String name, Completion operation);
}
