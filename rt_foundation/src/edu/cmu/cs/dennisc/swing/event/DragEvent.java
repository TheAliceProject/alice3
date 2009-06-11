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
package edu.cmu.cs.dennisc.swing.event;

/**
 * @author Dennis Cosgrove
 */
public class DragEvent extends edu.cmu.cs.dennisc.pattern.event.Event< edu.cmu.cs.dennisc.swing.DragSource > {
	private edu.cmu.cs.dennisc.swing.DragComponent m_dragComponent;
	private java.awt.event.MouseEvent m_mouseEvent;
	public DragEvent( edu.cmu.cs.dennisc.swing.DragSource source, edu.cmu.cs.dennisc.swing.DragComponent dragComponent, java.awt.event.MouseEvent mouseEvent ) {
		super( source );
		m_dragComponent = dragComponent;
		m_mouseEvent = mouseEvent;
	}
	public edu.cmu.cs.dennisc.swing.DragComponent getDragComponent() {
		return m_dragComponent;
	}
	public java.awt.event.MouseEvent getMouseEvent() {
		return m_mouseEvent;
	}
}
