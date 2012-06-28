package org.syzygy.chessms;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.RemoteDevice;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import org.syzygy.chessms.io.BluetoothDevice;
import org.syzygy.chessms.io.BluetoothDiscovery;
import org.syzygy.chessms.io.BluetoothTransport;
import org.syzygy.chessms.io.Completion;

public class BluetoothMIDlet 
	extends ChessMIDlet 
{
	public BluetoothMIDlet()
		throws IOException
	{
		this.uuid = getAppProperty("midlet.uuid");
	}
	
	protected void onStartApp()
	{
		form.addCommand(ok);
		form.addCommand(cancel);
		form.append(deviceList);
		form.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable disp) {
				if (c == cancel) {
					bluetooth.cancel();
					setTransport(new BluetoothTransport(uuid, null, bluetooth));
				} else {
					int i = deviceList.getSelectedIndex();
					if (i >= 0) {
						RemoteDevice d = ((BluetoothDevice)devices.elementAt(i)).getDevice();
						setTransport(new BluetoothTransport(uuid, d.getBluetoothAddress(), bluetooth));
					}
				}
			}
		});
		starting();
	}

	protected void findOpponent(Display display)
	{
		display.setCurrent(form);
		deviceList.deleteAll();
		devices.removeAllElements();
		bluetooth.discoverDevices(devices, new Completion() {
			public void complete(Object result) {
				BluetoothDevice d = (BluetoothDevice)result;
				if (d != null)
					if (d.getIndex() == -1) {
						deviceList.append(d.toString(), null);
						d.setIndex(deviceList.size() - 1);
					} else
						deviceList.set(d.getIndex(), d.toString(), null);
			}
		});		
	}
	
	protected void findExistingOpponent(Display display, String url, boolean toMove)
	{
		System.out.println("url="+url+" toMove="+toMove);
		if (url != null && url.startsWith(BluetoothTransport.PREFIX)) {
			String address = url.substring(BluetoothTransport.PREFIX.length());
			setTransport(new BluetoothTransport(uuid, address, bluetooth));
		} else if (!toMove)
			setTransport(new BluetoothTransport(uuid, null, bluetooth));
		else {
			form.removeCommand(cancel);
			findOpponent(display);
		}
	}
	
	private final Vector devices = new Vector();
	private final ChoiceGroup deviceList = new ChoiceGroup(null, Choice.EXCLUSIVE);
	private final Form form = new Form("Select Opponent");
	private final BluetoothDiscovery bluetooth = new BluetoothDiscovery();
	private final String uuid;
}
