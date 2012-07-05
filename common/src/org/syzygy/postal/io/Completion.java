package org.syzygy.postal.io;

public interface Completion
{
    void complete(Object result);

    void error(Exception e);
}
