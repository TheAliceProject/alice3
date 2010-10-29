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
package edu.cmu.cs.dennisc.lookingglass.overlay;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractButton extends Control {
	private String m_command;

	public String getCommand() {
		return m_command;
	}
	public void setCommand( String command ) {
		m_command = command;
	}
	private java.util.List< java.awt.event.ActionListener > m_actionListeners = new java.util.LinkedList< java.awt.event.ActionListener >();
	public void addActionListener( java.awt.event.ActionListener l ) {
		synchronized( m_actionListeners ) {
			m_actionListeners.add( l );
		}
	}
	public void removeActionListener( java.awt.event.ActionListener l ) {
		synchronized( m_actionListeners ) {
			m_actionListeners.remove( l );
		}
	}
	public Iterable< java.awt.event.ActionListener > accessActionListeners() {
		synchronized( m_actionListeners ) {
			return m_actionListeners;
		}
	}
	protected void fireActionListeners() {
		java.awt.event.ActionEvent e = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, getCommand() );
		synchronized( m_actionListeners ) {
			for( java.awt.event.ActionListener l : m_actionListeners ) {
				l.actionPerformed( e );
			}
		}
	}
}
