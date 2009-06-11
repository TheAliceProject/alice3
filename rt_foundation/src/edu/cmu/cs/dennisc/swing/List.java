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
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class List< E extends Object > extends javax.swing.JList {
	private java.util.List< java.awt.event.ActionListener > m_actionListeners = new java.util.LinkedList< java.awt.event.ActionListener >();
	public List() {
		initialize();
	}
	public List( javax.swing.ListModel model ) {
		super( model );
		initialize();
	}
	
	private void initialize() {
		addMouseListener( new java.awt.event.MouseListener() {
			public void mousePressed( java.awt.event.MouseEvent e ) {
				fireActionListeners( e.getWhen(), e.getModifiers(), true );
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
		} );
		addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					int modifiers = 0; //todo
					fireActionListeners( System.currentTimeMillis(), modifiers, false );
				}
			}
		} );
	}

	public void addActionListener( java.awt.event.ActionListener l ) {
		m_actionListeners.add( l );
	}
	public void removeActionListener( java.awt.event.ActionListener l ) {
		m_actionListeners.remove( l );
	}
	
	private int m_selectedIndexPrev = -1;
	private boolean m_isActionEventPrev = false;
	private void fireActionListeners( long when, int modifiers, boolean isActionEvent ) {
		int selectedIndex = getSelectedIndex();
		if( selectedIndex != m_selectedIndexPrev || m_isActionEventPrev == isActionEvent ) {
			String command;
			Object value = getSelectedValue();
			if( value != null ) {
				command = value.toString();
			} else {
				command = null;
			}
			java.awt.event.ActionEvent e = new java.awt.event.ActionEvent( this, java.awt.event.ActionEvent.ACTION_PERFORMED, command, when, modifiers );
			for( java.awt.event.ActionListener l : m_actionListeners ) {
				l.actionPerformed( e );
			}
			m_selectedIndexPrev = selectedIndex;
		} else {
			m_isActionEventPrev = isActionEvent;
		}
	}
	
	public E getSelectedTypedValue() {
		return (E)getSelectedValue();
	}
}
