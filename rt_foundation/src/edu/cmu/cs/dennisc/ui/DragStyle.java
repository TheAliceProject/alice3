/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.ui;

/**
 * @author Dennis Cosgrove
 */
public enum DragStyle {
	NORMAL( false, false ),
	CONTROL( true, false ),
	SHIFT( false, true ),
	CONTROL_SHIFT( true, true );
	private boolean m_isControlDown;
	private boolean m_isShiftDown;
	DragStyle( boolean isControlDown, boolean isShiftDown ) {
		m_isControlDown = isControlDown;
		m_isShiftDown = isShiftDown;
	}
	public boolean isControlDown() {
		 return m_isControlDown;
	}
	public boolean isShiftDown() {
		 return m_isShiftDown;
	}
}
