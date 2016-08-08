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
package edu.cmu.cs.dennisc.ziptree;

/**
 * @author Dennis Cosgrove
 */
public class DirectoryZipTreeNode extends ZipTreeNode {
	private java.util.List<ZipTreeNode> children = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private boolean isSorted = false;

	public DirectoryZipTreeNode( String path ) {
		super( path );
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	private java.util.List<? extends edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> getSortedChildren() {
		if( this.isSorted ) {
			//pass
		} else {
			java.util.Collections.sort( this.children );
			this.isSorted = true;
		}
		return this.children;
	}

	@Override
	public java.util.Enumeration<? extends edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> children() {
		return java.util.Collections.enumeration( this.getSortedChildren() );
	}

	@Override
	public java.util.Iterator iterator() {
		return this.children.iterator();
	}

	//	public java.util.Iterator< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> > iterator() {
	//		return this.children.iterator();
	//	}
	@Override
	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> getChildAt( int childIndex ) {
		return this.getSortedChildren().get( childIndex );
	}

	@Override
	public int getChildCount() {
		return this.children.size();
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public int getIndex( javax.swing.tree.TreeNode node ) {
		return this.getSortedChildren().indexOf( node );
	}

	/* package-private */void addChild( ZipTreeNode zipTreeNode ) {
		this.children.add( zipTreeNode );
		this.isSorted = false;
	}

	/* package-private */void removeChild( ZipTreeNode zipTreeNode ) {
		this.children.remove( zipTreeNode );
	}

	public ZipTreeNode getChildNamed( String name ) {
		for( ZipTreeNode zipTreeNode : this.children ) {
			if( name.equals( zipTreeNode.getName() ) ) {
				return zipTreeNode;
			}
		}
		return null;
	}

	public ZipTreeNode getDescendant( String path ) {
		ZipTreeNode rv = this;
		String[] names = path.split( "/" );
		for( String name : names ) {
			if( rv instanceof DirectoryZipTreeNode ) {
				DirectoryZipTreeNode directoryZipTreeNode = (DirectoryZipTreeNode)rv;
				rv = directoryZipTreeNode.getChildNamed( name );
			} else {
				rv = null;
				break;
			}
		}
		return rv;
	}
}
