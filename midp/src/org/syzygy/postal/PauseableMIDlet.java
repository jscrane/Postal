package org.syzygy.postal;

import javax.microedition.midlet.MIDlet;

abstract class PauseableMIDlet extends MIDlet
{
    protected void pauseApp()
    {
        this.paused = true;
        notifyPaused();
    }

    protected final void startApp()
    {
        if (paused)
            paused = false;
        else
            onStartApp();
    }

    protected abstract void onStartApp();

    private boolean paused = false;

    protected static interface EventListener
    {
        void onEvent(String eventClass, Object event);
    }
}
