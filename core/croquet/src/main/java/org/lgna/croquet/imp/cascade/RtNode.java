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

package org.lgna.croquet.imp.cascade;

import org.lgna.croquet.Element;

/**
 * @author Dennis Cosgrove
 */
abstract class RtNode<E extends Element, N extends CascadeNode<?, E>> {
	private final E element;
	private final N node;
	private RtNode<?, ?> parent;
	private RtNode<?, ?> nextSibling;

	public RtNode( E element, N node ) {
		assert element != null;
		this.element = element;
		assert node != null : element;
		this.node = node;
	}

	public E getElement() {
		return this.element;
	}

	public N getNode() {
		return this.node;
	}

	public RtRoot<?, ?> getRtRoot() {
		return this.parent.getRtRoot();
	}

	protected RtNode<?, ?> getParent() {
		return this.parent;
	}

	protected RtNode<?, ?> getNextSibling() {
		return this.nextSibling;
	}

	public void setParent( RtNode<?, ?> parent ) {
		this.parent = parent;
	}

	public void setNextSibling( RtNode<?, ?> nextSibling ) {
		this.nextSibling = nextSibling;
	}

	protected void updateParentsAndNextSiblings( RtNode<?, ?>[] rtNodes ) {
		for( RtNode<?, ?> rtNode : rtNodes ) {
			rtNode.setParent( this );
		}
		if( rtNodes.length > 0 ) {
			RtNode<?, ?> rtNodeA = rtNodes[ 0 ];
			for( int i = 1; i < rtNodes.length; i++ ) {
				RtNode<?, ?> rtNodeB = rtNodes[ i ];
				rtNodeA.setNextSibling( rtNodeB );
				rtNodeA = rtNodeB;
			}
			rtNodeA.setNextSibling( null );
		}
	}

	public abstract RtBlank<?> getNearestBlank();

	public RtBlank<?> getNextBlank() {
		RtBlank<?> blank = this.getNearestBlank();
		if( blank != null ) {
			RtBlank<?> nextSibling = (RtBlank<?>)blank.getNextSibling();
			if( nextSibling != null ) {
				return nextSibling;
			}
		}
		if( this.parent != null ) {
			return this.parent.getNextBlank();
		} else {
			return null;
		}
		//		RtBlank< ? > rv = null;
		//		RtBlank< ? > blank = this.getNearestBlank();
		//		if( blank != null ) {
		//			RtBlank<?> nextSibling = (RtBlank<?>)blank.getNextSibling();
		//			if( nextSibling != null ) {
		//				rv = nextSibling;
		//			}
		//		}
		//		if( rv != null ) {
		//			if( this.parent != null ) {
		//				rv = this.parent.getNextBlank();
		//			}
		//		}
		//		if( rv != null ) {
		//			if( rv.isAutomaticallyDetermined() ) {
		//				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( rv );
		//			}
		//		}
		//		return rv;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		edu.cmu.cs.dennisc.java.lang.ClassUtilities.getTrimmedClassName( this.getClass() );
		sb.append( "[" );
		sb.append( this.element );
		sb.append( "]" );
		return sb.toString();
	}
}
