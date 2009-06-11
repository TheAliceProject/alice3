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
public abstract class CascadingUbiquitousStatementTemplate extends org.alice.ide.templates.CascadingExpressionsStatementTemplate {
	private UbiquitousStatementImplementor implementor;
	private zoot.ZLabel label;
	public CascadingUbiquitousStatementTemplate( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement ) {
		super( cls );
		this.implementor = new UbiquitousStatementImplementor( incompleteStatement );
	}
	
	protected String getLabelText() {
		return this.implementor.getLabelText();
	}
	protected void updateLabel() {
		this.label.setText( this.getLabelText() );
	}
	
	@Override
	public java.awt.Component getSubject() {
		return this.implementor.getIncompleteStatementPane();
	}
	@Override
	public javax.swing.JToolTip createToolTip() {
		return this.implementor.getToolTip();
	}
	@Override
	public void addNotify() {
		if( this.label != null ) {
			//pass
		} else {
			//this.label = zoot.ZLabel.acquire( "<html><body>" + this.getLabelText() + "</body></html>" );
			this.label = zoot.ZLabel.acquire( this.getLabelText() );
			//this.label = zoot.ZLabel.acquire( "<html><body>\u2334</body></html>" );
			if( edu.cmu.cs.dennisc.alice.ast.Comment.class.isAssignableFrom( this.getStatementCls() ) ) {
				this.label.setForeground( getIDE().getCommentForegroundColor() );
			}
			//this.label.setFontToScaledFont( 1.2f );
			this.add( this.label );
			this.setToolTipText( "" );
			this.getIDE().addToConcealedBin( this.implementor.getIncompleteStatementPane() );
		}
		super.addNotify();
	}
	
	@Override
	public java.awt.Dimension getMinimumSize() {
		return this.implementor.adjustMinimumSize( super.getMinimumSize() );
	}
}
