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
public class ConstructorDeclaredInAlice extends AbstractConstructor implements CodeDeclaredInAlice {
	public edu.cmu.cs.dennisc.property.EnumProperty< Access > access = new edu.cmu.cs.dennisc.property.EnumProperty< Access >( this, Access.PUBLIC );
	public NodeListProperty< ParameterDeclaredInAlice > parameters = new NodeListProperty< ParameterDeclaredInAlice >( this );
	public NodeProperty< BlockStatement > body = new NodeProperty< BlockStatement >( this );
	public edu.cmu.cs.dennisc.property.BooleanProperty isSignatureLocked = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	private AbstractType m_declaringType;
	private edu.cmu.cs.dennisc.alice.annotations.Visibility m_visibility = edu.cmu.cs.dennisc.alice.annotations.Visibility.PRIME_TIME; 
	public edu.cmu.cs.dennisc.property.BooleanProperty isDeletionAllowed = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );

	public ConstructorDeclaredInAlice() {
	}
	public ConstructorDeclaredInAlice( ParameterDeclaredInAlice[] parameters, BlockStatement body ) {
		this.parameters.add( parameters );
		this.body.setValue( body );
	}
	

	public NodeProperty< BlockStatement > getBodyProperty() {
		return this.body;
	}
	public NodeListProperty< ParameterDeclaredInAlice > getParamtersProperty() {
		return this.parameters;
	}
	@Override
	public AbstractType getDeclaringType() {
		return m_declaringType;
	}
	public void setDeclaringType( AbstractType declaringType ) {
		m_declaringType = declaringType;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		return m_visibility;
	}
	public void setVisibility( edu.cmu.cs.dennisc.alice.annotations.Visibility visibility ) {
		m_visibility = visibility;
	}
	
	@Override
	public AbstractMember getNextLongerInChain() {
		return null;
	}
	@Override
	public AbstractMember getNextShorterInChain() {
		return null;
	}
	
	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return parameters.getValue();
	}

	@Override
	public Access getAccess() {
		return this.access.getValue();
	}
}
