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
package org.alice.stageide.sceneeditor;

import org.alice.stageide.sceneeditor.viewmanager.ManipulationHandleControlPanel;
import org.alice.interact.AbstractDragAdapter;

/**
 * @author Dennis Cosgrove
 */
class SidePane extends edu.cmu.cs.dennisc.croquet.swing.PageAxisPane {
	private boolean isExpanded = false;
	private ManipulationHandleControlPanel handleControlPanel;

	public SidePane() {
		this.handleControlPanel = new ManipulationHandleControlPanel();
		this.add( this.handleControlPanel );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
	}
	public boolean isExpanded() {
		return this.isExpanded;
	}
	public void setExpanded( boolean isExpanded ) {
		this.isExpanded = isExpanded;
		this.revalidate();
		this.repaint();
		//this.doLayout();
	}
	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.handleControlPanel.setDragAdapter( dragAdapter );
	}

}
