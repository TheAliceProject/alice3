/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.migration;

import java.util.ArrayList;

import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.JavaMethodParameter;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.MethodInvocation;

/**
 * @author Matt May
 */
public class EventAstMigration extends MethodInvocationAstMigration {

	public EventAstMigration() {
		super( new org.lgna.project.Version( "3.1.68.0.0" ),
				new org.lgna.project.Version( "3.1.70.0.0" ) );
	}

	@Override
	protected void migrate( MethodInvocation methodInvocation ) {
		org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
		if( method instanceof org.lgna.project.ast.JavaMethod ) {
			org.lgna.project.ast.JavaMethod javaMethod = (org.lgna.project.ast.JavaMethod)method;
			if( javaMethod.getDeclaringType() == org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SScene.class ) ) {
				String methodName = method.getName();
				if( methodName.equals( "addTimeListener" ) ) {
					System.out.println( "ADD TIME LISTENER" );
					ArrayList<JavaKeyedArgument> keyedParameter = methodInvocation.keyedArguments.getValue();
					for( JavaKeyedArgument arg : keyedParameter ) {
						JavaMethodParameter value = (JavaMethodParameter)arg.parameter.getValue();
						JavaType valueType = (JavaType)value.getValueType();
						System.out.println( "arg: " + valueType.getComponentType() );
					}
					//				} else if( methodName.equals( "addProximityEnterLIstener" ) ) {
					//					System.out.println( "ADD ProximityEnter" );
					//					ArrayList<JavaKeyedArgument> keyedParameter = methodInvocation.keyedArguments.getValue();
					//					for( JavaKeyedArgument arg : keyedParameter ) {
					//						System.out.println( "arg: " + arg );
					//					}
				}
			}
		}
	}
}
