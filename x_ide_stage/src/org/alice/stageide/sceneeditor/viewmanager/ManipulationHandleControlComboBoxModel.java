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

import org.alice.interact.handle.HandleSet;

/**
 * @author David Culyba
 */
public class ManipulationHandleControlComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
	private HandleSet[] handleModes = { 
			HandleSet.DEFAULT_INTERACTION, 
			HandleSet.ROTATION_INTERACTION,
			HandleSet.TRANSLATION_INTERACTION,
			HandleSet.RESIZE_INTERACTION
	};
	private int selectedIndex;

	public ManipulationHandleControlComboBoxModel() {
		this.selectedIndex = 0;
	}
	public Object getElementAt( int index ) {
		return this.handleModes[ index ];
	}
	public int getSize() {
		return this.handleModes.length;
	}
	public Object getSelectedItem() {
		if( 0 <= this.selectedIndex && this.selectedIndex < this.handleModes.length ) {
			return this.handleModes[ this.selectedIndex ];
		} else {
			return null;
		}
	}
	public void setSelectedItem( Object selectedItem ) {
		int index = -1;
		if( selectedItem != null ) {
			int i = 0;
			for( HandleSet handleMode : this.handleModes ) {
				if( selectedItem.equals( handleMode ) ) {
					index = i;
					break;
				}
				i++;
			}
		}
		this.selectedIndex = index;
	}
}
