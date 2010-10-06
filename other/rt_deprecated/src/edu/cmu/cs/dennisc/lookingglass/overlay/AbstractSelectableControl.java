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
public abstract class AbstractSelectableControl extends Control {
	private boolean m_isSelected = false;
	private Group<AbstractSelectableControl> m_group = null;

	@Override
	public boolean isSelected() {
		return m_isSelected;
	}
	public void setSelected( boolean isSelected ) {
		if( m_isSelected != isSelected ) {
			m_isSelected = isSelected;
			setTextureDirty( true );
			fireChangeListeners();
		}
	}

	private java.util.List< javax.swing.event.ChangeListener > m_changeListeners = new java.util.LinkedList< javax.swing.event.ChangeListener >();
	public void addChangeListener( javax.swing.event.ChangeListener l ) {
		synchronized( m_changeListeners ) {
			m_changeListeners.add( l );
		}
	}
	public void removeChangeListener( javax.swing.event.ChangeListener l ) {
		synchronized( m_changeListeners ) {
			m_changeListeners.remove( l );
		}
	}
	public Iterable< javax.swing.event.ChangeListener > accessChangeListeners() {
		synchronized( m_changeListeners ) {
			return m_changeListeners;
		}
	}
	protected void fireChangeListeners() {
		javax.swing.event.ChangeEvent e = new javax.swing.event.ChangeEvent( this );
		synchronized( m_changeListeners ) {
			for( javax.swing.event.ChangeListener l : m_changeListeners ) {
				l.stateChanged( e );
			}
		}
	}
	
	public Group<AbstractSelectableControl> getGroup() {
		return m_group;
	}
	public void setGroup( Group<AbstractSelectableControl> group ) {
		m_group = group;
	}
}
