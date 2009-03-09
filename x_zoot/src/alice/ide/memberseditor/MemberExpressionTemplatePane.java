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
package alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberExpressionTemplatePane extends ExpressionTemplatePane {
	private edu.cmu.cs.dennisc.alice.ast.AbstractMember member;
	private alice.ide.ast.NodeNameLabel nameLabel;
	private InstanceOrTypeExpressionPane instanceOrTypeExpressionPane = new InstanceOrTypeExpressionPane();
	
	public MemberExpressionTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		this.member = member;
		this.nameLabel = new alice.ide.ast.NodeNameLabel( member );
		this.nameLabel.scaleFont( 1.5f );
		alice.ide.IDE.getSingleton().addIDEListener( new alice.ide.event.IDEAdapter() {
			@Override
			public void fieldSelectionChanged( alice.ide.event.FieldSelectionEvent e ) {
				MemberExpressionTemplatePane.this.instanceOrTypeExpressionPane.updateLabel();
			}
			@Override
			public void focusedCodeChanged( alice.ide.event.FocusedCodeChangeEvent e ) {
				MemberExpressionTemplatePane.this.instanceOrTypeExpressionPane.updateLabel();
			}
		} );
		this.instanceOrTypeExpressionPane.updateLabel();
		
		//todo
		this.setToolTipText( "return type: " + this.getExpressionType().getName() );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractMember getMember() {
		return this.member;
	}
	protected InstanceOrTypeExpressionPane getInstanceOrTypeExpressionPane() {
		return this.instanceOrTypeExpressionPane;
	}
	public alice.ide.ast.NodeNameLabel getNameLabel() {
		return this.nameLabel;
	}
	@Override
	public java.awt.Point getToolTipLocation( java.awt.event.MouseEvent e ) {
		//todo
		return new java.awt.Point( e.getX(), this.getHeight() + 4 );
	}
}
