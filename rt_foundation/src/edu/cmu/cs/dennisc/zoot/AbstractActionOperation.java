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
public abstract class AbstractActionOperation extends AbstractOperation implements ActionOperation {
	private javax.swing.Action actionForConfiguringSwingComponents = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};
	private javax.swing.ButtonModel buttonModel = new javax.swing.DefaultButtonModel();
	public AbstractActionOperation( java.util.UUID groupUUID ) {
		super( groupUUID );
		this.buttonModel.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				AbstractActionOperation.this.handleActionPerformed( e );
			}
		} );
	}
	public javax.swing.Action getActionForConfiguringSwing() {
		return this.actionForConfiguringSwingComponents;
	}
	public javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}
	protected void putValue( String key, Object value ) {
		this.actionForConfiguringSwingComponents.putValue( key, value );
	}
	protected void handleActionPerformed( java.awt.event.ActionEvent e ) {
		ZManager.performIfAppropriate( this, e, ZManager.CANCEL_IS_WORTHWHILE );
	}
}
