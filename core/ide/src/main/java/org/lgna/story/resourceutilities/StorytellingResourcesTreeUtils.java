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
package org.lgna.story.resourceutilities;

import java.io.File;
import java.util.List;

import org.lgna.project.ast.JavaType;

import edu.cmu.cs.dennisc.javax.swing.models.TreeNode;

/**
 * @author Dennis Cosgrove
 */
public enum StorytellingResourcesTreeUtils {
	INSTANCE;

	private ModelResourceTree getGalleryTreeInternal() {
		if( this.galleryTree == null ) {
			List<Class<? extends org.lgna.story.resources.ModelResource>> classes = StorytellingResources.INSTANCE.findAndLoadAliceResourcesIfNecessary();
			this.galleryTree = new ModelResourceTree( classes );
		}
		return this.galleryTree;
	}

	private ModelResourceTreeNode getGalleryResourceTreeNodeForJavaType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return this.getGalleryTreeInternal().getGalleryResourceTreeNodeForJavaType( type );
	}

	public ModelResourceTreeNode getGalleryResourceTreeNodeForUserType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return getGalleryTreeInternal().getGalleryResourceTreeNodeForUserType( type );
	}

	public ModelResourceTreeNode getGalleryTree() {
		return getGalleryTreeInternal().getTree();
	}

	public java.util.List<org.lgna.project.ast.JavaType> getTopLevelGalleryTypes() {
		if( this.rootTypes == null ) {
			List<ModelResourceTreeNode> rootNodes = this.getGalleryTreeInternal().getSModelBasedNodes();
			this.rootTypes = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
			for( ModelResourceTreeNode node : rootNodes ) {
				this.rootTypes.add( node.getUserType().getFirstEncounteredJavaType() );
			}
		}

		return this.rootTypes;
	}

	private void buildGalleryTreeWithJars( File... resourceJars ) {
		java.util.ArrayList<File> jarFiles = new java.util.ArrayList<File>();
		for( File resourceJar : resourceJars ) {
			jarFiles.add( resourceJar );
		}
		List<Class<? extends org.lgna.story.resources.ModelResource>> modelResourceClasses = StorytellingResources.INSTANCE.getAndLoadModelResourceClasses( jarFiles );
		this.galleryTree = new ModelResourceTree( modelResourceClasses );
	}

	public void initializeGalleryTreeWithJars( File... resourceJars ) {
		this.buildGalleryTreeWithJars( resourceJars );
	}

	public org.lgna.project.ast.JavaType getGalleryResourceParentFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		TreeNode<JavaType> child = this.getGalleryResourceTreeNodeForJavaType( type );
		if( child != null ) {
			ModelResourceTreeNode parent = (ModelResourceTreeNode)child.getParent();
			//Go up an extra level if the node we're returning is a node with a single child (this mirrors what is happening in getResourceChildren)
			if( ( parent != null ) && hasSingleLeafChild( parent ) ) {
				parent = (ModelResourceTreeNode)parent.getParent();
			}
			return parent.getResourceJavaType();
		} else {
			return null;
		}
	}

	private boolean hasSingleLeafChild( TreeNode<?> node ) {
		return ( ( node.getChildCount() == 1 ) && ( node.getChildAt( 0 ).getChildCount() == 0 ) );
	}

	public List<org.lgna.project.ast.AbstractDeclaration> getGalleryResourceChildrenFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		//System.out.println( "Getting children for type: " + type );
		java.util.List<org.lgna.project.ast.AbstractDeclaration> toReturn = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
		TreeNode<JavaType> typeNode = this.getGalleryResourceTreeNodeForJavaType( type );
		if( typeNode != null ) {
			for( int i = 0; i < typeNode.getChildCount(); i++ ) {
				TreeNode<JavaType> child = typeNode.getChildAt( i );
				//If the child has a single leaf child, go down a level and return that
				if( hasSingleLeafChild( child ) ) {
					child = child.getChildAt( 0 );
				}
				ModelResourceTreeNode node = (ModelResourceTreeNode)child;
				if( node.isLeaf() && ( node.getJavaField() != null ) ) {
					//System.out.println( "  Returning field: " + node.getJavaField() );
					toReturn.add( node.getJavaField() );
				} else {
					//System.out.println( "  Returning type: " + node.getResourceJavaType() );
					toReturn.add( node.getResourceJavaType() );
				}
			}
		}
		return toReturn;
	}

	private List<org.lgna.project.ast.JavaType> rootTypes = null;
	private ModelResourceTree galleryTree;
}
