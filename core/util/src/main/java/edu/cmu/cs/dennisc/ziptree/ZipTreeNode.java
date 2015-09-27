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
public abstract class ZipTreeNode implements edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>, Comparable<ZipTreeNode> {
	private edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> parent;
	private String path;
	private String name;

	public ZipTreeNode( String path ) {
		this.path = path;
		if( this.path != null ) {
			String[] chunks = this.path.split( "/" );
			if( chunks.length > 0 ) {
				this.name = chunks[ chunks.length - 1 ];
			} else {
				this.name = null;
			}
		} else {
			this.name = null;
		}
	}

	@Override
	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> getParent() {
		return this.parent;
	}

	public void setParent( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> parent ) {
		if( this.parent instanceof DirectoryZipTreeNode ) {
			DirectoryZipTreeNode directoryZipTreeNode = (DirectoryZipTreeNode)this.parent;
			directoryZipTreeNode.removeChild( this );
		}
		this.parent = parent;
		if( this.parent instanceof DirectoryZipTreeNode ) {
			DirectoryZipTreeNode directoryZipTreeNode = (DirectoryZipTreeNode)this.parent;
			directoryZipTreeNode.addChild( this );
		}
	}

	@Override
	public String getValue() {
		return this.path;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int compareTo( edu.cmu.cs.dennisc.ziptree.ZipTreeNode other ) {
		if( this.getAllowsChildren() ) {
			if( other.getAllowsChildren() ) {
				return this.getName().compareToIgnoreCase( other.getName() );
			} else {
				return -1;
			}
		} else {
			if( other.getAllowsChildren() ) {
				return 1;
			} else {
				return this.getName().compareToIgnoreCase( other.getName() );
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + this.getValue() + "]";
	}
}
