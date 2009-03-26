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
package zoot;

/**
 * @author Dennis Cosgrove
 */
public class ZCheckBox extends javax.swing.JCheckBox {
	private StateOperation< Boolean > stateOperation;
	public ZCheckBox( StateOperation< Boolean > stateOperation ) {
		this.stateOperation = stateOperation;
		this.setAction( this.stateOperation.getActionForConfiguringSwing() );
//		this.addChangeListener( new javax.swing.event.ChangeListener() {
//			public void stateChanged(javax.swing.event.ChangeEvent e) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( e );
//				//ZManager.performIfAppropriate( ZCheckBox.this.stateOperation, e, ZManager.CANCEL_IS_FUTILE, ZCheckBox.this.stateOperation.getState(), ZCheckBox.this.isSelected() );
//			}
//		} );
		this.addItemListener( new java.awt.event.ItemListener() {
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				boolean prev;
				boolean next;
				if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
					prev = false;
					next = true;
				} else {
					prev = true;
					next = false;
				}
				ZManager.performIfAppropriate( ZCheckBox.this.stateOperation, e, ZManager.CANCEL_IS_FUTILE, prev, next );
			}
		} );
	}
	protected StateOperation< Boolean > getStateOperation() {
		return this.stateOperation;
	}
}
