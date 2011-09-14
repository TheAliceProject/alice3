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

package org.alice.ide.typemanager;

/**
 * @author Dennis Cosgrove
 */
public class TypeManager {
	public TypeManager() {
		throw new AssertionError();
	}
	
	private static org.lgna.project.Project prevProject;
	private static java.util.Map< org.lgna.project.ast.JavaType, org.lgna.project.ast.NamedUserType > prevProjectMap;
	
	private static org.lgna.project.ast.NamedUserType createTypeFor( org.lgna.project.ast.JavaType javaType ) {
		org.lgna.project.ast.NamedUserType rv = new org.lgna.project.ast.NamedUserType();
		rv.name.setValue( "My" + javaType.getName() ); //todo
		rv.superType.setValue( javaType );
		
		for( org.lgna.project.ast.JavaConstructor javaConstructor : javaType.getDeclaredConstructors() ) {
			java.util.ArrayList< ? extends org.lgna.project.ast.AbstractParameter > javaParameters = javaConstructor.getParameters();
			
			org.lgna.project.ast.NamedUserConstructor userConstructor = new org.lgna.project.ast.NamedUserConstructor();
			org.lgna.project.ast.ConstructorBlockStatement body = new org.lgna.project.ast.ConstructorBlockStatement();
			org.lgna.project.ast.SuperConstructorInvocationStatement superConstructorInvocationStatement = new org.lgna.project.ast.SuperConstructorInvocationStatement();

			superConstructorInvocationStatement.contructor.setValue( javaConstructor );
			final int N = javaParameters.size();
			for( int i=0; i<N; i++ ) {
				org.lgna.project.ast.AbstractParameter javaParameterI = javaParameters.get( i );
				String name = javaParameterI.getName(); //todo?
				if( name != null ) {
					//pass
				} else {
					name = "p"+i;
				}
				org.lgna.project.ast.UserParameter userParameterI = new org.lgna.project.ast.UserParameter( name, javaParameterI.getValueType() );
				userConstructor.parameters.add( userParameterI );
				superConstructorInvocationStatement.arguments.add( new org.lgna.project.ast.Argument( javaParameterI, new org.lgna.project.ast.ParameterAccess( userParameterI ) ) );
			}
			
			body.constructorInvocationStatement.setValue( superConstructorInvocationStatement );
			userConstructor.body.setValue( body );
			
			rv.constructors.add( userConstructor );
		}
		return rv;
	}
	public static java.util.List< org.lgna.project.ast.NamedUserType > getNamedUserTypesFor( java.util.List< org.lgna.project.ast.JavaType > javaTypes ) {
		java.util.ArrayList< org.lgna.project.ast.NamedUserType > rv = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithMinimumCapacity( javaTypes.size() );
		for( org.lgna.project.ast.JavaType javaType : javaTypes ) {
			rv.add( getNamedUserTypeFor( javaType ) );
		}
		return rv;
	}
	public static org.lgna.project.ast.NamedUserType getNamedUserTypeFor( org.lgna.project.ast.JavaType javaType ) {
		org.lgna.project.Project project = org.alice.ide.IDE.getActiveInstance().getProject();
		java.util.Set< org.lgna.project.ast.NamedUserType > types = project.getNamedUserTypes();
		for( org.lgna.project.ast.NamedUserType type : types ) {
			if( type.getSuperType() == javaType ) {
				return type;
			}
		}
		if( project == prevProject ) {
			org.lgna.project.ast.NamedUserType prevType = prevProjectMap.get( javaType );
			if( prevType != null ) {
				return prevType;
			}
		} else {
			prevProjectMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			prevProject = project;
		}
		
		org.lgna.project.ast.NamedUserType rv = createTypeFor( javaType );
		prevProjectMap.put( javaType, rv );
		return rv;
	}
}
