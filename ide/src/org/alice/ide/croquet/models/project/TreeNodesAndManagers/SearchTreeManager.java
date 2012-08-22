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
package org.alice.ide.croquet.models.project.TreeNodesAndManagers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.Icon;

import org.lgna.croquet.Application;
import org.lgna.croquet.CustomTreeSelectionState;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.components.Tree;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class SearchTreeManager extends CustomTreeSelectionState<SearchTreeNode> {

	protected int SHOULD_BE_EXPANDED = 2;
	protected SearchTreeNode root;
	protected LinkedList<SearchTreeNode> hiddenList = Collections.newLinkedList();
	protected Map<UserMethod, LinkedList<MethodInvocation>> methodParentMap = Collections.newHashMap();
	protected Tree<SearchTreeNode> owner;

	public SearchTreeManager( UUID id ) {
		super( Application.INFORMATION_GROUP, id, SearchCodec.getSingleton(), null );
		UserMethod rootMethod = new UserMethod();
		rootMethod.setName( "Project" );
		root = new SearchTreeNode( null, rootMethod );
		refresh();
	}

	public void refresh() {
		methodParentMap = Collections.newHashMap();
		root.removeChildren();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.NamedUserType programType = ide.getStrippedProgramType();
		if( programType != null ) {
			class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {

				public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
					if( crawlable instanceof MethodInvocation ) {
						MethodInvocation methodInvocation = (MethodInvocation)crawlable;
						UserMethod method = methodInvocation.getFirstAncestorAssignableTo( UserMethod.class );
						if( methodParentMap.get( method ) == null ) {
							methodParentMap.put( method, new LinkedList<MethodInvocation>() );
						}
						methodParentMap.get( method ).add( methodInvocation );
					}
				}
			}
			StatementCountCrawler crawler = new StatementCountCrawler();
			programType.crawl( crawler, true );

			for( UserMethod method : methodParentMap.keySet() ) {
				SearchTreeNode parent = addParentNode( root, method );
				addTunnelling( parent );
				List<SearchTreeNode> children = Collections.newLinkedList();
				for( MethodInvocation methodInvocation : methodParentMap.get( method ) ) {
					AbstractMethod abstractMethod = methodInvocation.method.getValue();
					SearchTreeNode child = new SearchTreeNode( parent, methodInvocation );
					children.add( child );
				}
				java.util.Collections.sort( children );
			}
		}
		refreshAll();
	}

	@Override
	final protected int getChildCount( SearchTreeNode parent ) {
		return parent.getChildren().size();
	}

	@Override
	final protected SearchTreeNode getChild( SearchTreeNode parent, int index ) {
		return parent.getChild( index );
	}

	@Override
	final protected int getIndexOfChild( SearchTreeNode parent, SearchTreeNode child ) {
		return parent.getChildIndex( child );
	}

	@Override
	final public SearchTreeNode getRoot() {
		return root;
	}

	@Override
	final protected SearchTreeNode getParent( SearchTreeNode node ) {
		return node.getParent();
	}

	@Override
	final public boolean isLeaf( SearchTreeNode node ) {
		return node.getNumChildren() == 0;
	}

	@Override
	final protected String getTextForNode( SearchTreeNode node ) {
		return node.getText();
	}

	@Override
	final protected Icon getIconForNode( SearchTreeNode node ) {
		return node.getIcon();
	}

	public SearchTreeNode addNode( SearchTreeNode parent, MethodInvocation invocation ) {
		SearchTreeNode rv;
		if( parent != null ) {
			parent.addChild( rv = new SearchTreeNode( parent, invocation ) );
		} else {
			getRoot().addChild( rv = new SearchTreeNode( getRoot(), invocation ) );
		}
		return rv;
	}

	private SearchTreeNode addParentNode( SearchTreeNode parent, UserMethod method ) {
		SearchTreeNode rv;
		if( parent != null ) {
			parent.addChild( rv = new SearchTreeNode( parent, method ) );
		} else {
			getRoot().addChild( rv = new SearchTreeNode( getRoot(), method ) );
		}
		return rv;
	}

	protected void removeNode( SearchTreeNode node ) {
		node.removeSelf();
	}

	private void addTunnelling( SearchTreeNode parent ) {
		AbstractMethod parentMethod = parent.getContent();
		if( methodParentMap.get( parentMethod ) != null ) {
			for( MethodInvocation methodInvocation : methodParentMap.get( parentMethod ) ) {
				//				AbstractMethod childMethod = (AbstractMethod)methodInvocation.method.getValue();
				SearchTreeNode node = addNode( parent, methodInvocation );
				addTunnelling( node );
			}
		}
	}

	public void setOwner( Tree<SearchTreeNode> tree ) {
		this.owner = tree;
	}

	protected void setProperExpandedLevels( SearchTreeNode node ) {
		if( node.getDepth() > SHOULD_BE_EXPANDED ) {
			owner.expandNode( node );
		} else {
			owner.collapseNode( node );
		}
		for( SearchTreeNode child : node.getChildren() ) {
			setProperExpandedLevels( child );
		}
	}

	protected void show( SearchTreeNode node ) {
		if( hiddenList.contains( node ) ) {
			show( node.getParent() );
			node.getParent().addChild( node );
			hiddenList.remove( node );
		}
	}

	protected boolean shouldShow( SearchTreeNode node ) {
		return true;
	}

	protected void hide( SearchTreeNode node ) {
		if( !hiddenList.contains( node ) ) {
			hiddenList.add( node );
			node.removeSelf();
			List<SearchTreeNode> children = Collections.newArrayList( node.getChildren() );
			for( SearchTreeNode child : children ) {
				hide( child );
			}
			node.removeSelf();
		}
	}

	protected void hideAll() {
		List<SearchTreeNode> children = Collections.newArrayList( root.getChildren() );
		for( SearchTreeNode node : children ) {
			hide( node );
		}
		java.util.Collections.sort( hiddenList );
	}

	private static class SearchCodec implements ItemCodec<SearchTreeNode> {

		private final static SearchCodec instance = new SearchCodec();

		public static SearchCodec getSingleton() {
			return instance;
		}

		public Class<SearchTreeNode> getValueClass() {
			return null;
		}

		public SearchTreeNode decodeValue( BinaryDecoder binaryDecoder ) {
			return null;
		}

		public void encodeValue( BinaryEncoder binaryEncoder, SearchTreeNode value ) {
		}

		public StringBuilder appendRepresentation( StringBuilder rv, SearchTreeNode value ) {
			return null;
		}

	}
}
