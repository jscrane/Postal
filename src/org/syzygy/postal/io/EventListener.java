package org.syzygy.postal.io;

public interface EventListener
{
    void onEvent(String eventClass, Object event);
}
