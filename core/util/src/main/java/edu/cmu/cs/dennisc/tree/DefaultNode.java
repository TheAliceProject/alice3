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

package edu.cmu.cs.dennisc.tree;

/**
 * @author Dennis Cosgrove
 */
public class DefaultNode<T> implements Node<T> {
	private final T value;
	private final java.util.List<DefaultNode<T>> children;

	public static <T> DefaultNode<T> createUnsafeInstance( T value, Class<T> cls ) {
		return new DefaultNode<T>( value, false );
	}

	public static <T> DefaultNode<T> createSafeInstance( T value, Class<T> cls ) {
		return new DefaultNode<T>( value, true );
	}

	private DefaultNode( T value, boolean isCopyOnWrite ) {
		this.value = value;
		if( isCopyOnWrite ) {
			this.children = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
		} else {
			this.children = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		}
	}

	public void addChild( DefaultNode<T> node ) {
		this.children.add( node );
	}

	public DefaultNode<T> addChild( T child ) {
		DefaultNode<T> rv = new DefaultNode<T>( child, this.children instanceof java.util.concurrent.CopyOnWriteArrayList );
		this.addChild( rv );
		return rv;
	}

	public void removeChild( DefaultNode<T> node ) {
		this.children.remove( node );
	}

	public DefaultNode<T> removeChild( T child ) {
		java.util.ListIterator<DefaultNode<T>> listIterator = this.children.listIterator();
		while( listIterator.hasNext() ) {
			DefaultNode<T> node = listIterator.next();
			if( node.getValue().equals( child ) ) {
				listIterator.remove();
				return node;
			}
		}
		return null;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@Override
	public java.util.List<DefaultNode<T>> getChildren() {
		return this.children;
	}

	@Override
	public boolean contains( T value ) {
		return get( value ) != null;
	}

	@Override
	public edu.cmu.cs.dennisc.tree.DefaultNode<T> get( T value ) {
		if( edu.cmu.cs.dennisc.java.util.Objects.equals( this.value, value ) ) {
			return this;
		} else {
			for( DefaultNode<T> child : this.children ) {
				DefaultNode<T> rv = child.get( value );
				if( rv != null ) {
					return rv;
				}
			}
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "DefaultNode[" );
		sb.append( this.value != null ? this.value.toString() : null );
		sb.append( "]" );
		return sb.toString();
	}
}
