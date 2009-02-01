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
public class ArrayLengthTemplatePane extends MemberExpressionTemplatePane {
	public ArrayLengthTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		Class< ? extends edu.cmu.cs.dennisc.alice.ast.Node > cls = edu.cmu.cs.dennisc.alice.ast.ArrayLength.class;
		this.setBackground( edu.cmu.cs.dennisc.alice.ide.IDE.getColorForASTClass( cls ) );
		this.add( this.getInstanceOrTypeExpressionPane() );
		this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( "." ) );
		this.add( this.getNameLabel() );
		this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( ".length" ) );
		this.add( javax.swing.Box.createHorizontalGlue() );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractField getField() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractField)this.getMember();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE;
	}
	
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression createExpression( final edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent e ) {
		edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess();
		fieldAccess.expression.setValue( getIDE().createInstanceExpression() );
		fieldAccess.field.setValue( this.getField() );
		
		edu.cmu.cs.dennisc.alice.ast.ArrayLength rv = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( fieldAccess );
		return rv;
	}
}
