/*******************************************************************************
 * Copyright (c) 2006 Wind River Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Wind River Systems, Inc. - initial implementation
 *     
 *******************************************************************************/
package org.eclipse.tm.terminal.internal.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

import org.eclipse.tm.terminal.ISettingsPage;
import org.eclipse.tm.terminal.ISettingsStore;
import org.eclipse.tm.terminal.ITerminalConnector;
import org.eclipse.tm.terminal.ITerminalControl;
import org.eclipse.tm.terminal.Logger;
import org.eclipse.tm.terminal.TerminalState;

public class SerialConnector implements ITerminalConnector {
	private OutputStream fOutputStream;
	private InputStream fInputStream;
	private ITerminalControl fControl;
	private SerialPort fSerialPort;
    private CommPortIdentifier fSerialPortIdentifier;
	private SerialPortHandler fTerminalSerialPortHandler;
	private boolean fIsPortInUse;
	private final SerialSettings fSettings;
	public SerialConnector() {
		SerialSettings settins=null;
		try {
			settins=new SerialSettings();			
		} catch (NoClassDefFoundError e) {
			// the comm library is not installed
			e.printStackTrace();
		}
		fSettings=settins;
	}
	public String getId() {
		return getClass().getName();
	}
	public boolean isInstalled() {
		// check if serial is installed
		try {
			return SerialPort.class!=null;
		} catch (Throwable e) {
			return false;
		}
	}
	public SerialConnector(SerialSettings settings) {
		fSettings=settings;
	}
	public void connect(ITerminalControl control) {
		Logger.log("entered."); //$NON-NLS-1$
		fControl=control;
		SerialConnectWorker worker = new SerialConnectWorker(this, control);
		worker.start();
	}
	public void disconnect() {
		Logger.log("entered."); //$NON-NLS-1$
	
		// Fix for SPR 112422.  When output is being received from the serial port, the
		// below call to removePortOwnershipListener() attempts to lock the serial port
		// object, but that object is already locked by another Terminal view thread
		// waiting for the SWT display thread to process a syncExec() call.  Since this
		// method is called on the display thread, the display thread is waiting to
		// lock the serial port object and the thread holding the serial port object
		// lock is waiting for the display thread to process a syncExec() call, so the
		// two threads end up deadlocked, which hangs the Workbench GUI.
		//
		// The solution is to spawn a short-lived worker thread that calls
		// removePortOwnershipListener(), thus preventing the display thread from
		// deadlocking with the other Terminal view thread.
	
		new Thread("Terminal View Serial Port Disconnect Worker") //$NON-NLS-1$
		{
			public void run() {
	
				if (getSerialPortIdentifier() != null) {
					getSerialPortIdentifier()
							.removePortOwnershipListener(getSerialPortHandler());
				}
	
				if (getSerialPort() != null) {
					getSerialPort().removeEventListener();
					Logger.log("Calling close() on serial port ..."); //$NON-NLS-1$
					getSerialPort().close();
				}
	
				if (getInputStream() != null) {
					try {
						getInputStream().close();
					} catch (Exception exception) {
						Logger.logException(exception);
					}
				}
	
				if (getOutputStream() != null) {
					try {
						getOutputStream().close();
					} catch (Exception exception) {
						Logger.logException(exception);
					}
				}
	
				setSerialPortIdentifier(null);
				cleanSerialPort();
				setSerialPortHandler(null);
			}

		}.start();
		fControl.setState(TerminalState.CLOSED);
	}
	public InputStream getInputStream() {
		return fInputStream;
	}
	public OutputStream getOutputStream() {
		return fOutputStream;
	}
	private void setInputStream(InputStream inputStream) {
		fInputStream = inputStream;
	}
	private void setOutputStream(OutputStream outputStream) {
		fOutputStream = outputStream;
	}
	public boolean isLocalEcho() {
		return false;
	}
	public void setTerminalSize(int newWidth, int newHeight) {
		// TODO
	}
	
	protected SerialPort getSerialPort() {
		return fSerialPort;
	}
	
	/**
	 * sets the socket to null
	 */
	void cleanSerialPort() {
		fSerialPort=null;
		setInputStream(null);
		setOutputStream(null);
	}
	
	protected void setSerialPort(SerialPort serialPort) throws IOException {
		cleanSerialPort();			
		if(serialPort!=null) {
			fSerialPort = serialPort;
			setOutputStream(serialPort.getOutputStream());
			setInputStream(serialPort.getInputStream());
		}
	}
	protected CommPortIdentifier getSerialPortIdentifier() {
		return fSerialPortIdentifier;
	}
	protected void setSerialPortIdentifier(CommPortIdentifier serialPortIdentifier) {
		fSerialPortIdentifier = serialPortIdentifier;
	}
	void setSerialPortHandler(SerialPortHandler serialPortHandler) {
		fTerminalSerialPortHandler=serialPortHandler;
	}
	SerialPortHandler getSerialPortHandler() {
		return fTerminalSerialPortHandler;
	}
	public boolean isPortInUse() {
		return fIsPortInUse;
	}
	public void setPortInUse(boolean b) {
		fIsPortInUse=true;
		
	}
	public ISerialSettings getSerialSettings() {
		return fSettings;
	}
	public ISettingsPage makeSettingsPage() {
		return new SerialSettingsPage(fSettings);
	}
	public String getStatusString(String strConnected) {
		return fSettings.getStatusString(strConnected);
	}
	public void load(ISettingsStore store) {
		fSettings.load(store);
	}
	public void save(ISettingsStore store) {
		fSettings.save(store);
	}
	
}