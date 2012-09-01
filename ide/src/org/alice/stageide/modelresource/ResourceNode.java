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
package org.alice.stageide.modelresource;

/**
 * @author Dennis Cosgrove
 */
public final class ResourceNode extends org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel {
	private ResourceNode parent;
	private final ResourceKey resourceKey;
	private final java.util.List<ResourceNode> children;
	private final org.lgna.croquet.CascadeBlankChild<ResourceNode> blankChild;

	public ResourceNode( ResourceKey resourceKey, java.util.List<ResourceNode> children ) {
		super( java.util.UUID.fromString( "3829c7ee-e604-4917-9384-2913b5df28b3" ) );
		this.resourceKey = resourceKey;
		for( ResourceNode child : children ) {
			assert child.parent == null : parent;
			child.parent = this;
		}
		this.children = children;
		if( this.resourceKey.isLeaf() ) {
			this.blankChild = new ResourceFillIn( this );
		} else {
			this.blankChild = new ResourceMenuModel( this );
		}
	}

	public ResourceNode getParent() {
		return this.parent;
	}

	public ResourceKey getResourceKey() {
		return this.resourceKey;
	}

	public java.util.List<ResourceNode> getNodeChildren() {
		return this.children;
	}

	@Override
	public String getText() {
		return this.resourceKey.getText();
	}

	@Override
	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		return this.resourceKey.getIconFactory();
	}

	public org.lgna.croquet.CascadeBlankChild<ResourceNode> getAddFieldBlankChild() {
		return this.blankChild;
	}

	@Override
	public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
		if( this.resourceKey instanceof EnumConstantResourceKey ) {
			EnumConstantResourceKey enumConstantResourceKey = (EnumConstantResourceKey)this.resourceKey;
			return org.alice.ide.croquet.models.declaration.ArgumentFieldSpecifiedManagedFieldDeclarationOperation.getInstance( enumConstantResourceKey.getField(), dropSite );
		} else if( this.resourceKey instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)this.resourceKey;
			if( classResourceKey.isLeaf() ) {
				return this.children.get( 0 ).getDropModel( step, dropSite );
			} else {
				//return ResourceCascade.getInstance( classResourceKey.getType(), dropSite );
				return new AddFieldCascade( this, dropSite );
			}
		} else if( this.resourceKey instanceof PersonResourceKey ) {
			PersonResourceKey personResourceKey = (PersonResourceKey)this.resourceKey;
			return org.alice.stageide.croquet.models.gallerybrowser.DeclareFieldFromPersonResourceIteratingOperation.getInstance();
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.croquet.Model getLeftButtonClickModel() {
		if( this.resourceKey instanceof EnumConstantResourceKey ) {
			EnumConstantResourceKey enumConstantResourceKey = (EnumConstantResourceKey)this.resourceKey;
			return this.getDropModel( null, null );
		} else if( this.resourceKey instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)this.resourceKey;
			if( classResourceKey.isLeaf() ) {
				return this.children.get( 0 ).getLeftButtonClickModel();
			} else {
				return ResourceNodeTreeSelectionState.getInstance().getItemSelectionOperation( this );
			}
		} else if( this.resourceKey instanceof PersonResourceKey ) {
			PersonResourceKey personResourceKey = (PersonResourceKey)this.resourceKey;
			return this.getDropModel( null, null );
		} else {
			return null;
		}
	}

}
