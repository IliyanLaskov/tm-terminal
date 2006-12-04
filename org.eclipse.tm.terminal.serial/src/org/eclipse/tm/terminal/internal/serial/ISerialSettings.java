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

import org.eclipse.tm.terminal.ISettingsStore;

public interface ISerialSettings {

	String getSerialPort();
	int getBaudRate();
	int getDataBits();
	int getStopBits();
	int getParity();
	int getFlowControl();
	int getTimeout();
	String getStatusString(String strConnected);
	void load(ISettingsStore store);
	void save(ISettingsStore store);
}