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
package edu.cmu.cs.dennisc.alice.ide.editors.type;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberStatementTemplatePane extends StatementTemplatePane {
	private edu.cmu.cs.dennisc.alice.ast.AbstractMember member;
	private edu.cmu.cs.dennisc.alice.ide.editors.common.NodeNameLabel nameLabel;
	private InstanceOrTypeExpressionPane instanceOrTypeExpressionPane = new InstanceOrTypeExpressionPane();
	
	public MemberStatementTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		this.member = member;
		this.nameLabel = new edu.cmu.cs.dennisc.alice.ide.editors.common.NodeNameLabel( member );
		edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().addIDEListener( new edu.cmu.cs.dennisc.alice.ide.event.IDEAdapter() {
			@Override
			public void fieldSelectionChanged( edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent e ) {
				MemberStatementTemplatePane.this.instanceOrTypeExpressionPane.updateLabel();
			}
			@Override
			public void focusedCodeChanged( edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent e ) {
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
	public edu.cmu.cs.dennisc.alice.ide.editors.common.NodeNameLabel getNameLabel() {
		return this.nameLabel;
	}
	@Override
	protected java.lang.Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getStatementClass() {
		return edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class;
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
			g2.setPaint( PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
}
