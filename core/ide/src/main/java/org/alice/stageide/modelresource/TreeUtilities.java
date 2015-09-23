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

import org.alice.nonfree.NebulousIde;

/**
 * @author Dennis Cosgrove
 */
public class TreeUtilities {
	private TreeUtilities() {
		throw new AssertionError();
	}

	private static ClassHierarchyBasedResourceNode treeBasedOnClassHierarchy;
	private static ThemeBasedResourceNode treeBasedOnTheme;
	private static GroupBasedResourceNode treeBasedOnGroup;

	private static ClassHierarchyBasedResourceNode createNode( org.lgna.story.resourceutilities.ModelResourceTreeNode source, ResourceKey key ) {
		java.util.List<ResourceNode> childNodes = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		if( key instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)key;
			if( classResourceKey.getModelResourceCls().equals( org.lgna.story.resources.BipedResource.class ) ) {
				java.util.List<ResourceNode> emptyList = java.util.Collections.emptyList();
				NebulousIde.nonfree.addBipedResourceResourceNodes( childNodes, emptyList );
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
		org.lgna.story.resourceutilities.ModelResourceTreeNode root = org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils.INSTANCE.getGalleryTree();
		return createNode( root, new RootResourceKey( "all classes" ) );
	}

	public static ClassHierarchyBasedResourceNode getTreeBasedOnClassHierarchy() {
		if( treeBasedOnClassHierarchy != null ) {
			//pass
		} else {
			treeBasedOnClassHierarchy = createTreeBasedOnClassHierarchy();
		}
		return treeBasedOnClassHierarchy;
	}

	private static void addTags( edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> map, String[] tags, ResourceNode node ) {
		if( tags != null ) {
			for( String tag : tags ) {
				if( tag.startsWith( "*" ) ) {
					tag = tag.substring( 1 );
				}
				java.util.List<ResourceNode> list = map.getInitializingIfAbsentToLinkedList( tag );
				list.add( node );
			}
		}
	}

	private static void buildMap( edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> mapGroup, edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> mapTheme, ResourceNode node ) {
		ResourceKey resourceKey = node.getResourceKey();
		if( resourceKey instanceof EnumConstantResourceKey ) {
			//pass
		} else {
			if( resourceKey instanceof RootResourceKey ) {
				//pass
			} else {
				addTags( mapGroup, node.getResourceKey().getGroupTags(), node );
				addTags( mapTheme, node.getResourceKey().getThemeTags(), node );
			}
			for( ResourceNode child : node.getNodeChildren() ) {
				buildMap( mapGroup, mapTheme, child );
			}
		}
	}

	private static java.util.List<ResourceNode> convert( java.util.List<ResourceNode> srcNodes, boolean isTheme ) {
		if( srcNodes != null ) {
			java.util.List<ResourceNode> dstNodes = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( ResourceNode srcNode : srcNodes ) {
				java.util.List<ResourceNode> dstChildNodes = convert( srcNode.getNodeChildren(), isTheme );
				ResourceNode node;
				if( isTheme ) {
					node = new ThemeBasedResourceNode( srcNode.getResourceKey(), dstChildNodes );
				} else {
					node = new GroupBasedResourceNode( srcNode.getResourceKey(), dstChildNodes );
				}
				dstNodes.add( node );
			}
			java.util.Collections.sort( dstNodes );
			return dstNodes;
		} else {
			return java.util.Collections.emptyList();
		}
	}

	private static java.util.List<org.lgna.croquet.icon.AbstractSingleSourceImageIconFactory> createIconFactories( java.util.List<ResourceNode> dstChildNodes ) {
		java.util.List<org.lgna.croquet.icon.AbstractSingleSourceImageIconFactory> iconFactories = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( ResourceNode resourceNode : dstChildNodes ) {
			ResourceKey resourceKey = resourceNode.getResourceKey();
			//todo
			org.lgna.croquet.icon.IconFactory iconFactory = resourceKey.getIconFactory();
			if( iconFactory instanceof org.lgna.croquet.icon.AbstractSingleSourceImageIconFactory ) {
				org.lgna.croquet.icon.AbstractSingleSourceImageIconFactory imageIconFactory = (org.lgna.croquet.icon.AbstractSingleSourceImageIconFactory)iconFactory;
				iconFactories.add( imageIconFactory );
			}
			if( iconFactories.size() == 5 ) {
				break;
			}
		}
		return iconFactories;
	}

	private static void addTagNode( String tag, java.util.List<ResourceNode> dstChildNodes, java.util.List<ResourceNode> tagNodes, java.util.Map<String, ResourceNode> mapTagToNode, boolean isTheme ) {
		ResourceNode dstNode;
		if( isTheme ) {
			TagKey tagKey;
			if( tag.indexOf( TagKey.SEPARATOR ) == -1 ) {
				tagKey = new ThemeTagKey( tag );
			} else {
				tagKey = new GroupTagKey( tag, createIconFactories( dstChildNodes ) );
			}
			dstNode = new ThemeBasedResourceNode( tagKey, dstChildNodes );
		} else {
			GroupTagKey groupTagKey = new GroupTagKey( tag, createIconFactories( dstChildNodes ) );
			dstNode = new GroupBasedResourceNode( groupTagKey, dstChildNodes );
		}
		tagNodes.add( dstNode );
		mapTagToNode.put( tag, dstNode );
	}

	private static java.util.List<ResourceNode> createTagNodeList( edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> map, boolean isTheme, String... emptyTagNames ) {
		java.util.Map<String, ResourceNode> mapInternal = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

		java.util.List<ResourceNode> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		java.util.List<String> emptyTags = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( emptyTagNames );
		for( String emptyGroupTag : emptyTags ) {
			java.util.List<ResourceNode> childNodes = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			addTagNode( emptyGroupTag, childNodes, rv, mapInternal, isTheme );
		}

		for( String tag : map.keySet() ) {
			if( emptyTags.contains( tag ) ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( tag );
			} else {
				java.util.List<ResourceNode> srcChildNodes = map.get( tag );
				java.util.List<ResourceNode> dstChildNodes = convert( srcChildNodes, isTheme );

				addTagNode( tag, dstChildNodes, rv, mapInternal, isTheme );
			}
		}
		java.util.ListIterator<ResourceNode> listIterator = rv.listIterator();
		while( listIterator.hasNext() ) {
			ResourceNode resourceNode = listIterator.next();
			TagKey tagKey = (TagKey)resourceNode.getResourceKey();
			String tag = tagKey.getTag();
			assert tag.startsWith( "*" ) == false : tag;
			int lastIndex = tag.lastIndexOf( TagKey.SEPARATOR );
			if( lastIndex != -1 ) {
				String parentTag = tag.substring( 0, lastIndex );
				ResourceNode parentToBeNode = mapInternal.get( parentTag );
				if( parentToBeNode != null ) {
					listIterator.remove();
					parentToBeNode.addNodeChild( 0, resourceNode );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( tagKey );
				}
			}
		}
		java.util.Collections.sort( rv );

		return rv;
	}

	private static java.util.List<ResourceNode> createGroupTagNodeList( edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> mapGroup ) {
		return createTagNodeList( mapGroup, false, "household" );
	}

	private static java.util.List<ResourceNode> createThemeTagNodeList( edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> mapTheme ) {
		return createTagNodeList( mapTheme, true, "household" );
	}

	private static void createTreesBasedOnThemeAndGroup() {
		ResourceNode rootClassHierarchy = getTreeBasedOnClassHierarchy();
		edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> mapGroup = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentListHashMap();
		edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<String, ResourceNode> mapTheme = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentListHashMap();
		buildMap( mapGroup, mapTheme, rootClassHierarchy );

		java.util.List<ResourceNode> themeNodes = createThemeTagNodeList( mapTheme );
		java.util.List<ResourceNode> groupNodes = createGroupTagNodeList( mapGroup );

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( themeNodes );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( groupNodes );

		treeBasedOnGroup = new GroupBasedResourceNode( new RootResourceKey( "all groups" ), groupNodes );
		treeBasedOnTheme = new ThemeBasedResourceNode( new RootResourceKey( "all themes" ), themeNodes );
	}

	public static ThemeBasedResourceNode getTreeBasedOnTheme() {
		if( treeBasedOnTheme != null ) {
			//pass
		} else {
			createTreesBasedOnThemeAndGroup();
		}
		return treeBasedOnTheme;
	}

	public static GroupBasedResourceNode getTreeBasedOnGroup() {
		if( treeBasedOnGroup != null ) {
			//pass
		} else {
			createTreesBasedOnThemeAndGroup();
		}
		return treeBasedOnGroup;
	}
}
