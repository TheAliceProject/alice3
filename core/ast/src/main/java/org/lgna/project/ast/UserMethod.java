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

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public class UserMethod extends AbstractUserMethod {
	public edu.cmu.cs.dennisc.property.BooleanProperty isStatic = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isAbstract = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isFinal = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.StringProperty name = new edu.cmu.cs.dennisc.property.StringProperty( this, null );

	public UserMethod() {
	}

	public UserMethod( String name, AbstractType<?, ?, ?> returnType, UserParameter[] requiredParameters, BlockStatement body ) {
		super( returnType, requiredParameters, body );
		this.name.setValue( name );
	}

	public UserMethod( String name, Class<?> returnCls, UserParameter[] requiredParameters, BlockStatement body ) {
		this( name, JavaType.getInstance( returnCls ), requiredParameters, body );
	}

	@Override
	public String getName() {
		return this.name.getValue();
	}

	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return this.name;
	}

	@Override
	public boolean isStatic() {
		return this.isStatic.getValue();
	}

	@Override
	public boolean isAbstract() {
		return this.isAbstract.getValue();
	}

	@Override
	public boolean isFinal() {
		return this.isFinal.getValue();
	}

	//	@Override
	//	public boolean isOverride() {
	//		//todo: this will need to be updated when you can inherit from other TypesDeclaredInAlice
	//		TypeDeclaredInJava typeDeclaredInJava = this.getDeclaringType().getFirstTypeEncounteredDeclaredInJava();
	//		Class<?> clsDeclaredInJava = typeDeclaredInJava.getCls();
	//		Class<?>[] parameterClses = new Class< ? >[ this.parameters.size() ];
	//		int i = 0;
	//		for( AbstractParameter parameter : this.parameters ) {
	//			if( parameter instanceof ParameterDeclaredInJava ) {
	//				ParameterDeclaredInJava parameterDeclaredInJava = (ParameterDeclaredInJava)parameter;
	//				parameterClses[ i ] = parameterDeclaredInJava.getValueTypeDeclaredInJava().getCls();
	//			} else {
	//				return false;
	//			}
	//			i++;
	//		}
	//		java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( clsDeclaredInJava, this.getName(), parameterClses );
	//		return mthd != null;
	//	}

	/* package-private */void appendJava( JavaCodeGenerator generator ) {
		generator.appendMethodHeader( this );
		this.body.getValue().appendJava( generator );
	}

	public String generateHeaderJavaCode( boolean isLambdaSupported ) {
		JavaCodeGenerator generator = new JavaCodeGenerator( isLambdaSupported );
		generator.appendMethodHeader( this );
		return generator.getText();
	}

	public String generateJavaCode( boolean isLambdaSupported ) {
		JavaCodeGenerator generator = new JavaCodeGenerator( isLambdaSupported );
		this.appendJava( generator );
		return generator.getText();
	}
}
