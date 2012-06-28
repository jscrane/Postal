package org.syzygy.chessms;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.TextField;

import org.syzygy.chessms.action.SharedGameController;
import org.syzygy.chessms.io.Completion;
import org.syzygy.chessms.io.Filer;
import org.syzygy.chessms.io.RmsGame;
import org.syzygy.chessms.ui.DefaultCommand;
import org.syzygy.chessms.ui.MainCanvas;

public final class BoardMIDlet
	extends PauseableMIDlet
{
	public BoardMIDlet()
	{
		this.display = Display.getDisplay(this);
		this.name = getAppProperty("board.name");
		this.filer = new Filer(getAppProperty("postal.midlet.directory"));

		this.main = new MainCanvas(new DefaultCommand(ok, new CommandListener() {
			public void commandAction(Command c, Displayable disp) {
				if (c == quit)
					display.setCurrent(saveDialog);
				else if (c == ok)
					controller.processMove();
				else if (c == saveGame)
					display.setCurrent(file);
				else if (c == loadGame) {
					directory.deleteAll();
					filer.list(new Completion() {
						public void complete(Object o) {
							Vector files = (Vector)o;
							for (Enumeration e = files.elements(); e.hasMoreElements(); )
								directory.append((String)e.nextElement(), null);
						}
					});
					display.setCurrent(directory);
				}
			}
		}));
		this.controller = new SharedGameController(main);
	}
	
	protected void destroyApp(boolean unconditional)
	{
		notifyDestroyed();
	}

	protected void onStartApp()
	{
		Vector moves = new Vector();
		rms.load(name, moves);
		controller.restart(moves);
		main.addCommand(ok);
		main.addCommand(quit);
		main.addCommand(saveGame);
		main.addCommand(loadGame);

		file.addCommand(ok);
		file.addCommand(cancel);
		file.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == ok)
					filer.save(fileName.getString(), controller.getMoves());
				display.setCurrent(main);
			}
		});

		directory.setSelectCommand(ok);
		directory.addCommand(cancel);
		directory.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == ok) {
					final String file = directory.getString(directory.getSelectedIndex());
					filer.load(file, new Completion() {
						public void complete(Object o) {
							if (o != null) {
								controller.restart((Vector)o);
								display.setCurrent(main);
							}
						}
					});
				} else
					display.setCurrent(main);
			}
		});

		final Command yes = new Command("Yes", Command.SCREEN, 1);
		final Command no = new Command("No", Command.SCREEN, 1);
		saveDialog.addCommand(yes);
		saveDialog.addCommand(no);
		saveDialog.addCommand(cancel);
		saveDialog.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == cancel)
					display.setCurrent(main);
				else {
					if (c == yes)
						rms.save(name, controller.getMoves(), null);
					destroyApp(false);
				}
			}
		});

		display.setCurrent(main);
	}

	private final String name;
	private final Filer filer;
	private final Display display;
	private final MainCanvas main;
	private final SharedGameController controller;
	private final TextField fileName = new TextField(null, null, 64, TextField.ANY);
	private final Form file = new Form("Save", new Item[] { fileName});
	private final List directory = new List("Load", List.IMPLICIT);
	private final RmsGame rms = new RmsGame();
	private final Command ok = new Command("OK", Command.SCREEN, 1);
	private final Command cancel = new Command("Cancel", Command.SCREEN, 1);
	private final Command quit = new Command("Quit", Command.SCREEN, 3);
	private final Command saveGame = new Command("Save", Command.SCREEN, 3);
	private final Command loadGame = new Command("Load", Command.SCREEN, 3);
	private final Screen saveDialog = new Alert("Save...", "Save game for next time?", null, AlertType.CONFIRMATION);
}
