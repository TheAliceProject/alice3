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
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberStatementTemplatePane extends StatementTemplatePane {
	private edu.cmu.cs.dennisc.alice.ast.AbstractMember member;
	private org.alice.ide.ast.NodeNameLabel nameLabel;
	private InstanceOrTypeExpressionPane instanceOrTypeExpressionPane = new InstanceOrTypeExpressionPane();
	
	public MemberStatementTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		super( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class );
		this.member = member;
		this.nameLabel = new org.alice.ide.ast.NodeNameLabel( member );
		this.nameLabel.scaleFont( 1.5f );
		org.alice.ide.IDE.getSingleton().addIDEListener( new org.alice.ide.event.IDEAdapter() {
			@Override
			public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
				MemberStatementTemplatePane.this.instanceOrTypeExpressionPane.updateLabel();
			}
			@Override
			public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
				MemberStatementTemplatePane.this.instanceOrTypeExpressionPane.updateLabel();
			}
		} );
		this.instanceOrTypeExpressionPane.updateLabel();
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractMember getMember() {
		return this.member;
	}
	protected InstanceOrTypeExpressionPane getInstanceOrTypeExpressionPane() {
		return this.instanceOrTypeExpressionPane;
	}
	public org.alice.ide.ast.NodeNameLabel getNameLabel() {
		return this.nameLabel;
	}
	@Override
	protected boolean isActuallyPotentiallyActive() {
		return getIDE().isSelectedFieldInScope();
	}
	
	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		if( getIDE().isSelectedFieldInScope() ) {
			//pass
		} else {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			g2.setPaint( org.alice.ide.lookandfeel.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
}
