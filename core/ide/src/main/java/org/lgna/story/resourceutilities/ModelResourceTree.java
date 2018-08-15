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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.tweedle.file.ModelManifest;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.story.SModel;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.ModelResource;

public class ModelResourceTree {
	public ModelResourceTree( List<Class<? extends ModelResource>> classes ) {
		this.galleryTree = this.createClassTree( classes );
	}

	public GalleryResourceTreeNode getTree() {
		return this.galleryTree;
	}

	public List<TypedDefinedGalleryTreeNode> getSModelBasedNodes() {
		return this.smodelBasedClasses;
	}

	public List<TypedDefinedGalleryTreeNode> getRootNodes() {
		LinkedList<TypedDefinedGalleryTreeNode> rootNodes = new LinkedList<TypedDefinedGalleryTreeNode>();
		if( this.galleryTree != null ) {
			for( int i = 0; i < this.galleryTree.getChildCount(); i++ ) {
				rootNodes.add( (TypedDefinedGalleryTreeNode)this.galleryTree.getChildAt( i ) );
			}
		}
		return rootNodes;
	}

	public void addUserModels(List<ModelManifest> userModels) {
		for (ModelManifest userModel : userModels) {
			addUserModel(userModel);

		}
	}

	private void addUserModel( ModelManifest userModel ){
		TypedDefinedGalleryTreeNode parentNode = null;
		for (TypedDefinedGalleryTreeNode classNode : this.smodelBasedClasses) {
			if (classNode.getUserType().getName().equals(userModel.parentClass)) {
				parentNode = classNode;
				break;
			}
		}

		ManifestDefinedGalleryTreeNode manifestNode = new ManifestDefinedGalleryTreeNode(userModel);
		manifestNode.setParent(parentNode);
	}

	public TypedDefinedGalleryTreeNode getGalleryResourceTreeNodeForUserType(AbstractType<?, ?, ?> type ) {
		return this.galleryTree.getDescendantOfUserType( type );
	}

	public TypedDefinedGalleryTreeNode getGalleryResourceTreeNodeForJavaType(AbstractType<?, ?, ?> type ) {
		return this.galleryTree.getDescendantOfJavaType( type );
	}

	public List<? extends GalleryResourceTreeNode> getGalleryResourceChildrenForJavaType( AbstractType<?, ?, ?> type ) {
		TypedDefinedGalleryTreeNode node = this.galleryTree.getDescendantOfJavaType( type );
		if( node != null ) {
			return node.childrenList();
		} else {
			return new LinkedList<TypedDefinedGalleryTreeNode>();
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
	private TypedDefinedGalleryTreeNode addNodes(TypedDefinedGalleryTreeNode root, Stack<Class<? extends ModelResource>> classes ) {
		Class<?> rootClass = null;
		GalleryResourceTreeNode currentNode = root;
		while( !classes.isEmpty() ) {
			Class<? extends ModelResource> currentClass = classes.pop();
			if( currentClass.isAnnotationPresent( Deprecated.class ) ) {
				continue;
			}

			//The root class is the one at the top of the stack, so grab it the first time around
			if( rootClass == null ) {
				rootClass = currentClass;
			}
			GalleryResourceTreeNode parentNode = currentNode;
			TypedDefinedGalleryTreeNode classNode = null;
			if( resourceClassToNodeMap.containsKey( currentClass ) ) {
				classNode = resourceClassToNodeMap.get( currentClass );
			}
			//Build a new TypedDefinedGalleryTreeNode for the current class
			if( classNode == null ) {

				NamedUserType aliceType = null;
				String aliceClassName = AliceResourceClassUtilities.getAliceClassName( currentClass );
				UserPackage packageName = getAlicePackage( currentClass, rootClass );

				UserMethod[] noMethods = {};
				UserField[] noFields = {};
				Field[] resourceConstants = AliceResourceClassUtilities.getFieldsOfType( currentClass, ModelResource.class );
				Class<? extends SModel> modelClass = AliceResourceClassUtilities.getModelClassForResourceClass( currentClass );
				AbstractType parentType = null;
				if( ( parentNode == null ) || ( parentNode.getUserType() == null ) ) {
					parentType = JavaType.getInstance( modelClass );
				} else {
					parentType = parentNode.getUserType();
				}
				NamedUserConstructor[] noConstructors = {};
				aliceType = new NamedUserType( aliceClassName, packageName, parentType, noConstructors, noMethods, noFields );

				classNode = new TypedDefinedGalleryTreeNode( aliceType, currentClass, modelClass );
				resourceClassToNodeMap.put( currentClass, classNode );
				if( root == null ) { //if the root node passed in is null, assign it to be the node from the first class we process
					root = classNode;
				}
				if( classNode.hasModelClass() ) {
					this.smodelBasedClasses.add( classNode );
				}
				if( resourceConstants.length != 0 ) {
					for( Field f : resourceConstants ) {
						String fieldClassName = AliceResourceClassUtilities.getClassNameFromName( f.getName() ) + aliceClassName;
						NamedUserType subParentType = classNode.getUserType();
						NamedUserType fieldType = new NamedUserType( fieldClassName, packageName, subParentType, noConstructors, noMethods, noFields );
						TypedDefinedGalleryTreeNode fieldNode = new TypedDefinedGalleryTreeNode( fieldType, currentClass, null );
						try {
							ModelResource resource = (ModelResource)f.get( null );
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

	private TypedDefinedGalleryTreeNode createClassTree(List<Class<? extends ModelResource>> classes ) {
		TypedDefinedGalleryTreeNode topNode = new TypedDefinedGalleryTreeNode( null, null, null );

		for( Class<? extends ModelResource> cls : classes ) {
			Class<? extends ModelResource> currentClass = cls;
			Stack<Class<? extends ModelResource>> classStack = new Stack<Class<? extends ModelResource>>();
			Class<?>[] interfaces = null;
			while( currentClass != null ) {
				classStack.push( currentClass );
				boolean isTopLevelResource = AliceResourceClassUtilities.isTopLevelResource( currentClass );
				if( isTopLevelResource ) {
					break;
				}

				interfaces = currentClass.getInterfaces();
				currentClass = null;
				if( ( interfaces != null ) && ( interfaces.length > 0 ) ) {
					for( Class<?> intrfc : interfaces ) {
						if( ModelResource.class.isAssignableFrom( intrfc ) ) {
							currentClass = (Class<? extends ModelResource>)intrfc;
							break;
						}
					}
				}
			}
			addNodes( topNode, classStack );
		}
		return topNode;
	}

	private final TypedDefinedGalleryTreeNode galleryTree;
	private final Map<Object, TypedDefinedGalleryTreeNode> resourceClassToNodeMap = Maps.newHashMap();
	private final List<TypedDefinedGalleryTreeNode> smodelBasedClasses = Lists.newArrayList();
}
