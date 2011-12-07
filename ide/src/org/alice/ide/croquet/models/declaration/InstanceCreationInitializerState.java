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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public class InstanceCreationInitializerState extends org.alice.ide.croquet.models.ExpressionState {
	private final DeclarationOperation<?> owner;
	public InstanceCreationInitializerState( DeclarationOperation<?> owner, org.lgna.project.ast.Expression initialValue ) {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "1e753e61-b420-4315-b654-c7f030537923" ), initialValue );
		this.owner = owner;
	}
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< org.lgna.project.ast.Expression > blankNode ) {
		org.lgna.project.ast.Expression prevExpression = this.getValue();
		if( prevExpression instanceof org.lgna.project.ast.InstanceCreation || prevExpression == null) {
			org.lgna.project.ast.InstanceCreation prevInstanceCreation = (org.lgna.project.ast.InstanceCreation)prevExpression;
			org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( prevInstanceCreation );
			if( argumentField != null ) {
				org.lgna.project.ast.JavaType javaType = prevInstanceCreation.constructor.getValue().getDeclaringType().getFirstTypeEncounteredDeclaredInJava();
				org.lgna.project.ast.NamedUserType userType = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFor( javaType, argumentField );
				org.lgna.project.ast.AbstractType< ?,?,? > type = userType;
				while( type instanceof org.lgna.project.ast.NamedUserType ) {
					org.lgna.project.ast.AbstractConstructor typeConstructor = type.getDeclaredConstructors().get( 0 );
					if( typeConstructor.getRequiredParameters().size() == 1 ) {
						rv.add( InstanceCreationFillInWithPredeterminedFieldAccessArgument.getInstance( typeConstructor, argumentField ) );
					} else {
						rv.add( InstanceCreationFillIn.getInstance( typeConstructor ) );
					}
					type = type.getSuperType();
				}
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			}
			rv.add( ChangeResourceMenuModel.getInstance() );
		}
		return rv;
	}
}
