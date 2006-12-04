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
package org.eclipse.tm.terminal;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * Manage a single connection. Implementations of this class are contributed 
 * via <code>org.eclipse.tm.terminal.terminalConnector</code> extension point.
 * 
 * @author Michael Scharf
 *
 */
public interface ITerminalConnector {
	/**
	 * @return an ID of this connector. Typically <code>getClass().getName()</code>
	 */
	String getId();
	/**
	 * @return true if the contribution is functioning (e.g. all external libraries are
	 * installed). This was added for the serial support, because it requires the java comm 
	 * library, which is installed in the lib/ext directory of the
	 */
	boolean isInstalled();
	/**
	 * Connect using the current state of the settings.
	 * @param control Used to inform the UI about state changes and messages from the connection.
	 */
	void connect(ITerminalControl control);
	/**
	 * Disconnect if connected. Else do nothing.
	 */
	void disconnect();

	/**
	 * @return true if a local echo is needed.
	 * TODO:Michael Scharf: this should be handed within the connection....
	 */
	boolean isLocalEcho();

    /**
     * Notify the remote site that the size of the terminal has changed.
     * @param newWidth
     * @param newHeight
     */
    void setTerminalSize(int newWidth, int newHeight);

    /**
     * @return a stream with data coming from the remote site.
     */
    OutputStream getOutputStream();
    /**
     * @return a stream to write to the remote site.
     */
    InputStream getInputStream();
    
	/**
	 * Load the state of this connection. Is typically called before 
	 * {@link #connect(ITerminalControl)}.
	 * 
	 * @param store a string based data store. Short keys like "foo" can be used to 
	 * store the state of the connectio.
	 */
	void load(ISettingsStore store);
	
	/**
	 * When the view or dialog containing the terminal is closed, 
	 * the state of the connection is saved into the settings store <code>store</code>
	 * @param store
	 */
	void save(ISettingsStore store);

	/**
	 * @return a new page that can be used in a dialog to setup this connection.
	 *  
	 */
	ISettingsPage makeSettingsPage();

	/**
	 * @param connectedLabel a String with the connected state {@link TerminalState}. 
	 * Like "CONNECTED", "CLOSED". Can be used to build up the status string.
	 * @return A string that represents the state of the connection. 
	 * TODO: Michael Scharf:
	 */
	String getStatusString(String connectedLabel);

}