package org.syzygy.postal.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public final class DefaultCommand implements CommandListener
{
    public DefaultCommand(Command defaultCommand, CommandListener delegate)
    {
        this.delegate = delegate;
        this.command = defaultCommand;
    }

    public void fire(Displayable disp)
    {
        delegate.commandAction(command, disp);
    }

    public void commandAction(Command c, Displayable disp)
    {
        delegate.commandAction(c, disp);
    }

    private final CommandListener delegate;
    private final Command command;
}
