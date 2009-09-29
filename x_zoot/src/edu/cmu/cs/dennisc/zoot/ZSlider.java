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
public class ZSlider extends javax.swing.JSlider {
	private BoundedRangeOperation boundedRangeOperation;
	public ZSlider( BoundedRangeOperation boundedRangeOperation ) {
		this.boundedRangeOperation = boundedRangeOperation;
		this.setModel( this.boundedRangeOperation.getBoundedRangeModel() );
//		this.addVetoableChangeListener( new java.beans.VetoableChangeListener() {
//			public void vetoableChange( java.beans.PropertyChangeEvent e ) throws java.beans.PropertyVetoException {
//				ZManager.performIfAppropriate( ZSlider.this.boundedRangeOperation, e );
//			}
//		} );
		this.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				ZManager.performIfAppropriate( ZSlider.this.boundedRangeOperation, e, ZManager.CANCEL_IS_FUTILE );
			}
		} );
	}
	public BoundedRangeOperation getBoundedRangeOperation() {
		return this.boundedRangeOperation;
	}
}
