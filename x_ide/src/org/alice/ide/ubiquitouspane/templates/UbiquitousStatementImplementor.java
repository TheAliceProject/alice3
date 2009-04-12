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
package org.alice.ide.ubiquitouspane.templates;

/**
 * @author Dennis Cosgrove
 */
public class UbiquitousStatementImplementor { //todo: needs a better name
	private edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement;
	private javax.swing.JComponent incompleteStatementPane;
	private String labelText;
	private javax.swing.JToolTip toolTip;

	public UbiquitousStatementImplementor( edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement ) {
		this.incompleteStatement = incompleteStatement;
	}
	public edu.cmu.cs.dennisc.alice.ast.Statement getIncompleteStatement() {
		return this.incompleteStatement;
	}
	public javax.swing.JComponent getIncompleteStatementPane() {
		if( this.incompleteStatementPane != null ) {
			//pass
		} else {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			this.incompleteStatementPane = ide.getTemplatesFactory().createStatementPane( incompleteStatement );
		}
		return this.incompleteStatementPane;
	}
	public String getLabelText() {
		if( this.labelText != null ) {
			//pass
		} else {
			Class<?> cls = incompleteStatement.getClass();
			this.labelText = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "org.alice.ide.ubiquitouspane.Templates" );
		}
		return this.labelText;
	}
	public javax.swing.JToolTip getToolTip() {
		if( this.toolTip != null ) {
			//pass
		} else {
			this.toolTip = new zoot.ZToolTip( this.getIncompleteStatementPane() );
		}
		return this.toolTip;
	}
	public java.awt.Dimension adjustMinimumSize( java.awt.Dimension rv ) {
		rv.width = Math.min( rv.width, 24 );
		return rv;
	}
	
}
