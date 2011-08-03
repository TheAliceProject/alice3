/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.formatter;

/**
 * @author Dennis Cosgrove
 */
public abstract class Formatter {
	private java.util.Locale locale;
	private String repr;
	public Formatter( java.util.Locale locale, String repr ) {
		this.locale = locale;
		this.repr = repr;
	}
	@Deprecated
	public java.util.Locale getLocale() {
		return this.locale;
	}
	
	
	protected abstract String getTextForMethodReflectionProxy( org.lgna.project.ast.MethodReflectionProxy methodReflectionProxy );
	protected abstract String getTextForParameterDeclaredInJava( org.lgna.project.ast.ParameterDeclaredInJava parameterInJava );
	public String getNameForDeclaration( org.lgna.project.ast.AbstractDeclaration declaration ) {
		if (declaration instanceof org.lgna.project.ast.MethodDeclaredInJava) {
			org.lgna.project.ast.MethodDeclaredInJava methodInJava = (org.lgna.project.ast.MethodDeclaredInJava) declaration;
			return this.getTextForMethodReflectionProxy( methodInJava.getMethodReflectionProxy() );
		} else if( declaration instanceof org.lgna.project.ast.ParameterDeclaredInJava ) {
			org.lgna.project.ast.ParameterDeclaredInJava parameterInJava = (org.lgna.project.ast.ParameterDeclaredInJava)declaration;
			return this.getTextForParameterDeclaredInJava( parameterInJava );
		} else if( declaration instanceof org.lgna.project.ast.AbstractType<?,?,?> ) {
			org.lgna.project.ast.AbstractType<?,?,?> type = (org.lgna.project.ast.AbstractType<?,?,?>)declaration;
			return this.getTextForType( type );
		} else {
			return declaration.getName();
		}
	}
	
	
	public abstract String getNameForField( java.lang.reflect.Field fld );
	
	public abstract boolean isTypeExpressionDesired();

	public abstract String getTextForThis();
	public abstract String getTextForNull();
	protected abstract String getTextForCls( Class<?> cls );
	public String getTextForType(org.lgna.project.ast.AbstractType<?, ?, ?> type) {
		if( type != null ) {
			if( type.isArray() ) {
				return this.getTextForType( type.getComponentType() ) + "[]";
			} else {
				if (type instanceof org.lgna.project.ast.TypeDeclaredInJava) {
					org.lgna.project.ast.TypeDeclaredInJava typeInJava = (org.lgna.project.ast.TypeDeclaredInJava) type;
					Class<?> cls = typeInJava.getClassReflectionProxy().getReification();
					return this.getTextForCls( cls );
				} else {
					return type.getName();
				}
			}
		} else {
			return this.getTextForNull();
		}
	}
	@Override
	public String toString() {
		return this.repr;
	}
}
