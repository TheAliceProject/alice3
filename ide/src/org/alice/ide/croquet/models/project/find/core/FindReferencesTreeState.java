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
package org.alice.ide.croquet.models.project.find.core;

import java.util.List;

import javax.swing.Icon;

import org.alice.ide.croquet.models.project.find.croquet.FindComposite;
import org.lgna.croquet.CustomTreeSelectionState;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.codecs.DefaultItemCodec;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.UserMethod;

import com.sun.tools.javac.util.Pair;

/**
 * @author Matt May
 */
public class FindReferencesTreeState extends CustomTreeSelectionState<SearchObjectNode> {

	private final static SearchObjectNode root = new SearchObjectNode( null, null );
	private boolean showGenerated = true;
	private final static ItemCodec<SearchObjectNode> codec = new DefaultItemCodec<SearchObjectNode>( SearchObjectNode.class );

	public FindReferencesTreeState() {
		super( FindComposite.FIND_COMPOSITE_GROUP, java.util.UUID.fromString( "88fc8668-1de6-4976-9f3b-5c9688675e2b" ), root, codec );
	}

	@Override
	protected String getTextForNode( SearchObjectNode node ) {
		return "TODO: get text";
	}

	@Override
	protected Icon getIconForNode( SearchObjectNode node ) {
		return null;
	}

	@Override
	public SearchObjectNode getParent( SearchObjectNode node ) {
		return node.getParent();
	}

	@Override
	protected int getChildCount( SearchObjectNode parent ) {
		return parent.getChildren().size();
	}

	@Override
	protected SearchObjectNode getChild( SearchObjectNode parent, int index ) {
		return parent.getChildren().get( index );
	}

	@Override
	protected int getIndexOfChild( SearchObjectNode parent, SearchObjectNode child ) {
		return parent.getChildren().indexOf( child );
	}

	@Override
	protected SearchObjectNode getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf( SearchObjectNode node ) {
		return node.getIsLeaf();
	}

	public void refreshWith( SearchObject<?> searchObject ) {
		this.setValueTransactionlessly( null );
		root.removeAllChildren();
		if( searchObject != null ) {
			List<Expression> references = searchObject.getReferences();
			for( Expression reference : references ) {
				UserMethod userMethod = reference.getFirstAncestorAssignableTo( UserMethod.class );
				if( showGenerated || !userMethod.getManagementLevel().isGenerated() ) {
					SearchObjectNode userMethodNode = root.getChildForReference( userMethod );
					if( userMethodNode == null ) {
						SearchObjectNode newChildNode = new SearchObjectNode( userMethod, root );
						root.addChild( newChildNode );
						newChildNode.addChild( new SearchObjectNode( reference, newChildNode ) );
					} else {
						userMethodNode.addChild( new SearchObjectNode( reference, userMethodNode ) );
					}
				}
			}
		}
		refreshAll();
	}

	public void moveSelectedUpOne() {
		SearchObjectNode selected = this.getValue();
		if( selected.getParent() == root ) {
			if( selected.getLocationAmongstSiblings() > 0 ) {
				SearchObjectNode olderSibling = selected.getOlderSibling();
				setValueTransactionlessly( olderSibling.getChildren().get( olderSibling.getChildren().size() - 1 ) );
			}
		} else {
			if( selected.getLocationAmongstSiblings() > 0 ) {
				this.setValueTransactionlessly( selected.getOlderSibling() );
			} else {
				this.setValueTransactionlessly( selected.getParent() );
			}
		}
	}

	public void moveSelectedDownOne() {
		SearchObjectNode selected = this.getValue();
		if( selected.getParent() == root ) {
			this.setValueTransactionlessly( selected.getChildren().get( 0 ) );
		} else {
			if( selected.getLocationAmongstSiblings() < ( selected.getParent().getChildren().size() - 1 ) ) {
				this.setValueTransactionlessly( selected.getYoungerSibling() );
			} else if( selected.getParent().getLocationAmongstSiblings() < ( selected.getParent().getParent().getChildren().size() - 1 ) ) {
				this.setValueTransactionlessly( selected.getParent().getYoungerSibling() );
			}
		}
	}

	public SearchObjectNode selectAtCoordinates( int a, int b ) {
		if( b == -1 ) {
			return root.getChildren().get( a );
		} else {
			return root.getChildren().get( a ).getChildren().get( b );
		}
	}

	public boolean isEmpty() {
		return root.getChildren().size() > 0;
	}

	public SearchObjectNode getTopValue() {
		return root.getChildren().get( 0 );
	}

	public Pair<Integer, Integer> getSelectedCoordinates() {
		int a;
		int b;
		SearchObjectNode value = this.getValue();
		if( value.getParent() == root ) {
			b = -1;
			a = value.getLocationAmongstSiblings();
		} else {
			b = value.getLocationAmongstSiblings();
			a = value.getParent().getLocationAmongstSiblings();
		}
		return new Pair<Integer, Integer>( a, b );
	}

	public void setShowGenerated( boolean showGenerated ) {
		this.showGenerated = showGenerated;
	}
}
