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
public abstract class TypeGalleryNode extends DeclarationGalleryNode< org.lgna.project.ast.AbstractType< ?,?,? > > {
	private static class CompositeIcon extends edu.cmu.cs.dennisc.javax.swing.icons.DefaultCompositeIcon {
		public CompositeIcon( javax.swing.Icon icon ) {
			super( 
					org.alice.ide.icons.Icons.FOLDER_BACK_ICON_LARGE,
					icon,
					org.alice.ide.icons.Icons.FOLDER_FRONT_ICON_LARGE 
			);
		}
	}
	
	private static java.util.Set<Class<? extends org.lgna.story.resources.JointedModelResource>> setOfClassesWithIcons = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( 
			org.lgna.story.resources.BipedResource.class, 
			org.lgna.story.resources.FishResource.class, 
			org.lgna.story.resources.PropResource.class, 
			org.lgna.story.resources.QuadrupedResource.class, 
			org.lgna.story.resources.SwimmerResource.class, 
			org.lgna.story.resources.MarineMammalResource.class 
	);
	
	public static java.util.Set<Class<? extends org.lgna.story.resources.JointedModelResource>> getSetOfClassesWithIcons() {
		return setOfClassesWithIcons;
	}
	
	private static javax.swing.ImageIcon getIcon( org.lgna.project.ast.AbstractType< ?,?,? > type, boolean isOffset ) {
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
	
	public static javax.swing.ImageIcon getIcon( org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		return getIcon( type, false );
	}
	public static javax.swing.ImageIcon getIcon( Class<?> cls ) {
		return getIcon( org.lgna.project.ast.JavaType.getInstance( cls ) );
	}
	public static javax.swing.Icon getOffsetIcon( org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		javax.swing.Icon icon = getIcon( type, true );
		if( icon != null ) {
			//pass
		} else {
			java.awt.image.BufferedImage image = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail(type.getFirstEncounteredJavaType().getClassReflectionProxy().getReification());
			if( image != null ) {
				//icon = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( image );
				icon = new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( image, 120, 90 );
			}
		}
		return icon;
	}
	
	private final javax.swing.Icon largeIcon;
	public TypeGalleryNode( java.util.UUID id, org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		super( id, type );
		javax.swing.Icon icon = getOffsetIcon( type );
		if( icon != null ) {
			this.largeIcon = new CompositeIcon(icon);
		} else {
			this.largeIcon = org.alice.ide.icons.Icons.FOLDER_BACK_ICON_LARGE;
		}
	}
	
	private java.util.List<org.lgna.project.ast.AbstractDeclaration> children;
	
	protected abstract java.util.List< org.lgna.project.ast.AbstractDeclaration > getDeclarationChildren( org.alice.ide.ApiConfigurationManager api );
	private java.util.List< org.lgna.project.ast.AbstractDeclaration > getDeclarationChildren() {
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
	public GalleryNode getChild( int index ) {
		return getDeclarationNodeInstance( this.getDeclarationChildren().get( index ) );
	}
	@Override
	public int getIndexOfChild( GalleryNode child ) {
		return this.getDeclarationChildren().indexOf( ((DeclarationGalleryNode<?>)child).getDeclaration() );
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
