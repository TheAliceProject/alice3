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

package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class TypeDragModel extends org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel {
	private static java.util.Map<org.lgna.project.ast.NamedUserType, TypeDragModel> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static TypeDragModel getInstance( org.lgna.project.ast.NamedUserType type ) {
		TypeDragModel rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new TypeDragModel( type );
			map.put( type, rv );
		}
		return rv;
	}

	private final org.lgna.project.ast.NamedUserType type;
	private final javax.swing.Icon largeIcon;

	private TypeDragModel( org.lgna.project.ast.NamedUserType type ) {
		super( java.util.UUID.fromString( "547192e8-12cc-4c62-b05d-8108205c0b06" ) );
		this.type = type;

		//todo: share code
		org.lgna.project.ast.AbstractType<?, ?, ?> snapshotType = org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0Parameter0Type( this.type );
		java.awt.image.BufferedImage thumbnail = null;
		javax.swing.Icon snapshotIcon = null;
		if( snapshotType != null ) {
			if( snapshotType instanceof org.lgna.project.ast.JavaType ) {
				org.lgna.project.ast.JavaType snapShotJavaType = (org.lgna.project.ast.JavaType)snapshotType;
				thumbnail = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail( snapShotJavaType.getClassReflectionProxy().getReification() );
			}
			if( thumbnail != null ) {
				snapshotIcon = new javax.swing.ImageIcon( thumbnail );
			} else {
				snapshotIcon = org.alice.ide.croquet.models.gallerybrowser.TypeGalleryNode.getIcon( snapshotType );
			}
			//snapshotIcon = org.alice.stageide.gallerybrowser.ResourceManager.getLargeIconForType( snapshotType );
			//snapshotText = snapshotType.toString();
		} else {
			org.lgna.project.ast.JavaField field = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( this.type.getDeclaredConstructors().get( 0 ) );
			if( field != null ) {
				thumbnail = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail( field.getValueType().getClassReflectionProxy().getReification() );
				//snapshotText = field.toString();
				if( thumbnail != null ) {
					snapshotIcon = new javax.swing.ImageIcon( thumbnail );
				}
			}
		}

		this.largeIcon = snapshotIcon;
	}

	@Override
	public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
		org.lgna.project.ast.AbstractConstructor constructor = this.type.getDeclaredConstructors().get( 0 );
		java.util.ArrayList<? extends org.lgna.project.ast.AbstractParameter> requiredParameters = constructor.getRequiredParameters();
		if( requiredParameters.size() > 0 ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> parameterType = requiredParameters.get( 0 ).getValueType();
			return org.alice.ide.croquet.models.gallerybrowser.ResourceCascade.getInstance( parameterType, dropSite );
		} else {
			org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( constructor );
			return org.alice.ide.croquet.models.declaration.ArgumentFieldSpecifiedManagedFieldDeclarationOperation.getInstance( argumentField, dropSite );
		}
	}

	@Override
	public org.lgna.croquet.Model getLeftButtonClickModel() {
		return null;
	}

	@Override
	public javax.swing.Icon getLargeIcon() {
		return this.largeIcon;
	}

	@Override
	public javax.swing.Icon getSmallIcon() {
		return null;
	}

	@Override
	public String getText() {
		return this.type.getName();
	}
}
