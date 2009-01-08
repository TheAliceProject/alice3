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
public abstract class ParameterDeclaredInJava extends AbstractParameter {
	private java.lang.annotation.Annotation[] m_annotations;
	public ParameterDeclaredInJava( java.lang.annotation.Annotation[] annotations ) {
		m_annotations = annotations;
	}
	@Override
	public boolean isDeclaredInAlice() {
		return false;
	}
	@Override
	public boolean isVariableLength() {
		for( java.lang.annotation.Annotation annotation : m_annotations ) {
			if( annotation instanceof edu.cmu.cs.dennisc.lang.ParameterAnnotation ) {
				edu.cmu.cs.dennisc.lang.ParameterAnnotation parameterAnnotation = (edu.cmu.cs.dennisc.lang.ParameterAnnotation)annotation;
				return parameterAnnotation.isVariable();
			}
		}
		return false;
	}
	
	public TypeDeclaredInJava getValueTypeDeclaredInJava() {
		return (TypeDeclaredInJava)getValueType();
	}
	@Override
	public AbstractType getDesiredValueType() {
		for( java.lang.annotation.Annotation annotation : m_annotations ) {
			if( annotation instanceof edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate ) {
				edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate parameterTemplate = (edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate)annotation;
				return TypeDeclaredInJava.get( parameterTemplate.preferredArgumentClass() );
			}
		}
		return getValueType();
	}
	
}
