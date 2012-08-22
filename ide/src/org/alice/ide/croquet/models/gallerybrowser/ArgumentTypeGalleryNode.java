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
public class ArgumentTypeGalleryNode extends TypeGalleryNode {
	private static java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, ArgumentTypeGalleryNode> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static ArgumentTypeGalleryNode getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		ArgumentTypeGalleryNode rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new ArgumentTypeGalleryNode( type );
			map.put( type, rv );
		}
		return rv;
	}

	private ArgumentTypeGalleryNode( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		super( java.util.UUID.fromString( "22829f96-159f-49c7-805e-80e9587a174f" ), type );
	}

	private org.lgna.project.ast.AbstractType<?, ?, ?> getParentDeclaration( org.alice.ide.ApiConfigurationManager api ) {
		return api.getGalleryResourceParentFor( this.getDeclaration() );
	}

	@Override
	public final GalleryNode getParent() {
		org.lgna.project.ast.AbstractType<?, ?, ?> parentType = this.getParentDeclaration( org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager() );
		if( parentType != null ) {
			return ArgumentTypeGalleryNode.getInstance( parentType );
		} else {
			return RootGalleryNode.getInstance();
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
			return ResourceCascade.getInstance( this.getDeclaration(), dropSite );
		}
	}

	@Override
	public org.lgna.croquet.Model getLeftButtonClickModel() {
		return org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance().getSelectionOperationFor( this );
	}
}
