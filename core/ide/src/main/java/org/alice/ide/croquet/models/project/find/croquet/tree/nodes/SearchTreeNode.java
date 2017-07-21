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
package org.alice.ide.croquet.models.project.find.croquet.tree.nodes;

import java.util.List;

import org.lgna.project.ast.MethodInvocation;

/**
 * @author Matt May
 */
public class SearchTreeNode {

	private final SearchTreeNode parent;
	private final List<SearchTreeNode> children = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();

	public SearchTreeNode( SearchTreeNode parent ) {
		this.parent = parent;
	}

	public SearchTreeNode getParent() {
		return parent;
	}

	public List<SearchTreeNode> getChildren() {
		return children;
	}

	public Object getValue() {
		return null;
	}

	public boolean getIsLeaf() {
		return this.children.size() == 0;
	}

	public boolean childrenContains( Object reference ) {
		return getChildForReference( reference ) != null;
	}

	public void addChild( SearchTreeNode newChildNode ) {
		this.children.add( newChildNode );
	}

	public void removeAllChildren() {
		this.children.clear();
	}

	public SearchTreeNode getChildForReference( Object reference ) {
		for( SearchTreeNode child : children ) {
			if( child.getValue().equals( reference ) ) {
				return child;
			}
		}
		return null;
	}

	public int getLocationAmongstSiblings() {
		return this.getParent().getChildren().indexOf( this );
	}

	public SearchTreeNode getYoungerSibling() {
		int location = this.getLocationAmongstSiblings();
		assert location < ( this.parent.children.size() - 1 );
		return this.getParent().children.get( location + 1 );
	}

	public SearchTreeNode getOlderSibling() {
		int location = this.getLocationAmongstSiblings();
		assert location > 0;
		return this.getParent().children.get( location - 1 );
	}

	@Override
	public String toString() {
		if( getValue() instanceof MethodInvocation ) {
			return ( (MethodInvocation)getValue() ).method.getValue().getName();
		}
		return getValue() != null ? getValue().toString() : "ROOT";
	}
}
