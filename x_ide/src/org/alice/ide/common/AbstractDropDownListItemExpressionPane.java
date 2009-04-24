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
public abstract class AbstractDropDownListItemExpressionPane extends org.alice.ide.common.AbstractDropDownPane {
	private int index;
	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.Expression > listPropertyAdapter = new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.Expression >() {
		@Override
		protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
		}
		@Override
		protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
			AbstractDropDownListItemExpressionPane.this.refresh();
		}
	};
	public AbstractDropDownListItemExpressionPane( int index, edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty ) {
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.index = index;
		this.expressionListProperty = expressionListProperty;
		this.setLeftButtonPressOperation( new org.alice.ide.operations.ast.FillInExpressionListPropertyItemOperation( this.index, this.expressionListProperty ) {
			@Override
			protected edu.cmu.cs.dennisc.alice.ast.AbstractType getFillInType() {
				return AbstractDropDownListItemExpressionPane.this.getFillInType();
			}
		});
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.expressionListProperty.addListPropertyListener( this.listPropertyAdapter );
	}
	@Override
	public void removeNotify() {
		this.expressionListProperty.removeListPropertyListener( this.listPropertyAdapter );
		super.removeNotify();
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getFillInType();
	public void refresh() {
		edu.cmu.cs.dennisc.swing.ForgetUtilities.forgetAndRemoveAllComponents( this );
		if( this.index < this.expressionListProperty.size() ) {
			this.add( org.alice.ide.IDE.getSingleton().getCodeFactory().createExpressionPane( this.expressionListProperty.get( this.index ) ) );
		}
	}
}

