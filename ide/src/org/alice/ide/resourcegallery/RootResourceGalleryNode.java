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
public class RootResourceGalleryNode extends org.alice.ide.croquet.models.gallerybrowser.GalleryNode {
	private static class SingletonHolder {
		private static RootResourceGalleryNode instance = new RootResourceGalleryNode();
	}

	public static RootResourceGalleryNode getInstance() {
		return SingletonHolder.instance;
	}

	private final java.util.List<ResourceClassGalleryNode> children;

	private RootResourceGalleryNode() {
		super( java.util.UUID.fromString( "2187b93f-3f9b-49d6-aee6-58f73e082653" ) );

		//note: when there are multiple apis we will need to have a root for each of them
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = org.alice.ide.ApiConfigurationManager.EPIC_HACK_getActiveInstance();
		java.util.List<org.lgna.project.ast.JavaType> topLevelGalleryResourceTypes = apiConfigurationManager.getTopLevelGalleryTypes();
		java.util.List<ResourceClassGalleryNode> list = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithMinimumCapacity( topLevelGalleryResourceTypes.size() );
		for( org.lgna.project.ast.JavaType resourceType : topLevelGalleryResourceTypes ) {
			list.add( ResourceClassGalleryNode.getInstance( resourceType ) );
		}
		this.children = java.util.Collections.unmodifiableList( list );
	}

	@Override
	public String getSearchText() {
		return "";
	}

	@Override
	public org.alice.ide.croquet.models.gallerybrowser.GalleryNode getParent() {
		return null;
	}

	@Override
	public int getChildCount() {
		return this.children.size();
	}

	@Override
	public org.alice.ide.croquet.models.gallerybrowser.GalleryNode getChild( int index ) {
		return this.children.get( index );
	}

	@Override
	public int getIndexOfChild( org.alice.ide.croquet.models.gallerybrowser.GalleryNode child ) {
		return this.children.indexOf( child );
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
