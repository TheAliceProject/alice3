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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractOperation implements Operation {
	private java.util.UUID groupUUID;
	public AbstractOperation( java.util.UUID groupUUID ) {
		this.groupUUID = groupUUID;
	}
	public java.util.UUID getGroupUUID() {
		return this.groupUUID;
	}
	protected java.awt.Component getSourceComponent( Context< ? > context ) {
		if( context != null ) {
			java.util.EventObject e = context.getEvent();
			return edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( e.getSource(), java.awt.Component.class );
		} else {
			return null;
		}
	}
	public boolean canDoOrRedo() {
		return true;
	}
	public boolean canUndo() {
		return true;
	}
//	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
//		throw new javax.swing.undo.CannotRedoException();
//	}
//	public void undo() throws javax.swing.undo.CannotUndoException {
//		throw new javax.swing.undo.CannotUndoException();
//	}
	
	public abstract boolean isSignificant();
	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
		throw new javax.swing.undo.CannotRedoException();
	}
	public void undo() throws javax.swing.undo.CannotUndoException {
		throw new javax.swing.undo.CannotUndoException();
	}
	
	private boolean isEnabled = true;
	public boolean isEnabled() {
		return this.isEnabled;
	}
	public void setEnabled( boolean isEnabled ) {
		if( this.isEnabled != isEnabled ) {
			this.isEnabled = isEnabled;
			synchronized( this.components ) {
				for( javax.swing.JComponent component : this.components ) {
					component.setEnabled( this.isEnabled );
				}
			}
		}
	}

	private String toolTipText = null;
	public String getToolTipText() {
		return this.toolTipText;
	}
	public void setToolTipText( String toolTipText ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.toolTipText, toolTipText ) ) {
			//pass
		} else {
			this.toolTipText = toolTipText;
			synchronized( this.components ) {
				for( javax.swing.JComponent component : this.components ) {
					component.setToolTipText( this.toolTipText );
				}
			}
		}
	}
	private java.util.List< javax.swing.JComponent > components = new java.util.LinkedList< javax.swing.JComponent >();
	
	public void addComponent( javax.swing.JComponent component ) {
		synchronized( this.components ) {
			this.components.add( component );
			component.setEnabled( this.isEnabled );
			component.setToolTipText( this.toolTipText );
		}
	}
	public void removeComponent( javax.swing.JComponent component ) {
		synchronized( this.components ) {
			this.components.remove( component );
		}
	}
}
