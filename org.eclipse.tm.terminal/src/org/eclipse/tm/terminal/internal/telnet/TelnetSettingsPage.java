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

package org.eclipse.tm.terminal.internal.telnet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tm.terminal.ISettingsPage;

public class TelnetSettingsPage implements ISettingsPage {
	private Text fHostText;
	private Combo fNetworkPortCombo;
	private Text fTimeout;
	private final TelnetSettings fTerminalSettings;

	public TelnetSettingsPage(TelnetSettings settings) {
		fTerminalSettings=settings;
	}
	public void saveSettings() {
		fTerminalSettings.setHost(fHostText.getText());
		fTerminalSettings.setTimeout(fTimeout.getText());
		fTerminalSettings.setNetworkPort(getNetworkPort());
	}

	public void loadSettings() {
		if(fTerminalSettings!=null) {
			setHost(fTerminalSettings.getHost());
			setTimeout(fTerminalSettings.getTimeoutString());
			setNetworkPort(fTerminalSettings.getNetworkPortString());
		}
	}
	private void setHost(String strHost) {
		if(strHost==null)
			strHost=""; //$NON-NLS-1$
		fHostText.setText(strHost);
		
	}
	private void setTimeout(String timeout) {
		if(timeout==null || timeout.length()==0)
			timeout="5"; //$NON-NLS-1$
		fTimeout.setText(timeout);
		
	}
	private void setNetworkPort(String strNetworkPort) {
		String strPortName = getNetworkPortMap().findPortName(strNetworkPort);
		if(strPortName==null)
			strPortName=""; //$NON-NLS-1$
		int nIndex = fNetworkPortCombo.indexOf(strPortName);

		if (nIndex == -1) {
			fNetworkPortCombo.setText(strNetworkPort);
		} else {
			fNetworkPortCombo.select(nIndex);
		}
	}
	private String getNetworkPort() {
		return getNetworkPortMap().findPort(fNetworkPortCombo.getText());
	}
	private NetworkPortMap getNetworkPortMap() {
		return fTerminalSettings.getProperties().getNetworkPortMap();
	}

	public boolean validateSettings() {
		return true;
	}
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		composite.setLayout(gridLayout);
		composite.setLayoutData(gridData);

		// Add label
		Label ctlLabel = new Label(composite, SWT.RIGHT);
		ctlLabel.setText(TelnetMessages.HOST + ":"); //$NON-NLS-1$

		// Add control
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		fHostText = new Text(composite, SWT.BORDER);
		fHostText.setLayoutData(gridData);

		// Add label
		ctlLabel = new Label(composite, SWT.RIGHT);
		ctlLabel.setText(TelnetMessages.PORT + ":"); //$NON-NLS-1$

		// Add control
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		fNetworkPortCombo = new Combo(composite, SWT.DROP_DOWN);

		fNetworkPortCombo.setLayoutData(gridData);

		List table = getNetworkPortMap().getNameTable();
		Collections.sort(table);
		loadCombo(fNetworkPortCombo, table);

		new Label(composite, SWT.RIGHT).setText(TelnetMessages.TIMEOUT + ":"); //$NON-NLS-1$
		fTimeout = new Text(composite, SWT.BORDER);
		fTimeout.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		loadSettings();
	}
	private void loadCombo(Combo ctlCombo, List table) {
		for (Iterator iter = table.iterator(); iter.hasNext();) {
			String label = (String) iter.next();
			ctlCombo.add(label);
		}
	}

	public String getName() {
		return TelnetMessages.CONNTYPE_NETWORK;
	}

}