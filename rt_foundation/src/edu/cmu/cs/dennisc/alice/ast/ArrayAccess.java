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
public class ArrayAccess extends Expression {
	public DeclarationProperty< AbstractType > arrayType = new DeclarationProperty< AbstractType >( this );
	public ExpressionProperty array = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			AbstractType arrayType = ArrayAccess.this.arrayType.getValue();
			assert arrayType != null;
			return arrayType;
		}
	};
	public ExpressionProperty index = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return TypeDeclaredInJava.get( Integer.class );
		}
	};

	public ArrayAccess() {
	}
	public ArrayAccess( AbstractType arrayType, Expression array, Expression index ){
		assert arrayType.isArray();
		this.arrayType.setValue( arrayType );
		this.array.setValue( array );
		this.index.setValue( index );
	}
	public ArrayAccess( Class<?> arrayCls, Expression array, Expression index ){
		this( TypeDeclaredInJava.get( arrayCls ), array, index );
	}
	@Override
	public AbstractType getType() {
		AbstractType arrayType = this.arrayType.getValue();
		assert arrayType != null;
		return arrayType.getComponentType();
	}
}
