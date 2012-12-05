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
public class TreeUtilities {
	private TreeUtilities() {
		throw new AssertionError();
	}

	private static ClassHierarchyBasedResourceNode treeBasedOnClassHierarchy;
	private static ThemeBasedResourceNode treeBasedOnTheme;

	private static ClassHierarchyBasedResourceNode createNode( org.lgna.story.resourceutilities.ModelResourceTreeNode source, ResourceKey key ) {
		java.util.List<ResourceNode> childNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		if( key instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)key;
			if( classResourceKey.getCls().equals( org.lgna.story.resources.BipedResource.class ) ) {
				java.util.List<ResourceNode> emptyList = java.util.Collections.emptyList();
				childNodes.add( new ClassHierarchyBasedResourceNode( PersonResourceKey.getInstance(), emptyList ) );
			}
		}
		for( org.lgna.story.resourceutilities.ModelResourceTreeNode childSource : source.childrenList() ) {
			org.lgna.project.ast.JavaType type = childSource.getResourceJavaType();
			org.lgna.project.ast.JavaField field = childSource.getJavaField();
			ResourceKey childKey;
			if( field != null ) {
				try {
					childKey = new EnumConstantResourceKey( (Enum<? extends org.lgna.story.resources.ModelResource>)field.getFieldReflectionProxy().getReification().get( null ) );
				} catch( IllegalAccessException iae ) {
					throw new RuntimeException( iae );
				}
			} else {
				childKey = new ClassResourceKey( (Class<? extends org.lgna.story.resources.ModelResource>)type.getClassReflectionProxy().getReification() );
			}
			childNodes.add( createNode( childSource, childKey ) );
		}
		return new ClassHierarchyBasedResourceNode( key, java.util.Collections.unmodifiableList( childNodes ) );
	}

	private static ClassHierarchyBasedResourceNode createTreeBasedOnClassHierarchy() {
		org.lgna.story.resourceutilities.StorytellingResources storytellingResources = org.lgna.story.resourceutilities.StorytellingResources.getInstance();
		org.lgna.story.resourceutilities.ModelResourceTreeNode root = storytellingResources.getGalleryTree();
		return createNode( root, new RootResourceKey() );
	}

	public static ClassHierarchyBasedResourceNode getTreeBasedOnClassHierarchy() {
		if( treeBasedOnClassHierarchy != null ) {
			//pass
		} else {
			treeBasedOnClassHierarchy = createTreeBasedOnClassHierarchy();
		}
		return treeBasedOnClassHierarchy;
	}

	private static void buildMap( edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> map, ResourceNode node ) {
		ResourceKey resourceKey = node.getResourceKey();
		if( resourceKey instanceof EnumConstantResourceKey ) {
			//pass
		} else {
			if( resourceKey instanceof RootResourceKey ) {
				//pass
			} else {
				String[] groupTags = node.getResourceKey().getGroupTags();
				String groupTag0;
				if( ( groupTags != null ) && ( groupTags.length > 0 ) ) {
					groupTag0 = groupTags[ 0 ];
				} else {
					groupTag0 = null;
				}
				if( groupTag0 != null ) {
					java.util.List<ResourceNode> list = map.getInitializingIfAbsentToLinkedList( groupTag0 );
					list.add( node );
				}
			}
			for( ResourceNode child : node.getNodeChildren() ) {
				buildMap( map, child );
			}
		}
	}

	private static java.util.List<ResourceNode> convert( java.util.List<ResourceNode> srcNodes ) {
		if( srcNodes != null ) {
			java.util.List<ResourceNode> dstNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( ResourceNode srcNode : srcNodes ) {
				java.util.List<ResourceNode> dstChildNodes = convert( srcNode.getNodeChildren() );
				dstNodes.add( new ThemeBasedResourceNode( srcNode.getResourceKey(), dstChildNodes ) );
			}
			java.util.Collections.sort( dstNodes );
			return java.util.Collections.unmodifiableList( dstNodes );
		} else {
			return java.util.Collections.emptyList();
		}
	}

	private static ThemeBasedResourceNode createTreeBasedOnTheme() {
		ResourceNode rootClassHierarchy = getTreeBasedOnClassHierarchy();
		edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> map = edu.cmu.cs.dennisc.java.util.Collections.newInitializingIfAbsentListHashMap();
		buildMap( map, rootClassHierarchy );

		java.util.List<ResourceNode> groupNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( String groupTag : map.keySet() ) {
			GroupTagKey groupTagKey = new GroupTagKey( groupTag );
			java.util.List<ResourceNode> srcChildNodes = map.get( groupTag );
			java.util.List<ResourceNode> dstChildNodes = convert( srcChildNodes );
			groupNodes.add( new ThemeBasedResourceNode( groupTagKey, dstChildNodes ) );
		}
		java.util.Collections.sort( groupNodes );
		return new ThemeBasedResourceNode( new RootResourceKey(), java.util.Collections.unmodifiableList( groupNodes ) );
	}

	public static ThemeBasedResourceNode getTreeBasedOnTheme() {
		if( treeBasedOnTheme != null ) {
			//pass
		} else {
			treeBasedOnTheme = createTreeBasedOnTheme();
		}
		return treeBasedOnTheme;
	}
}
