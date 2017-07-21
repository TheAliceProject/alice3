/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.typemanager;

/**
 * @author Dennis Cosgrove
 */
@Deprecated
public class ConstructorArgumentUtilities {
	private ConstructorArgumentUtilities() {
		throw new AssertionError();
	}

	public static org.lgna.project.ast.AbstractConstructor getContructor0( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			java.util.List<? extends org.lgna.project.ast.AbstractConstructor> constructors = type.getDeclaredConstructors();
			if( constructors.size() > 0 ) {
				org.lgna.project.ast.AbstractConstructor constructor0 = constructors.get( 0 );
				return constructor0;
			}
		}
		return null;
	}

	public static org.lgna.project.ast.AbstractType<?, ?, ?> getParameter0Type( org.lgna.project.ast.AbstractConstructor constructor ) {
		if( constructor != null ) {
			java.util.List<? extends org.lgna.project.ast.AbstractParameter> requiredParameters = constructor.getRequiredParameters();
			if( requiredParameters.size() > 0 ) {
				org.lgna.project.ast.AbstractParameter parameter0 = requiredParameters.get( 0 );
				return parameter0.getValueType();
			}
		}
		return null;
	}

	public static org.lgna.project.ast.AbstractType<?, ?, ?> getContructor0Parameter0Type( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return getParameter0Type( getContructor0( type ) );
	}

	public static org.lgna.project.ast.JavaField getField( org.lgna.project.ast.SimpleArgumentListProperty arguments ) {
		if( arguments.size() > 0 ) {
			org.lgna.project.ast.Expression expression = arguments.get( 0 ).expression.getValue();
			if( expression instanceof org.lgna.project.ast.FieldAccess ) {
				org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)expression;
				return (org.lgna.project.ast.JavaField)fieldAccess.field.getValue();
			}
		}
		return null;
	}

	public static org.lgna.project.ast.JavaField getArgumentField( org.lgna.project.ast.AbstractConstructor constructor ) {
		if( constructor instanceof org.lgna.project.ast.NamedUserConstructor ) {
			org.lgna.project.ast.NamedUserConstructor namedUserConstructor = (org.lgna.project.ast.NamedUserConstructor)constructor;
			org.lgna.project.ast.ConstructorInvocationStatement constructorInvocationStatement = namedUserConstructor.body.getValue().constructorInvocationStatement.getValue();
			return getField( constructorInvocationStatement.requiredArguments );
		}
		return null;
	}

	public static org.lgna.project.ast.JavaField getArgumentField( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		if( instanceCreation != null ) {
			org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
			if( instanceCreation.requiredArguments.size() == 1 ) {
				return getField( instanceCreation.requiredArguments );
			} else {
				return getArgumentField( constructor );
			}
		} else {
			return null;
		}
	}
}
