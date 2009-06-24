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
package edu.cmu.cs.dennisc.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractOperation implements Operation {
	private javax.swing.Action actionForConfiguringSwingComponents = new javax.swing.AbstractAction() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			AbstractOperation.this.getIDE().performIfAppropriate( AbstractOperation.this, e );
		} 
	};
	public AbstractOperation() {
	}
	public IDE getIDE() {
		return IDE.getSingleton();
	}
	public javax.swing.Action getActionForConfiguringSwingComponents() {
		return this.actionForConfiguringSwingComponents;
	}
	protected void putValue( String key, Object value ) {
		this.actionForConfiguringSwingComponents.putValue( key, value );
	}
}
