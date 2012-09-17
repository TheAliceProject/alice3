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

package org.alice.ide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class RootGalleryNode extends GalleryNode {
	private static class SingletonHolder {
		private static RootGalleryNode instance = new RootGalleryNode();
	}

	public static RootGalleryNode getInstance() {
		return SingletonHolder.instance;
	}

	private RootGalleryNode() {
		super( java.util.UUID.fromString( "9c1a9783-f865-446d-b1ef-268e266d6230" ) );
	}

	public org.lgna.project.ast.AbstractConstructor getConstructorForArgumentType( org.lgna.project.ast.AbstractType<?, ?, ?> argumentType ) {
		for( org.lgna.project.ast.NamedUserType userType : this.getDeclarationChildren() ) {
			org.lgna.project.ast.AbstractConstructor constructor = userType.getDeclaredConstructors().get( 0 );
			org.lgna.project.ast.AbstractParameter parameter = constructor.getRequiredParameters().get( 0 );
			if( parameter.getValueType().isAssignableFrom( argumentType ) ) {
				return constructor;
			}
		}
		return null;
	}

	@Override
	public String getSearchText() {
		return "";
	}

	private java.util.List<org.lgna.project.ast.NamedUserType> getDeclarationChildren() {
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = org.alice.ide.ApiConfigurationManager.EPIC_HACK_getActiveInstance();
		return org.alice.ide.typemanager.TypeManager.getNamedUserTypesFromSuperTypes( apiConfigurationManager.getTopLevelGalleryTypes() );
	}

	@Override
	public GalleryNode getParent() {
		return null;
	}

	@Override
	public int getChildCount() {
		return this.getDeclarationChildren().size();
	}

	@Override
	public GalleryNode getChild( int index ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = this.getDeclarationChildren().get( index );
		org.lgna.project.ast.AbstractConstructor constructor = type.getDeclaredConstructors().get( 0 );
		org.lgna.project.ast.AbstractParameter parameter = constructor.getRequiredParameters().get( 0 );
		return ArgumentTypeGalleryNode.getInstance( parameter.getValueType() );
	}

	@Override
	public int getIndexOfChild( GalleryNode child ) {
		return this.getDeclarationChildren().indexOf( ( (TypeGalleryNode)child ).getDeclaration() );
	}

	@Override
	public String getText() {
		return "all models";
	}

	@Override
	public javax.swing.Icon getSmallIcon() {
		return org.alice.ide.icons.Icons.FOLDER_ICON_SMALL;
	}

	@Override
	public javax.swing.Icon getLargeIcon() {
		return org.alice.ide.icons.Icons.FOLDER_BACK_ICON_LARGE;
	}

	@Override
	public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
		return null;
	}

	@Override
	public org.lgna.croquet.Model getLeftButtonClickModel() {
		return null;
	}

	@Override
	public String[] getTags() {
		return null;
	}
}
