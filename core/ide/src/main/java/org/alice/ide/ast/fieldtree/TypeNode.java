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

package org.alice.ide.ast.fieldtree;

/**
 * @author Dennis Cosgrove
 */
public class TypeNode extends Node<org.lgna.project.ast.AbstractType<?, ?, ?>> {
	private final int collapseThreshold;
	private final int collapseThresholdForDescendants;
	private final java.util.List<TypeNode> typeNodes = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final java.util.List<FieldNode> fieldNodes = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	public static TypeNode createAndAddToParent( TypeNode parent, org.lgna.project.ast.AbstractType<?, ?, ?> type, int collapseThreshold, int collapseThresholdForDescendants ) {
		TypeNode rv = new TypeNode( parent, type, collapseThreshold, collapseThresholdForDescendants );
		if( parent != null ) {
			parent.getTypeNodes().add( rv );
		}
		return rv;
	}

	protected TypeNode( TypeNode parent, org.lgna.project.ast.AbstractType<?, ?, ?> type, int collapseThreshold, int collapseThresholdForDescendants ) {
		super( parent, type );
		this.collapseThreshold = collapseThreshold;
		this.collapseThresholdForDescendants = collapseThresholdForDescendants;
	}

	public java.util.List<TypeNode> getTypeNodes() {
		return this.typeNodes;
	}

	public java.util.List<FieldNode> getFieldNodes() {
		return this.fieldNodes;
	}

	public int getCollapseThreshold() {
		return this.collapseThreshold;
	}

	public int getCollapseThresholdForDescendants() {
		return this.collapseThresholdForDescendants;
	}

	public void collapseIfAppropriate() {
		for( TypeNode typeNode : this.typeNodes ) {
			typeNode.collapseIfAppropriate();
		}
		if( this.fieldNodes.size() < this.getCollapseThreshold() ) {
			TypeNode superTypeNode = this.getParent();
			if( superTypeNode != null ) {
				superTypeNode.fieldNodes.addAll( this.fieldNodes );
				this.fieldNodes.clear();
			}
		}
	}

	public void removeEmptyTypeNodes() {
		TypeNode[] copy = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.typeNodes, TypeNode.class );
		for( TypeNode typeNode : copy ) {
			typeNode.removeEmptyTypeNodes();
		}
		if( this.fieldNodes.size() == 0 ) {
			TypeNode superTypeNode = this.getParent();
			superTypeNode.typeNodes.remove( this );
			if( this.typeNodes.size() > 0 ) {
				superTypeNode.typeNodes.addAll( this.typeNodes );
			}
		}
	}

	public void sort() {
		java.util.Collections.sort( this.typeNodes );
		java.util.Collections.sort( this.fieldNodes );
		for( TypeNode typeNode : this.typeNodes ) {
			typeNode.sort();
		}
	}

	@Override
	protected void append( StringBuilder sb, int depth ) {
		super.append( sb, depth );
		for( TypeNode typeNode : this.typeNodes ) {
			typeNode.append( sb, depth + 1 );
		}
		for( FieldNode fieldNode : this.fieldNodes ) {
			fieldNode.append( sb, depth + 1 );
		}
	}
}
