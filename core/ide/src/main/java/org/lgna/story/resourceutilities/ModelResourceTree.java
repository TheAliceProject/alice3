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

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.ModelResource;

public class ModelResourceTree {
	public ModelResourceTree( List<Class<? extends org.lgna.story.resources.ModelResource>> classes ) {
		this.galleryTree = this.createClassTree( classes );
	}

	public ModelResourceTreeNode getTree() {
		return this.galleryTree;
	}

	public List<ModelResourceTreeNode> getSModelBasedNodes() {
		return this.smodelBasedClasses;
	}

	public List<ModelResourceTreeNode> getRootNodes() {
		LinkedList<ModelResourceTreeNode> rootNodes = new LinkedList<ModelResourceTreeNode>();
		if( this.galleryTree != null ) {
			for( int i = 0; i < this.galleryTree.getChildCount(); i++ ) {
				rootNodes.add( (ModelResourceTreeNode)this.galleryTree.getChildAt( i ) );
			}
		}
		return rootNodes;
	}

	public ModelResourceTreeNode getGalleryResourceTreeNodeForUserType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return this.galleryTree.getDescendantOfUserType( type );
	}

	public ModelResourceTreeNode getGalleryResourceTreeNodeForJavaType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return this.galleryTree.getDescendantOfJavaType( type );
	}

	public List<? extends ModelResourceTreeNode> getGalleryResourceChildrenForJavaType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		ModelResourceTreeNode node = this.galleryTree.getDescendantOfJavaType( type );
		if( node != null ) {
			return node.childrenList();
		} else {
			return new LinkedList<ModelResourceTreeNode>();
		}
	}

	private static UserPackage getAlicePackage( Class<?> resourceClass, Class<?> rootClass ) {
		String resourcePackage = resourceClass.getPackage().getName();
		String rootPackage = rootClass.getPackage().getName();
		int rootIndex = resourcePackage.indexOf( rootPackage );
		if( rootIndex != -1 ) {
			resourcePackage = resourcePackage.substring( rootIndex + rootPackage.length() );
			if( resourcePackage.startsWith( "." ) ) {
				resourcePackage = resourcePackage.substring( 1 );
			}
		}
		resourcePackage = AliceResourceClassUtilities.DEFAULT_PACKAGE + resourcePackage;
		return new UserPackage( resourcePackage );
	}

	//The Stack<Class<?>> classes is a stack of classes representing the hierarchy of the classes, with the parent class at the top of the stack
	private ModelResourceTreeNode addNodes( ModelResourceTreeNode root, Stack<Class<? extends org.lgna.story.resources.ModelResource>> classes ) {
		Class<?> rootClass = null;
		ModelResourceTreeNode currentNode = root;
		while( !classes.isEmpty() ) {
			Class<? extends org.lgna.story.resources.ModelResource> currentClass = classes.pop();
			if( currentClass.isAnnotationPresent( Deprecated.class ) ) {
				continue;
			}

			//The root class is the one at the top of the stack, so grab it the first time around
			if( rootClass == null ) {
				rootClass = currentClass;
			}
			ModelResourceTreeNode parentNode = currentNode;
			ModelResourceTreeNode classNode = null;
			if( resourceClassToNodeMap.containsKey( currentClass ) ) {
				classNode = resourceClassToNodeMap.get( currentClass );
			}
			//Build a new ModelResourceTreeNode for the current class
			if( classNode == null ) {

				NamedUserType aliceType = null;
				String aliceClassName = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getAliceClassName( currentClass );
				UserPackage packageName = getAlicePackage( currentClass, rootClass );

				UserMethod[] noMethods = {};
				UserField[] noFields = {};
				Field[] resourceConstants = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getFieldsOfType( currentClass, org.lgna.story.resources.ModelResource.class );
				Class<? extends org.lgna.story.SModel> modelClass = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getModelClassForResourceClass( currentClass );
				org.lgna.project.ast.AbstractType parentType = null;
				if( ( parentNode == null ) || ( parentNode.getUserType() == null ) ) {
					parentType = JavaType.getInstance( modelClass );
				} else {
					parentType = parentNode.getUserType();
				}
				NamedUserConstructor[] noConstructors = {};
				aliceType = new NamedUserType( aliceClassName, packageName, parentType, noConstructors, noMethods, noFields );

				classNode = new ModelResourceTreeNode( aliceType, currentClass, modelClass );
				resourceClassToNodeMap.put( currentClass, classNode );
				if( root == null ) { //if the root node passed in is null, assign it to be the node from the first class we process
					root = classNode;
				}
				if( classNode.hasModelClass() ) {
					this.smodelBasedClasses.add( classNode );
				}
				if( resourceConstants.length != 0 ) {
					for( Field f : resourceConstants ) {
						String fieldClassName = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getClassNameFromName( f.getName() ) + aliceClassName;
						NamedUserType subParentType = classNode.getUserType();
						NamedUserType fieldType = new NamedUserType( fieldClassName, packageName, subParentType, noConstructors, noMethods, noFields );
						ModelResourceTreeNode fieldNode = new ModelResourceTreeNode( fieldType, currentClass, null );
						try {
							ModelResource resource = (ModelResource)f.get( null );
							fieldNode.setModelResourceInstance( resource );
							JavaField javaField = JavaField.getInstance( f );
							fieldNode.setJavaField( javaField );
						} catch( Exception e ) {
							e.printStackTrace();
						}
						fieldNode.setParent( classNode );
						resourceClassToNodeMap.put( f, fieldNode );
					}
				}
			}
			classNode.setParent( parentNode );
			currentNode = classNode;
		}
		return root;
	}

	private ModelResourceTreeNode createClassTree( List<Class<? extends org.lgna.story.resources.ModelResource>> classes ) {
		ModelResourceTreeNode topNode = new ModelResourceTreeNode( null, null, null );

		for( Class<? extends org.lgna.story.resources.ModelResource> cls : classes ) {
			Class<? extends org.lgna.story.resources.ModelResource> currentClass = cls;
			Stack<Class<? extends org.lgna.story.resources.ModelResource>> classStack = new Stack<Class<? extends org.lgna.story.resources.ModelResource>>();
			Class<?>[] interfaces = null;
			while( currentClass != null ) {
				classStack.push( currentClass );
				boolean isTopLevelResource = org.lgna.story.implementation.alice.AliceResourceClassUtilities.isTopLevelResource( currentClass );
				if( isTopLevelResource ) {
					break;
				}

				interfaces = currentClass.getInterfaces();
				currentClass = null;
				if( ( interfaces != null ) && ( interfaces.length > 0 ) ) {
					for( Class<?> intrfc : interfaces ) {
						if( org.lgna.story.resources.ModelResource.class.isAssignableFrom( intrfc ) ) {
							currentClass = (Class<? extends org.lgna.story.resources.ModelResource>)intrfc;
							break;
						}
					}
				}
			}
			addNodes( topNode, classStack );
		}
		return topNode;
	}

	private final ModelResourceTreeNode galleryTree;
	private final Map<Object, ModelResourceTreeNode> resourceClassToNodeMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final List<ModelResourceTreeNode> smodelBasedClasses = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
}
