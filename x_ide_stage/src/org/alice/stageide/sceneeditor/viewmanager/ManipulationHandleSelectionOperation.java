/**
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
package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.handle.HandleSet;

/**
 * @author David Culyba
 */
public class ManipulationHandleSelectionOperation extends edu.cmu.cs.dennisc.zoot.AbstractItemSelectionOperation< HandleSet > {
	private AbstractDragAdapter dragAdapter;
	
	public ManipulationHandleSelectionOperation(AbstractDragAdapter dragAdapter) {
		super( org.alice.ide.IDE.INTERFACE_GROUP, new ManipulationHandleControlComboBoxModel() );
		this.dragAdapter = dragAdapter;
	}
	@Override
	protected String getNameFor( int index, HandleSet handleMode ) {
		if( handleMode != null ) {
			return HandleSet.getStringForSet( handleMode );
		} else {
			return "null";
		}
	}
	
	@Override
	public KeyStroke getAcceleratorForConfiguringSwing( int index ) {
		// TODO Auto-generated method stub
		return KeyStroke.getKeyStroke( KeyEvent.VK_1 + index, 0);
	}
	
	public void setDragAdapter(AbstractDragAdapter dragAdapter)
	{
		this.dragAdapter = dragAdapter;
	}
	
	@Override
	protected void handleSelectionChange(org.alice.interact.handle.HandleSet value) {
		this.dragAdapter.setHandleSet( value );
	}
	@Override
	public boolean isSignificant() {
		return false;
	}
}
