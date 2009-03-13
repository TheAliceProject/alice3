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
public class ArrayAccessTemplatePane extends MemberExpressionTemplatePane {
	public ArrayAccessTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		Class< ? extends edu.cmu.cs.dennisc.alice.ast.Node > cls = edu.cmu.cs.dennisc.alice.ast.ArrayAccess.class;
		this.setBackground( org.alice.ide.IDE.getColorForASTClass( cls ) );
		this.add( this.getInstanceOrTypeExpressionPane() );
		this.add( new zoot.ZLabel( "." ) );
		this.add( this.getNameLabel() );
		this.add( new zoot.ZLabel( "[" ) );
		this.add( new org.alice.ide.ast.EmptyExpressionPane( this.getExpressionType() ) );
		this.add( new zoot.ZLabel( "]" ) );
		this.add( javax.swing.Box.createHorizontalGlue() );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractField getField() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractField)this.getMember();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return getField().getValueType().getComponentType();
	}
	
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression createExpression( final org.alice.ide.ast.DropAndDropEvent e ) {
		assert javax.swing.SwingUtilities.isEventDispatchThread() == false;
		edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			@Override
			public void run() {
				ArrayAccessTemplatePane.this.getIDE().promptUserForExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, null, e.getEndingMouseEvent(), this );
			}
		};

		edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess();
		fieldAccess.expression.setValue( getIDE().createInstanceExpression() );
		fieldAccess.field.setValue( this.getField() );
		
		edu.cmu.cs.dennisc.alice.ast.ArrayAccess rv = new edu.cmu.cs.dennisc.alice.ast.ArrayAccess( getField().getValueType(), fieldAccess, taskObserver.getResult() );
		return rv;
	}
}
