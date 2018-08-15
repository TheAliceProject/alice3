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
package org.alice.stageide.modelresource;

import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.nonfree.NebulousIde;
import org.lgna.croquet.icon.AbstractSingleSourceImageIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resourceutilities.GalleryResourceTreeNode;
import org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils;

import java.util.*;

/**
 * @author Dennis Cosgrove
 */
public class TreeUtilities {
	private TreeUtilities() {
		throw new AssertionError();
	}

	private static ClassHierarchyBasedResourceNode treeBasedOnClassHierarchy;
	private static UserDefinedResourceNode treeBasedOnUserResources;
	private static ThemeBasedResourceNode treeBasedOnTheme;
	private static GroupBasedResourceNode treeBasedOnGroup;

	private static ClassHierarchyBasedResourceNode createNode( GalleryResourceTreeNode source, ResourceKey key ) {
		List<ResourceNode> childNodes = Lists.newLinkedList();
		if( key instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)key;
			if( classResourceKey.getModelResourceCls().equals( BipedResource.class ) ) {
				List<ResourceNode> emptyList = Collections.emptyList();
				NebulousIde.nonfree.addBipedResourceResourceNodes( childNodes, emptyList );
			}
		}
		for( GalleryResourceTreeNode childSource : source.childrenList() ) {
			ResourceKey childKey = childSource.createResourceKey();
			childNodes.add( createNode( childSource, childKey ) );
		}
		return new ClassHierarchyBasedResourceNode( key, Collections.unmodifiableList( childNodes ) );
	}

	private static ClassHierarchyBasedResourceNode createTreeBasedOnClassHierarchy() {
		GalleryResourceTreeNode root = StorytellingResourcesTreeUtils.INSTANCE.getGalleryTree();
		return createNode( root, new RootResourceKey( "AllClasses", "all classes" ) );
	}

	public static ClassHierarchyBasedResourceNode getTreeBasedOnClassHierarchy() {
		if( treeBasedOnClassHierarchy != null ) {
			//pass
		} else {
			treeBasedOnClassHierarchy = createTreeBasedOnClassHierarchy();
		}
		return treeBasedOnClassHierarchy;
	}

	public static UserDefinedResourceNode getTreeBasedOnUserResources() {
		if ( treeBasedOnUserResources == null ) {
			List<ResourceNode> userResourceNodes = new ArrayList<>();
			getUserResourceNodes(getTreeBasedOnClassHierarchy(), userResourceNodes);

			List<ResourceNode> convertedUserResourceNodes = convert( userResourceNodes, UserDefinedResourceNode.class.getSimpleName() );

			treeBasedOnUserResources = new UserDefinedResourceNode( new RootResourceKey( "MyGallery", "My Gallery" ), convertedUserResourceNodes );
		}
		return treeBasedOnUserResources;
	}

	private static void addTags( InitializingIfAbsentListHashMap<String, ResourceNode> map, String[] tags, ResourceNode node ) {
		if( tags != null ) {
			for( String tag : tags ) {
				if( tag.startsWith( "*" ) ) {
					tag = tag.substring( 1 );
				}
				List<ResourceNode> list = map.getInitializingIfAbsentToLinkedList( tag );
				list.add( node );
			}
		}
	}

	private static void getUserResourceNodes( ResourceNode node, List<ResourceNode> userResourceNodes ) {
		if( node.isUserDefinedModel() ) {
			userResourceNodes.add(node);
		}
		for( ResourceNode child : node.getNodeChildren() ) {
			getUserResourceNodes( child, userResourceNodes );
		}
	}

	private static void buildMap( InitializingIfAbsentListHashMap<String, ResourceNode> mapGroup, InitializingIfAbsentListHashMap<String, ResourceNode> mapTheme, ResourceNode node ) {
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

	private static List<ResourceNode> convert( List<ResourceNode> srcNodes, String nodeClassName ) {
		if( srcNodes != null ) {
			List<ResourceNode> dstNodes = Lists.newLinkedList();
			for( ResourceNode srcNode : srcNodes ) {
				List<ResourceNode> dstChildNodes = convert( srcNode.getNodeChildren(), nodeClassName );
				ResourceNode node = null;
				switch (nodeClassName) {
					case "ThemeBasedResourceNode" : node = new ThemeBasedResourceNode( srcNode.getResourceKey(), dstChildNodes ); break;
					case "GroupBasedResourceNode" : node = new GroupBasedResourceNode( srcNode.getResourceKey(), dstChildNodes ); break;
					case "UserDefinedResourceNode" : node = new UserDefinedResourceNode( srcNode.getResourceKey(), dstChildNodes ); break;
				}
				dstNodes.add( node );
			}
			Collections.sort( dstNodes );
			return dstNodes;
		} else {
			return Collections.emptyList();
		}
	}

	private static List<AbstractSingleSourceImageIconFactory> createIconFactories( List<ResourceNode> dstChildNodes ) {
		List<AbstractSingleSourceImageIconFactory> iconFactories = Lists.newLinkedList();
		for( ResourceNode resourceNode : dstChildNodes ) {
			ResourceKey resourceKey = resourceNode.getResourceKey();
			//todo
			IconFactory iconFactory = resourceKey.getIconFactory();
			if( iconFactory instanceof AbstractSingleSourceImageIconFactory ) {
				AbstractSingleSourceImageIconFactory imageIconFactory = (AbstractSingleSourceImageIconFactory)iconFactory;
				iconFactories.add( imageIconFactory );
			}
			if( iconFactories.size() == 5 ) {
				break;
			}
		}
		return iconFactories;
	}

	private static void addTagNode( String tag, List<ResourceNode> dstChildNodes, List<ResourceNode> tagNodes, Map<String, ResourceNode> mapTagToNode, boolean isTheme ) {
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

	private static List<ResourceNode> createTagNodeList( InitializingIfAbsentListHashMap<String, ResourceNode> map, boolean isTheme, String... emptyTagNames ) {
		Map<String, ResourceNode> mapInternal = Maps.newHashMap();

		List<ResourceNode> rv = Lists.newLinkedList();

		List<String> emptyTags = Lists.newArrayList( emptyTagNames );
		for( String emptyGroupTag : emptyTags ) {
			List<ResourceNode> childNodes = Lists.newLinkedList();
			addTagNode( emptyGroupTag, childNodes, rv, mapInternal, isTheme );
		}

		for( String tag : map.keySet() ) {
			if( emptyTags.contains( tag ) ) {
				Logger.severe( tag );
			} else {
				List<ResourceNode> srcChildNodes = map.get( tag );
				String nodeClassName = isTheme ? ThemeBasedResourceNode.class.getSimpleName() : GroupBasedResourceNode.class.getSimpleName();
				List<ResourceNode> dstChildNodes = convert( srcChildNodes, nodeClassName );

				addTagNode( tag, dstChildNodes, rv, mapInternal, isTheme );
			}
		}
		ListIterator<ResourceNode> listIterator = rv.listIterator();
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
					Logger.severe( tagKey );
				}
			}
		}
		Collections.sort( rv );

		return rv;
	}

	private static List<ResourceNode> createGroupTagNodeList( InitializingIfAbsentListHashMap<String, ResourceNode> mapGroup ) {
		return createTagNodeList( mapGroup, false, "household" );
	}

	private static List<ResourceNode> createThemeTagNodeList( InitializingIfAbsentListHashMap<String, ResourceNode> mapTheme ) {
		return createTagNodeList( mapTheme, true, "household" );
	}

	private static void createTreesBasedOnThemeAndGroup() {
		ResourceNode rootClassHierarchy = getTreeBasedOnClassHierarchy();
		InitializingIfAbsentListHashMap<String, ResourceNode> mapGroup = Maps.newInitializingIfAbsentListHashMap();
		InitializingIfAbsentListHashMap<String, ResourceNode> mapTheme = Maps.newInitializingIfAbsentListHashMap();
		buildMap( mapGroup, mapTheme, rootClassHierarchy );

		List<ResourceNode> themeNodes = createThemeTagNodeList( mapTheme );
		List<ResourceNode> groupNodes = createGroupTagNodeList( mapGroup );

		Logger.outln( themeNodes );
		Logger.outln( groupNodes );

		treeBasedOnGroup = new GroupBasedResourceNode( new RootResourceKey( "AllGroups", "all groups" ), groupNodes );
		treeBasedOnTheme = new ThemeBasedResourceNode( new RootResourceKey( "AllThemes", "all themes" ), themeNodes );
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
