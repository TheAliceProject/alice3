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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class SelectedFieldExpressionPane extends ExpressionLikeSubstance {
	private org.alice.ide.ast.SelectedFieldExpression selectedFieldExpression;
	private zoot.ZLabel label = new zoot.ZLabel();
	public SelectedFieldExpressionPane( org.alice.ide.ast.SelectedFieldExpression selectedFieldExpression ) {
		this.add( this.label );
		this.selectedFieldExpression = selectedFieldExpression;
		this.updateLabel();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = getIDE().getFieldSelection();
		if( field != null ) {
			return field.getValueType();
		} else {
			return null;
		}
	}
	public void updateLabel() {
		org.alice.ide.IDE ide = getIDE();
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = ide.getFieldSelection();
		this.label.setText( getIDE().getInstanceTextForField( field, true ) );
		edu.cmu.cs.dennisc.alice.ast.Expression expression = ide.createInstanceExpression();
		java.awt.Color color;
		if( expression != null ) {
			color = getIDE().getColorForASTInstance( expression );
		} else {
			color = java.awt.Color.LIGHT_GRAY;
		}
		this.setBackground( color );
	}
}
