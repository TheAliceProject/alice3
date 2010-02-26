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

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class FieldAccess extends Expression {
	public ExpressionProperty expression = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			AbstractField f = field.getValue();
			if( f != null ) {
				return f.getDeclaringType();
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: field.getValue() == null" );
				return TypeDeclaredInJava.OBJECT_TYPE;
			}
		}
	};
	public DeclarationProperty< AbstractField > field = new DeclarationProperty< AbstractField >( this );

	public FieldAccess() {
	}
	public FieldAccess( Expression expression, AbstractField field ){
		this.expression.setValue( expression );
		this.field.setValue( field );
	}
	@Override
	public AbstractType getType() {
		return field.getValue().getValueType();
	}
	
	@Override
	protected StringBuffer appendRepr( StringBuffer rv, java.util.Locale locale ) {
		//return super.appendRepr( rv, locale );
		Node.safeAppendRepr( rv, this.field.getValue(), locale );
		return rv;
	}
}
