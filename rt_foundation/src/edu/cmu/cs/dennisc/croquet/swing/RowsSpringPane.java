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
package edu.cmu.cs.dennisc.croquet.swing;

public abstract class RowsSpringPane extends edu.cmu.cs.dennisc.croquet.swing.Pane {
	private int xPad;
	private int yPad;
	public RowsSpringPane( int xPad, int yPad ) {
		this.xPad = xPad;
		this.yPad = yPad;
	}
	@Override
	public void addNotify() {
		if( getLayout() instanceof javax.swing.SpringLayout ) {
			//pass
		} else {
			java.util.List< java.awt.Component[] > componentRows = this.createComponentRows();
			edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( this, componentRows, this.xPad, this.yPad );
		}
		super.addNotify();
	}
	protected abstract java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv );
	private java.util.List< java.awt.Component[] > createComponentRows() {
		return addComponentRows( new java.util.LinkedList< java.awt.Component[] >() );
	}
}
