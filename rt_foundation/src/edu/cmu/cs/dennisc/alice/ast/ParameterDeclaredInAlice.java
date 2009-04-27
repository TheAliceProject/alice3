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
public class ParameterDeclaredInAlice extends AbstractParameter {
	public edu.cmu.cs.dennisc.property.StringProperty name = new edu.cmu.cs.dennisc.property.StringProperty( this, null );
	public DeclarationProperty< AbstractType > valueType = new DeclarationProperty< AbstractType >( this );
	public ParameterDeclaredInAlice() {
	}
	public ParameterDeclaredInAlice( String name, AbstractType valueType ) {
		this.name.setValue( name );
		this.valueType.setValue( valueType );
	}
	public ParameterDeclaredInAlice( String name, Class< ? > valueCls ) {
		this( name, TypeDeclaredInJava.get( valueCls ) );
	}
	@Override
	public boolean isDeclaredInAlice() {
		return true;
	}
	@Override
	public String getName() {
		return name.getValue();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return this.name;
	}
	@Override
	public AbstractType getValueType() {
		return valueType.getValue();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getDesiredValueType() {
		return getValueType();
	}
	@Override
	public boolean isVariableLength() {
		return false;
	}	
}
