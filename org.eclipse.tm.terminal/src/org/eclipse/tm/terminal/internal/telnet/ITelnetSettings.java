/*******************************************************************************
 * Copyright (c) 2006 Wind River Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Michael Scharf (Wind River) - initial API and implementation
 * Martin Oberhuber (Wind River) - fixed copyright headers and beautified
 *******************************************************************************/
package org.eclipse.tm.terminal.internal.telnet;

import org.eclipse.tm.terminal.ISettingsStore;

public interface ITelnetSettings {
	String getHost();
	int getNetworkPort();
	int getTimeout();
	String getStatusString(String strConnected);
	void load(ISettingsStore store);
	void save(ISettingsStore store);
}
