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
public class CountLoop extends AbstractLoop {
	public DeclarationProperty< VariableDeclaredInAlice > variable = new DeclarationProperty< VariableDeclaredInAlice >( this ) {
		@Override
		public boolean isReference() {
			return false;
		}
	};
	public DeclarationProperty< ConstantDeclaredInAlice > constant = new DeclarationProperty< ConstantDeclaredInAlice >( this ) {
		@Override
		public boolean isReference() {
			return false;
		}
	};
	public ExpressionProperty count = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return TypeDeclaredInJava.get( Integer.class );
		}
	};
	public CountLoop() {
	}
	public CountLoop( VariableDeclaredInAlice variable, ConstantDeclaredInAlice constant, Expression count, BlockStatement body ) {
		super( body );
		this.variable.setValue( variable );
		this.constant.setValue( constant );
		this.count.setValue( count );
	}
	@Override
	protected void postDecode() {
		super.postDecode();
		if( this.variable.getValue() == null ) {
			this.variable.setValue( new VariableDeclaredInAlice( null, Integer.class ) );
		}
		if( this.constant.getValue() == null ) {
			this.constant.setValue( new ConstantDeclaredInAlice( null, Integer.class ) );
		}
	}
}
