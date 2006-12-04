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
package org.eclipse.tm.terminal.internal.actions;

import org.eclipse.osgi.util.NLS;

public class ActionMessages extends NLS {
	static {
		NLS.initializeMessages(ActionMessages.class.getName(), ActionMessages.class);
	}
    public static String  NEW_TERMINAL;
    public static String  CONNECT;
    public static String  DISCONNECT;
    public static String  SETTINGS_ELLIPSE;
    public static String  COPY;
    public static String  CUT;
    public static String  PASTE;
    public static String  SELECTALL;
    public static String  CLEARALL;
    public static String  SETTINGS;

}