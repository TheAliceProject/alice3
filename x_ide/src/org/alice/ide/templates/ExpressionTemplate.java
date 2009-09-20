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
package org.alice.ide.templates;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionTemplate extends org.alice.ide.common.ExpressionCreatorPane {
	public ExpressionTemplate() {
		this.setDragAndDropOperation( new org.alice.ide.operations.DefaultDragAndDropOperation() );
		
		//todo
		this.setPopupOperation( new org.alice.ide.operations.InconsequentialActionOperation() {
			@Override
			protected void performInternal(zoot.ActionContext actionContext) {
				actionContext.cancel();
			}
		} );
	}
//	@Override
//	protected boolean isFauxDragDesired() {
//		return true;
//	}
	
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createIncompleteExpression();
	@Override
	public void addNotify() {
		super.addNotify();
		this.refresh();
	}
	@Override
	public void removeNotify() {
		this.removeAll();
		super.removeNotify();
	}
	protected void refresh() {
		this.removeAll();
		edu.cmu.cs.dennisc.alice.ast.Expression incompleteExpression = this.createIncompleteExpression();
		this.setBackground( getIDE().getColorFor( incompleteExpression ) );
		this.add( getIDE().getTemplatesFactory().createComponent( incompleteExpression ) );
	}
	@Override
	protected boolean isPressed() {
		return false;
	}
	
	@Override
	public boolean contains( int x, int y ) {
		if( getIDE().isSelectedFieldInScope() ) {
			return super.contains( x, y );
		} else {
			return false;
		}
	}
	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		if( getIDE().isSelectedFieldInScope() ) {
			//pass
		} else {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			g2.setPaint( zoot.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
}
