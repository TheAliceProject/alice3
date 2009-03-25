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
public abstract class CascadingUbiquitousStatementTemplate extends org.alice.ide.templates.CascadingExpressionsStatementTemplate implements UbiquitousStatementTemplate {
	private zoot.ZLabel label = new zoot.ZLabel();
	private edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement;
	private java.awt.Component incompleteStatementPane;
	private javax.swing.JToolTip toolTip;
	
	public javax.swing.JComponent getJComponent() {
		return this;
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.Statement getIncompleteStatement() {
		return this.incompleteStatement;
	}
	protected java.awt.Component getIncompleteStatementPane() {
		return this.incompleteStatementPane;
	}
	
	@Override
	public java.awt.Component getSubject() {
		return this.getIncompleteStatementPane();
	}
	public CascadingUbiquitousStatementTemplate( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement ) {
		super( cls );
		this.incompleteStatement = incompleteStatement;
		String text = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "org.alice.ide.ubiquitouspane.Templates" );
		//text = "label: " + text;
		this.label.setText( text );
		this.add( this.label );
		this.setToolTipText( "" );
	}
	public void setToolTip( javax.swing.JToolTip toolTip ) {
		this.toolTip = toolTip;
	}
	@Override
	public javax.swing.JToolTip createToolTip() {
		if( this.toolTip != null ) {
			//pass
		} else {
			this.toolTip = new zoot.ZToolTip( this.incompleteStatementPane );
		}
		return this.toolTip;
	}
	@Override
	public void addNotify() {
		if( this.incompleteStatementPane != null ) {
			//pass
		} else {
			this.incompleteStatementPane = getIDE().getTemplatesFactory().createStatementPane( this.incompleteStatement );
			this.getIDE().addToConcealedBin( this.incompleteStatementPane );
		}
		super.addNotify();
	}
	
	@Override
	public java.awt.Dimension getMinimumSize() {
		java.awt.Dimension rv = super.getMinimumSize();
		rv.width = Math.min( rv.width, 24 );
		return rv;
	}
}
