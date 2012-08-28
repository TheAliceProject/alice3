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
package org.alice.ide.resourcegallery;

/**
 * @author Dennis Cosgrove
 */
abstract class TypeGalleryNode extends DeclarationGalleryNode<org.lgna.project.ast.JavaType> {
	private static class CompositeIcon extends edu.cmu.cs.dennisc.javax.swing.icons.DefaultCompositeIcon {
		public CompositeIcon( javax.swing.Icon icon ) {
			super(
					org.alice.ide.icons.Icons.FOLDER_BACK_ICON_LARGE,
					icon,
					org.alice.ide.icons.Icons.FOLDER_FRONT_ICON_LARGE );
		}
	}

	private static javax.swing.ImageIcon getIcon( org.lgna.project.ast.AbstractType<?, ?, ?> type, boolean isOffset ) {
		Class<?> cls = type.getFirstEncounteredJavaType().getClassReflectionProxy().getReification();
		StringBuilder sb = new StringBuilder();
		sb.append( "images/" );
		sb.append( cls.getName().replace( ".", "/" ) );
		if( isOffset ) {
			sb.append( "_offset" );
		}
		sb.append( ".png" );
		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( TypeGalleryNode.class.getResource( sb.toString() ) );
	}

	public static javax.swing.ImageIcon getIcon( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return getIcon( type, false );
	}

	public static javax.swing.ImageIcon getIcon( Class<?> cls ) {
		return getIcon( org.lgna.project.ast.JavaType.getInstance( cls ) );
	}

	public static javax.swing.Icon getOffsetIcon( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		javax.swing.Icon icon = getIcon( type, true );
		if( icon != null ) {
			//pass
		} else {
			java.awt.image.BufferedImage image = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail( type.getFirstEncounteredJavaType().getClassReflectionProxy().getReification() );
			if( image != null ) {
				//icon = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( image );
				icon = new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( image, 120, 90 );
			}
		}
		return icon;
	}

	private final javax.swing.Icon largeIcon;

	public TypeGalleryNode( java.util.UUID id, org.lgna.project.ast.JavaType type ) {
		super( id, type );
		javax.swing.Icon icon = getOffsetIcon( type );
		if( icon != null ) {
			this.largeIcon = new CompositeIcon( icon );
		} else {
			this.largeIcon = org.alice.ide.icons.Icons.FOLDER_BACK_ICON_LARGE;
		}
	}

	private java.util.List<org.lgna.project.ast.AbstractDeclaration> children;

	protected abstract java.util.List<org.lgna.project.ast.AbstractDeclaration> getDeclarationChildren( org.alice.ide.ApiConfigurationManager api );

	private java.util.List<org.lgna.project.ast.AbstractDeclaration> getDeclarationChildren() {
		if( this.children != null ) {
			//pass
		} else {
			org.alice.ide.ApiConfigurationManager apiConfigurationManager = org.alice.ide.ApiConfigurationManager.EPIC_HACK_getActiveInstance();
			this.children = this.getDeclarationChildren( apiConfigurationManager );
		}
		return this.children;
	}

	@Override
	public int getChildCount() {
		return this.getDeclarationChildren().size();
	}

	@Override
	public org.alice.ide.croquet.models.gallerybrowser.GalleryNode getChild( int index ) {
		return getDeclarationNodeInstance( this.getDeclarationChildren().get( index ) );
	}

	@Override
	public int getIndexOfChild( org.alice.ide.croquet.models.gallerybrowser.GalleryNode child ) {
		return this.getDeclarationChildren().indexOf( ( (DeclarationGalleryNode<?>)child ).getDeclaration() );
	}

	@Override
	public javax.swing.Icon getSmallIcon() {
		return org.alice.ide.icons.Icons.FOLDER_ICON_SMALL;
	}

	@Override
	public javax.swing.Icon getLargeIcon() {
		return this.largeIcon;
	}

	@Override
	public String[] getTags() {
		return null;
	}

	@Override
	protected void appendClassName( java.lang.StringBuilder sb ) {
		String name = this.getDeclaration().getName();
		sb.append( name.replace( "Resource", "" ) );
	}
}

public class ResourceClassGalleryNode extends TypeGalleryNode {
	private static java.util.Map<org.lgna.project.ast.JavaType, ResourceClassGalleryNode> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static ResourceClassGalleryNode getInstance( org.lgna.project.ast.JavaType type ) {
		ResourceClassGalleryNode rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new ResourceClassGalleryNode( type );
			map.put( type, rv );
		}
		return rv;
	}

	private ResourceClassGalleryNode( org.lgna.project.ast.JavaType type ) {
		super( java.util.UUID.fromString( "303c11fe-b03e-485f-aa08-2c3fff30aa89" ), type );
	}

	private org.lgna.project.ast.JavaType getParentDeclaration( org.alice.ide.ApiConfigurationManager api ) {
		return api.getGalleryResourceParentFor( this.getDeclaration() );
	}

	@Override
	public final org.alice.ide.croquet.models.gallerybrowser.GalleryNode getParent() {
		org.lgna.project.ast.JavaType parentType = this.getParentDeclaration( org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager() );
		if( parentType != null ) {
			return ResourceClassGalleryNode.getInstance( parentType );
		} else {
			return RootResourceGalleryNode.getInstance();
		}
	}

	private final boolean isPerson() {
		return this.getDeclaration().isAssignableTo( org.lgna.story.resources.sims2.PersonResource.class );
	}

	@Override
	protected java.util.List<org.lgna.project.ast.AbstractDeclaration> getDeclarationChildren( org.alice.ide.ApiConfigurationManager api ) {
		if( this.isPerson() ) {
			return java.util.Collections.emptyList();
		} else {
			return api.getGalleryResourceChildrenFor( this.getDeclaration() );
		}
	}

	@Override
	public javax.swing.Icon getSmallIcon() {
		if( this.isPerson() ) {
			return org.alice.stageide.gallerybrowser.ResourceTab.CREATE_PERSON_SMALL_ICON;
		} else {
			return super.getSmallIcon();
		}
	}

	@Override
	public javax.swing.Icon getLargeIcon() {
		if( this.isPerson() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( this );
			return org.alice.stageide.gallerybrowser.ResourceTab.CREATE_PERSON_LARGE_ICON;
		} else {
			return super.getLargeIcon();
		}
	}

	@Override
	protected void appendClassName( java.lang.StringBuilder sb ) {
		if( this.isPerson() ) {
			sb.append( "MyPerson" );
		} else {
			super.appendClassName( sb );
		}
	}

	@Override
	public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
		if( this.getDeclaration().isAssignableTo( org.lgna.story.resources.sims2.PersonResource.class ) ) {
			return org.alice.stageide.croquet.models.gallerybrowser.CreateFieldFromPersonResourceOperation.getInstance();
		} else {
			return org.alice.ide.croquet.models.gallerybrowser.ResourceCascade.getInstance( this.getDeclaration(), dropSite );
		}
	}

	@Override
	public org.lgna.croquet.Model getLeftButtonClickModel() {
		return org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance().getSelectionOperationFor( this );
	}
}
