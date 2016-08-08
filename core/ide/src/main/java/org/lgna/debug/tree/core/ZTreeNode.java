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
package org.lgna.debug.tree.core;

/**
 * @author Dennis Cosgrove
 */
public final class ZTreeNode<T> implements javax.swing.tree.TreeNode {
	private static enum IsLeaf {
		TRUE,
		FALSE
	};

	public static class Builder<T> {
		public Builder( T value, boolean isLeaf ) {
			this.value = value;
			if( isLeaf ) {
				this.childBuilders = null;
			} else {
				this.childBuilders = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			}
		}

		public T getValue() {
			return this.value;
		}

		public void addChildBuilder( Builder<T> childBuilder ) {
			assert this.childBuilders != null : this;
			this.childBuilders.add( childBuilder );
		}

		//for pruning
		public java.util.Iterator<Builder<T>> getChildBuildersIterator() {
			if( this.childBuilders != null ) {
				return this.childBuilders.iterator();
			} else {
				return edu.cmu.cs.dennisc.java.util.Iterators.emptyIterator();
			}
		}

		//for pruning
		public boolean isEmpty() {
			return this.childBuilders != null ? this.childBuilders.size() == 0 : true;
		}

		public ZTreeNode<T> build() {
			java.util.List<ZTreeNode<T>> children;
			IsLeaf isLeaf;
			if( this.childBuilders != null ) {
				java.util.List<ZTreeNode<T>> list = edu.cmu.cs.dennisc.java.util.Lists.newArrayListWithInitialCapacity( this.childBuilders.size() );
				for( Builder<T> childBuilder : this.childBuilders ) {
					list.add( childBuilder.build() );
				}
				children = java.util.Collections.unmodifiableList( list );
				isLeaf = IsLeaf.FALSE;
			} else {
				children = java.util.Collections.emptyList();
				isLeaf = IsLeaf.TRUE;
			}
			ZTreeNode<T> rv = new ZTreeNode<T>( value, children, isLeaf );
			for( ZTreeNode<T> child : children ) {
				child.parent = rv;
			}
			return rv;
		}

		private final T value;
		private final java.util.List<Builder<T>> childBuilders;
	}

	private ZTreeNode( T value, java.util.List<ZTreeNode<T>> children, IsLeaf isLeaf ) {
		this.value = value;
		this.children = children;
		this.isLeaf = isLeaf;
	}

	public T getValue() {
		return this.value;
	}

	@Override
	public javax.swing.tree.TreeNode getChildAt( int childIndex ) {
		return this.children.get( childIndex );
	}

	@Override
	public int getChildCount() {
		return this.children.size();
	}

	@Override
	public javax.swing.tree.TreeNode getParent() {
		return this.parent;
	}

	@Override
	public int getIndex( javax.swing.tree.TreeNode node ) {
		return this.children.indexOf( node );
	}

	@Override
	public boolean getAllowsChildren() {
		return this.isLeaf() == false;
	}

	@Override
	public boolean isLeaf() {
		return this.isLeaf == IsLeaf.TRUE;
	}

	@Override
	public java.util.Enumeration<ZTreeNode<T>> children() {
		return java.util.Collections.enumeration( this.children );
	}

	private final T value;
	private final java.util.List<ZTreeNode<T>> children;
	private final IsLeaf isLeaf;
	private/*pseudo-final*/ZTreeNode<T> parent;
}
