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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public class GalleryResourceTypeFillIn extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithExpressionBlanks<org.lgna.project.ast.InstanceCreation> {
	private static java.util.Map<org.lgna.project.ast.JavaType, GalleryResourceTypeFillIn> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized GalleryResourceTypeFillIn getInstance( org.lgna.project.ast.JavaType type ) {
		GalleryResourceTypeFillIn rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new GalleryResourceTypeFillIn( type );
			map.put( type, rv );
		}
		return rv;
	}

	private final org.lgna.project.ast.JavaType ancestorType;

	private GalleryResourceTypeFillIn( org.lgna.project.ast.JavaType ancestorType ) {
		super( java.util.UUID.fromString( "281ad60a-090e-4fd8-bb47-da03a2508a4a" ), GalleryResourceBlank.getInstance( org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0Parameter0Type( ancestorType ) ) );
		this.ancestorType = ancestorType;
	}

	@Override
	public String getMenuItemText( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.InstanceCreation, org.lgna.project.ast.Expression> step ) {
		return org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0Parameter0Type( this.ancestorType ).getName();
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.InstanceCreation, org.lgna.project.ast.Expression> step ) {
		return null;
	}

	@Override
	public org.lgna.project.ast.InstanceCreation getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.lgna.project.ast.InstanceCreation, org.lgna.project.ast.Expression> step ) {
		return null;
	}

	@Override
	protected org.lgna.project.ast.InstanceCreation createValue( org.lgna.project.ast.Expression[] expressions ) {
		if( expressions.length == 1 ) {
			org.lgna.project.ast.Expression expression = expressions[ 0 ];
			if( expression instanceof org.lgna.project.ast.FieldAccess ) {
				org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)expression;
				org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
				if( field.isStatic() ) {
					if( field instanceof org.lgna.project.ast.JavaField ) {
						org.lgna.project.ast.JavaField argumentField = (org.lgna.project.ast.JavaField)field;
						org.lgna.project.ast.NamedUserType userType = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromArgumentField( this.ancestorType, argumentField );
						org.lgna.project.ast.NamedUserConstructor constructor = userType.getDeclaredConstructors().get( 0 );
						org.lgna.project.ast.Expression[] argumentExpressions;
						if( constructor.getRequiredParameters().size() == 1 ) {
							argumentExpressions = new org.lgna.project.ast.Expression[] {
									org.lgna.project.ast.AstUtilities.createStaticFieldAccess( argumentField )
							};
						} else {
							argumentExpressions = new org.lgna.project.ast.Expression[] {};
						}
						return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, argumentExpressions );
					}
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( expression );
			}
		}
		return null;
	}
}
